/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.hud.DisplayManager;
import hiof.SkaalsveenEskerud.network.GameServer.OnServerStartListener;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 *
 * @author anskaal
 */
public class NetworkManager {

    public static final long POSISTION_MESSAGE_WAIT_TIME = 5;
    public static int PORT = 6147;
    public static final String GAME_NAME = "PlayingAround";
    public static final int GAME_VERSION = 1;
    static boolean USE_INTERNET = false;
    protected PlayerManager playerManager;
    protected PadServer padServer;
    private GameServer gameServer;
    public GameClient2 gameClient;
    private final Camera cam;

    public NetworkManager(Node rootNode, Main main) {
        super();

        this.cam = main.getCamera();
        this.playerManager = main.playerManager;

        gameClient = new GameClient2(this, main);
        gameClient.start();
        playerManager.attachNetworkClient(gameClient);

    }

    public static String makeConnectionString() {
        return PORT + "," + GAME_NAME.replace(",", "") + "," + GAME_VERSION;
    }
    GameServer.OnServerStartListener ossl = new GameServer.OnServerStartListener() {
        public void onServerStart() {

            if (playerManager != null) {

                gameServer.addConnectionListener(gameServer.dataHandler);

                DisplayManager dm = playerManager.getDisplaymanager();
                if (dm != null) {
                    gameServer.addConnectionListener(dm);
                } else {
                    System.out.println("Could not add DisplayManager as listener. NullPointerException.");
                }
            } else {
                System.out.println("Could not add DisplayManager as listener. PlayerManager is null.");
            }
        }
    };

    public void startServer(OnServerStartListener clientTrigger) {

        if (gameServer == null) {
            gameServer = new GameServer();
            gameServer.addOnServerStartListener(clientTrigger);
            gameServer.addOnServerStartListener(ossl);
            gameServer.startServer();
            playerManager.setServer(true);
        } else {
            System.err.println("Two instances of GameServer is not allowed.");
        }

    }

    public void close() {

        if (USE_INTERNET) {
            try {
                AddressProvider.disconnect();
            } catch (MalformedURLException ex) {
                System.err.println("Could not discconnect correctly... MalformedURLException.");
            } catch (IOException ex) {
                System.err.println("Could not discconnect correctly... IOException.");
            }
        }

        if (gameClient != null) {
            gameClient.close();
        }

        if (gameServer != null) {
            gameServer.close();
        }

        if (padServer != null) {
            padServer.closeConnection();
        }
    }

    public GameClient2 getClient() {
        return gameClient;
    }

    PlayerManager getPlayerManager() {
        return playerManager;
    }

    public String getReport() {
        return (USE_INTERNET ? "" : "INTERNET DISABLED")
                + "\nPosition update time: " + POSISTION_MESSAGE_WAIT_TIME + "ms"
                + "\n" + gameClient.toString();
    }
}
