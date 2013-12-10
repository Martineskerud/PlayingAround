/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.hud;

import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import com.jme3.niftygui.NiftyJmeDisplay;
import hiof.SkaalsveenEskerud.GameData;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.appstate.NiftyAppState;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenHandler;
import hiof.SkaalsveenEskerud.character.Oto;
import hiof.SkaalsveenEskerud.hud.ChatHud.OnSendListener;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.network.msg.ActionMessage;
import hiof.SkaalsveenEskerud.network.msg.ChatMessage;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 *
 * @author anskaal
 */
public abstract class DisplayManager implements GameData.OnDataChangedListener, MessageListener<Client>, ConnectionListener {

    public final Hud hud;
    private final HashMap<String, Oto> nodeForOtherPlayer;
    public Main main;
    private NiftyAppState nas;
    private final ChatHud chatHud;
    private final PlayerList playerList;
    private final Menu menu;

    public DisplayManager(Main main, Hud hud, InputManager inputManager) {
        this.hud = hud;
        this.main = main;

        nodeForOtherPlayer = new HashMap<String, Oto>();
        NiftyJmeDisplay niftyDisplay = main.getNIftyDisplay();

        nas = new NiftyAppState(main.getStateManager(), niftyDisplay);

        chatHud = new ChatHud(nas, inputManager);
        nas.addScreen(chatHud);

        playerList = new PlayerList(nas);
        nas.addScreen(playerList);

        menu = new Menu(nas, main);
        nas.addScreen(menu);
        nas.addControls();

        NiftyScreenHandler nsh = new NiftyScreenHandler(nas);
        inputManager.addListener(nsh, KeyMapper.SHOW_PLAYER_LIST);
        inputManager.addListener(nsh, KeyMapper.HIDE_CHAT, KeyMapper.SHOW_CHAT);

        main.getStateManager().attach(nas);
    }

    public void setChatMessageListener(OnSendListener osl) {
        chatHud.setOnSendListener(osl);
    }

    public void onPingCalculated(long ping) {
    }

    public void onDataChanged(final GameData gameData) {

        main.enqueue(new Callable<Void>() {
            public Void call() throws Exception {

                for (String key : gameData.playerData.keySet()) {

                    if (!key.equals(gameData.myPlayerKey)) {
                        Oto obj;
                        if (nodeForOtherPlayer.containsKey(key)) {
                            obj = nodeForOtherPlayer.get(key);
                        } else {
                            obj = createPlayerObject(key);
                            nodeForOtherPlayer.put(key, obj);
                        }

                        Vector3f pos = gameData.playerData.get(key).posMsg.getTranslation();
                        Quaternion rot = gameData.playerData.get(key).posMsg.getRotation();
                        obj.update(pos, rot);

                    } else {
                        main.playerManager.healthManager.setHpSource(gameData.playerData.get(key), key);
                    }
                }

                return null;
            }
        });

        playerList.onDataChanged(gameData);
    }

    public abstract Oto createPlayerObject(String playerKey);

    public void setServer(boolean isServer) {
        hud.queueText((isServer ? "Server" : "Client"), 5000);
    }

    public void messageReceived(Client source, Message m) {

        if (m instanceof ChatMessage) {
            chatHud.addMessage((ChatMessage) m);
        } else if (m instanceof ActionMessage) {
            ActionMessage am = (ActionMessage) m;
            
            if (am.action.equals(ActionMessage.ACTION_JUMP_PLAYER)) {
                if (am.value1.equals(main.playerManager.gameData.myPlayerKey)) {
                    main.walkableState.character.jump();
                }
            }

            if (am.action.equals(ActionMessage.ACTION_RESPAWN_PLAYER)) {
                main.walkableState.respawnSelf();
                hud.updateHealthUi(100);
            }
        } else {
            System.out.println("DisplayManager: messageReceived. Couldn't recognize format.");
        }


    }

    public void setMessage(String msg) {
        hud.queueText(msg, 3000);
    }

    public void setHp(int hp) {
        hud.updateHealthUi(hp);
    }

    public void connectionAdded(Server server, HostedConnection conn) {
        setMessage(conn.getAddress() + " joined the game.");
    }

    public void connectionRemoved(Server server, HostedConnection conn) {
    }

}
