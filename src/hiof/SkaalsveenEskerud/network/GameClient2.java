/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.network.MessageListener;
import com.jme3.network.serializing.Serializer;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.hud.DisplayManager;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.network.msg.ActionMessage;
import hiof.SkaalsveenEskerud.network.msg.ChatMessage;
import hiof.SkaalsveenEskerud.network.msg.HitMessage;
import hiof.SkaalsveenEskerud.network.msg.PingMessage;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;
import hiof.SkaalsveenEskerud.network.msg.ServerSummarizeMessage;
import hiof.SkaalsveenEskerud.network.msg.TestMessage;
import hiof.SkaalsveenEskerud.weapon.Pistol;

/**
 *
 * @author root
 */
public class GameClient2 extends BasicGameClient {

    private final NetworkManager networkManager;
    private BroadcastDiscoveryClient bdc;
    private final Main main;

    public GameClient2(NetworkManager networkManager, Main main) {
        super(main.getCamera());
        this.main = main;
        
        Serializer.registerClass(ServerSummarizeMessage.class);
        Serializer.registerClass(ChatMessage.class);

        this.networkManager = networkManager;
    }

    private void addMessageListener(MessageListener ml, Class<?> cl) {

        System.out.println("Adding MessageListener[" + cl.getName() + "]");
        if (networkClient != null) {
            networkClient.addMessageListener(ml, cl);
        }
        
    }

    public void sendActionMessage(String action, String value1, float value2) {

        if (networkClient != null) {
            System.out.println("Sending Action message: " + action);
            ActionMessage am = new ActionMessage(action, value1, value2);
            networkClient.send(am);
        } else {
            System.err.println("CLIENT: Action message not sent. Client connection not established...");
        }
    }

    @Override
    protected void init() {

        super.init();

        bdc = new BroadcastDiscoveryClient() {
            @Override
            public void onServerFound(String ip, String connectionString) {


                System.out.println("ip:" + ip + ", " + connectionString);
                if (connect(ip, "Ethernet")) {
                    System.out.println("Successfully connected to: " + ip + ", " + connectionString);
                } else {
                    System.out.println("No luck connecting to: " + ip + ", " + connectionString);
                }
            }

            @Override
            public void noServersResponded() {

                System.out.println("ETHERNET BROADCAST: No server response...");

                if (NetworkManager.USE_INTERNET && connect(getInternetAddress(), "Internet")) {
                    return;
                }

                connectToSelf();

            }
        };
        bdc.start();

    }

    @Override
    public void close() {

        super.close();
    }

    private String getInternetAddress() {

        String addr = null;
        try {
            addr = AddressProvider.connect().toArray(new String[0])[0];

        } catch (Exception ex) {
            System.err.println("Could not contact online address provider...");
        }

        return addr;
    }

    private void connectToSelf() {
        networkManager.startServer(new GameServer.OnServerStartListener() {
            public void onServerStart() {


                if (!connect("localhost", "Server Started")) {

                    System.err.println("CLIENT: Every attempt to join a server failed.");
                }

            }
        });
    }

    @Override
    protected void setupListeners() {

        PlayerManager playerManager = networkManager.getPlayerManager();

        if (playerManager != null) {
            addMessageListener(playerManager, TestMessage.class);
            addMessageListener(playerManager, ServerSummarizeMessage.class);
            addMessageListener(playerManager, PingMessage.class);
            
            DisplayManager dm = playerManager.getDisplaymanager();
            addMessageListener(dm, ChatMessage.class);
            addMessageListener(dm, ActionMessage.class);
        } else {
            System.err.println("player manager is null. Cannot set listeners in client!");
        }
    }

    public void onSend(String msg) {

        if (networkClient != null) {

            send(new ChatMessage(msg));
        } else {
            System.out.println("Could not send message. Client-object is null.");
        }

    }

    void damagePlayerByWeapon(String name, String currentWeapon, String triggerKey) {

        if (triggerKey.equals(KeyMapper.PLAYER_SHOOT2)) {
            ActionMessage am = new ActionMessage(ActionMessage.ACTION_JUMP_PLAYER, name, 1);
            networkClient.send(am);
        } else if (Pistol.guns != null) {
            
            if (Pistol.guns.containsKey(currentWeapon)) {
                final float damage = Pistol.guns.get(Pistol.currentWeapon).getDamage();
                sendDamage(name, damage);
            } else {
                System.err.println("Illegal weapon!");
            }
        } else {
            System.err.println("Guns not created yet.");
        }
    }

    public void sendDamage(String name, final float health) {
        
        if(networkClient != null){
            System.out.println("CLIENT: sendDamage: "+ health);
            HitMessage hm = new HitMessage(name, health);
            networkClient.send(hm);
        }
    }
}
