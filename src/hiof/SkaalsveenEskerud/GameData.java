/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import hiof.SkaalsveenEskerud.network.ClientData;
import hiof.SkaalsveenEskerud.network.PlayerManager;
import hiof.SkaalsveenEskerud.network.msg.PingMessage;
import hiof.SkaalsveenEskerud.network.msg.ServerSummarizeMessage;
import hiof.SkaalsveenEskerud.network.msg.TestMessage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author anskaal
 */
public class GameData implements MessageListener<Client> {

    private long ping = -1;
    private final ArrayList<OnDataChangedListener> onDataChangedListeners;
    public HashMap<String, ClientData> playerData;
    public String myPlayerKey;

    /**
     * yoyo
     */
    public GameData() {
        onDataChangedListeners = new ArrayList<OnDataChangedListener>();
    }

    public void messageReceived(Client source, Message m) {

        if (m instanceof PingMessage) {

            PingMessage pm = (PingMessage) m;

            if (pm.isFromServer()) {
                this.ping = pm.getPing();
            }
        }

        if (m instanceof ServerSummarizeMessage) {

            ServerSummarizeMessage ssm = (ServerSummarizeMessage) m;
            playerData = ssm.playerData;

            if (onDataChangedListeners != null) {
                for (OnDataChangedListener odcl : onDataChangedListeners) {
                    odcl.onDataChanged(this);
                }
            } else {
                System.out.println("GameData: no listeners for serverSummarizeMessages.");
            }
        }

        if (m instanceof TestMessage) {
            TestMessage tm = (TestMessage) m;
            myPlayerKey = tm.playerKey;
        }

    }

    public ClientData getMyData() {
        if (myPlayerKey != null && playerData.containsKey(myPlayerKey)) {
            return playerData.get(myPlayerKey);
        }
        return null;
    }

    public void addDataChangeListener(OnDataChangedListener listener) {
        onDataChangedListeners.add(listener);
    }

    public long getPing() {
        return ping;
    }

    public interface OnDataChangedListener {

        public void onDataChanged(GameData gameData);
    }

    public boolean isPlayer(String name) {

        if (playerData != null) {
            return playerData.containsKey(name);
        }
        return false;
    }
}
