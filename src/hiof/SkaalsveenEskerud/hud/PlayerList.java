/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.hud;

import de.lessvoid.nifty.Nifty;
import hiof.SkaalsveenEskerud.GameData;
import hiof.SkaalsveenEskerud.appstate.NiftyAppState;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenControl;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenHandler;
import hiof.SkaalsveenEskerud.network.ClientData;

/**
 *
 * @author anskaal
 */
public class PlayerList extends NiftyScreenControl implements GameData.OnDataChangedListener {

    private String[] data = new String[]{"", "", "", ""};
    private int players = 0;

    PlayerList(NiftyAppState nas) {
        super(nas, NiftyScreenHandler.SCREEN_PLAYERS);
    }

    public String getPlayerCount() {
        return players + "";
    }

    public String getPlayers() {
        return data[0];
    }

    public String getScores() {

        return data[1];
    }

    public String getDeaths() {
        return data[2];
    }

    public String getLatencies() {
        return data[3];
    }

    private void splitPlayerHashMap(GameData gameData) {

        if (gameData != null && gameData.playerData != null) {
            String[] keys = new String[]{""};
            StringBuilder[] sb = new StringBuilder[4];

            sb[0] = new StringBuilder();
            sb[1] = new StringBuilder();
            sb[2] = new StringBuilder();
            sb[3] = new StringBuilder();


            players = 0;
            for (String key : gameData.playerData.keySet()) {

                players++;
                ClientData cd = gameData.playerData.get(key);

                if (cd != null) {
                    sb[0].append(cd.ip);
                    sb[0].append("\n");

                    sb[1].append(cd.score);
                    sb[1].append("\n");

                    sb[2].append(cd.deaths);
                    sb[2].append("\n");

                    sb[3].append(cd.ping);
                    sb[3].append("\n");
                }
            }

            data[0] = sb[0].toString();
            data[1] = sb[1].toString();
            data[2] = sb[2].toString();
            data[3] = sb[3].toString();
        }
    }

    public void onDataChanged(GameData gameData) {
        splitPlayerHashMap(gameData);
        requireRefresh();
    }

    @Override
    public void onPostLoad(float tpf) {
    }

    @Override
    public void loadXML(Nifty nifty, String xml) {
        nifty.fromXml(xml, getName(), PlayerList.this);
    }
}
