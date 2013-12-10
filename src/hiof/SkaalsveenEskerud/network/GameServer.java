/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import hiof.SkaalsveenEskerud.network.msg.TestMessage;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.kernel.KernelException;
import com.jme3.network.serializing.Serializer;
import hiof.SkaalsveenEskerud.network.msg.ActionMessage;

import hiof.SkaalsveenEskerud.network.msg.ChatMessage;
import hiof.SkaalsveenEskerud.network.msg.HitMessage;
import hiof.SkaalsveenEskerud.network.msg.PingMessage;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;
import hiof.SkaalsveenEskerud.network.msg.ServerSummarizeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anskaal
 */
public class GameServer extends Thread implements ConnectionListener {

    private Server server;
    private boolean isConnected;
    private static boolean isStarted = false;
    protected final ServerDataHandler dataHandler;
    private int connections = 0;
    private ArrayList<OnServerStartListener> ossl;
    private BroadcastDiscoveryListener bdl;

    public GameServer() {
        super("GameServer-Thread");

        System.out.println("Creating GameServer.");

        dataHandler = new ServerDataHandler(this);

        Serializer.registerClass(HitMessage.class);
        Serializer.registerClass(ChatMessage.class);
        Serializer.registerClass(ActionMessage.class);

        ossl = new ArrayList<OnServerStartListener>();
    }

    @Override
    public void run() {

        if (isConnected && ossl != null) {

            bdl = new BroadcastDiscoveryListener();
            bdl.start();

            onServerStart();
        } else {
            System.err.println("Could not launch onStartServerListener... null or not connected");
        }

        while (isConnected) {

            if (Math.random() > 0.9999) { // update some times
                //dataHandler.updateBots();
            }

            broadcastPositions();

            try {
                Thread.sleep(NetworkManager.POSISTION_MESSAGE_WAIT_TIME);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Server stopped sending pos...");
    }

    public void close() {

        if (bdl != null) {
            bdl.close();
        }

        if (server != null) {
            server.close();
        }

        isConnected = false;
        boolean retry = true;

        while (retry) {

            try {
                join();
                retry = false;
                break;
            } catch (InterruptedException ex) {
            }
        }


        System.out.println("Server stopped.");

    }
    private MessageListener broadcastHandler = new MessageListener<HostedConnection>() {
        public void messageReceived(HostedConnection source, Message m) {

            if (m instanceof PositionMessage) {
                PositionMessage pm = (PositionMessage) m;
                
                if (pm.subject != null) {
                    server.broadcast(pm);
                }

            } else if (m instanceof ChatMessage) {
                ChatMessage cm = (ChatMessage) m;

                if (cm.message.startsWith("/")) {
                    System.out.println("Server Command: " + cm.message);
                } else {
                    cm.timestamp = System.currentTimeMillis();
                    cm.player = dataHandler.getPlayerID(source);

                    server.broadcast(cm);
                }
            } else if (m instanceof ActionMessage) {
                
                ActionMessage am = (ActionMessage) m;
                System.out.println("SERVER: AM: "+ am.action);
                server.broadcast(am);
            } 

        }
    };

    private void addListeners() {

        addMessageListener(dataHandler, TestMessage.class);
        addMessageListener(dataHandler, PingMessage.class);
        addMessageListener(dataHandler, PositionMessage.class);
        addMessageListener(broadcastHandler, PositionMessage.class);
        addMessageListener(dataHandler, HitMessage.class);
        addMessageListener(broadcastHandler, ActionMessage.class);
        addMessageListener(broadcastHandler, ChatMessage.class);

        server.addConnectionListener(this);

    }

    public void addConnectionListener(ConnectionListener cl) {
        server.addConnectionListener(cl);
    }

    public void startServer() {

        if (!isStarted) {
            isStarted = true;
            try {
                AddressProvider.host();
            } catch (IOException ex) {
                System.err.println("SERVER: Could not connect to swe.no. Only LAN connections possible.");
            }

            try {
                server = Network.createServer(NetworkManager.GAME_NAME, NetworkManager.GAME_VERSION, NetworkManager.PORT, NetworkManager.PORT);
                System.out.println("Starting server...");
                server.start();
                isConnected = true;

                start();
                addListeners();
            } catch (IOException ex) {
                System.err.println("SERVER: Could not create server. Check netowrk interface...");
            } catch (KernelException e) {
                try {
                    Thread.sleep(1000);
                    System.err.println("Port " + NetworkManager.PORT + " is probably bussy. I'll Increment by 1 and try again... ");
                    NetworkManager.PORT++;
                    startServer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            System.err.println("SERVER ERROR: Already one instance of GameServer is running.");
            close();
        }
    }

    public void addMessageListener(MessageListener ml, Class<?> cl) {

        Serializer.registerClass(cl);
        server.addMessageListener(ml, cl);
    }

    public void connectionAdded(Server server, HostedConnection conn) {
        connections++;
        System.out.println("SEVER: connection added " + conn.getAddress());

    }

    public void connectionRemoved(Server server, HostedConnection conn) {
        connections--;
        System.out.println("SEVER: connection removed");


    }

    public void addBot() {
        connections++;
        dataHandler.addBot();
    }

    public void removeBot(String i) {
        connections--;
        dataHandler.removePlayer(i);
    }

    private void broadcastPositions() {

        if (connections > 0 && server != null) {
            server.broadcast(ServerSummarizeMessage.getInstance(dataHandler));
        }
    }

    public void addOnServerStartListener(OnServerStartListener listener) {
        if (ossl != null) {
            ossl.add(listener);
        }
    }

    private void onServerStart() {
        for (OnServerStartListener o : ossl) {
            o.onServerStart();
        }


    }

    public Server getServerConnection() {
        return server;
    }

    public interface OnServerStartListener {

        public void onServerStart();
    }
}
