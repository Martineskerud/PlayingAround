/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;
import com.jme3.network.Message;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;
import com.jme3.renderer.Camera;
import hiof.SkaalsveenEskerud.appstate.WalkableState;
import hiof.SkaalsveenEskerud.hud.ChatHud;
import hiof.SkaalsveenEskerud.network.msg.ActionMessage;
import hiof.SkaalsveenEskerud.network.msg.HitMessage;
import hiof.SkaalsveenEskerud.network.msg.PingMessage;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;
import hiof.SkaalsveenEskerud.network.msg.TestMessage;
import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class BasicGameClient extends Thread implements ChatHud.OnSendListener, ErrorListener<Client>, ClientStateListener {

    private boolean connected = false;
    private final Camera camera;
    protected Client networkClient;
    public static final int CONNECT_SELF = 1;
    private boolean running;
    private WalkableState walkableState;

    public BasicGameClient(Camera cam) {
        super("BasicGameThread");
        this.camera = cam;

    }

    @Override
    public void run() {
        super.run();

        init();
        running = true;
        while (running) {

            if (connected) {
                sendPositionMessage();
            }

            try {
                Thread.sleep(NetworkManager.POSISTION_MESSAGE_WAIT_TIME / 2);
            } catch (InterruptedException ex) {
                Logger.getLogger(BasicGameClient.class.getName()).log(Level.WARNING, null, ex);
            }
        }

    }

    public void close() {

        if (networkClient != null && networkClient.isConnected()) {
            System.out.println("CLIENT: Closing connection.");
            networkClient.close();
        }

        running = false;
        boolean retry = true;

        while (retry) {

            try {
                join();
                retry = false;
                break;
            } catch (InterruptedException ex) {
            }
        }

        System.err.println("Client closed.");
    }

    public void onSend(String msg) {
        System.out.println("CHAT: " + msg);
    }

    private void sendPositionMessage() {

        if (walkableState != null && walkableState.character != null) {
            Vector3f pos = walkableState.character.getPhysicsLocation();
            Quaternion rot = camera.getRotation();

            send(new PositionMessage(pos, rot));
            send(new PingMessage().setClientTimestamp());
        } else {
            //System.err.println("myCharacterControl is null");
        }
    }

    protected void init() {

        Serializer.registerClass(ClientData.class);
        Serializer.registerClass(TestMessage.class);
        Serializer.registerClass(PositionMessage.class);
        Serializer.registerClass(PingMessage.class);
        Serializer.registerClass(HitMessage.class);
        Serializer.registerClass(ActionMessage.class);

    }

    public boolean connect(String addr, String message) {

        if (addr == null) {
            System.out.println("No addresses: " + message);
        }

        if (!connected) {

            System.out.println("Connecting network-client: " + addr);

            try {
                networkClient = Network.connectToServer(NetworkManager.GAME_NAME, NetworkManager.GAME_VERSION, addr, NetworkManager.PORT, NetworkManager.PORT);
                System.out.println("Contacting server[" + addr + "]");
                networkClient.addErrorListener(this);
                networkClient.addClientStateListener(this);
                networkClient.start();
                connected = true;

                return true;
            } catch (ConnectException ce) {
                System.out.println("FAILED TO CONNECT");

            } catch (IOException ex) {
                System.out.println("FAILED TO CONNECT");
                ex.printStackTrace();
            }
        } else {
            System.err.println("CLIENT: Client allready connected: " + message);
        }
        return false;
    }

    public void handleError(Client source, Throwable t) {

        if (networkClient != null && !networkClient.isConnected()) {
            System.err.println("CLIENT: network error. Connection is closed");
        } else {
            System.err.println("NETWORK ERROR: " + t.getMessage());

            if (t instanceof ConnectException) {
                if (connected) {
                    close();
                    System.out.println("Server shut down.");
                }
            } else {
                t.printStackTrace();
            }
        }

    }

    public void clientConnected(Client c) {
        setupListeners();
        System.out.println("CLIENT: connected.");
        c.send(new TestMessage("Hello World!"));
    }

    public void clientDisconnected(Client c, DisconnectInfo info) {
        System.out.println("CLIENT: disconnected. ");
        running = false;

    }

    public void send(Message m) {
        if (connected) {
            networkClient.send(m);
        }
    }

    protected void setupListeners() {
    }

    public void bindWalkableState(WalkableState walkableState) {
        System.out.println("setting setMyCharacterControl");
        this.walkableState = walkableState;
    }

    @Override
    public String toString() {
        return "  \nState:" + (connected ? "connected" : "not connected");
    }
}
