/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import hiof.SkaalsveenEskerud.network.msg.ActionMessage;
import hiof.SkaalsveenEskerud.network.msg.HitMessage;
import hiof.SkaalsveenEskerud.network.msg.PingMessage;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;
import hiof.SkaalsveenEskerud.network.msg.TestMessage;
import hiof.SkaalsveenEskerud.world.WorldData;
import java.util.HashMap;

/**
 *
 * @author anskaal
 *
 * We use this class to insert received data to a hashtable for the different
 * clients.
 */
public class ServerDataHandler implements MessageListener<HostedConnection>, ConnectionListener {

    private HashMap<String, ClientData> playerData;
    private HashMap<String, Integer> playerIdMapping;
    private HashMap<String, Bot> bots;
    private HitMessage hm;
    private final WorldData worldData;
    private final GameServer gameServer;

    public ServerDataHandler(GameServer gameServer) {
        this.gameServer = gameServer;
        this.playerData = new HashMap<String, ClientData>();
        this.playerIdMapping = new HashMap<String, Integer>();
        worldData = new WorldData();
    }

    public void messageReceived(HostedConnection source, Message m) {

        String ip = getPlayerID(source);
        playerIdMapping.put(ip, source.getId());

        if (m instanceof PositionMessage) {

            PositionMessage pm = (PositionMessage) m;
            
            if(pm.subject == null){
                handlePositionMessage(pm, ip);
            }

        } else if (m instanceof HitMessage) {

            hm = (HitMessage) m;

            if (playerData.containsKey(hm.target)) {
                
                System.out.println("Player " + hm.target + " hurt by " + hm.damage);
                playerData.get(hm.target).hp -= hm.damage;
                
            } else if (worldData.containsKey(hm.target)) {

                System.out.println("World object " + hm.target + " hurt by " + hm.damage);
                float value = worldData.get(hm.target);
                value -= hm.damage;
                worldData.put(hm.target, value);

            } else {
                System.out.println("SERVER ERROR: unknown player " + hm.target);
            }

            if (playerData.containsKey(hm.target)) {
                ClientData cd = playerData.get(hm.target);
                if (cd.hp <= 0) {
                    cd.hp = 100;

                    cd.deaths++;
                    ActionMessage am = new ActionMessage(ActionMessage.ACTION_RESPAWN_PLAYER, ip, hm.damage);
                    Server server = gameServer.getServerConnection();

                    if (playerIdMapping.containsKey(hm.target)) {
                        server.broadcast(Filters.in(server.getConnection(playerIdMapping.get(hm.target))), am);
                    } else {
                        System.err.println("Could not find player in playerIdMapping. Not killed...");
                    }
                    
                    
                    //set score ++
                    playerData.get(ip).score++;
                    
                    System.out.println("SERVER: "+ ip + " killed "+ hm.target);
                }
            }

        } else if (m instanceof TestMessage) {

            //player says Hi!
            TestMessage tm = (TestMessage) m;
            tm.playerKey = getPlayerID(source);
            source.send(tm);

        } else if (m instanceof PingMessage) {
            PingMessage pm = (PingMessage) m;
            source.send(pm.setFromServer(true));
        }

    }

    public void removePlayer(String ip) {
        playerData.remove(ip);
    }

    public HashMap<String, ClientData> getPlayerData() {

        return playerData;
    }

    public void connectionAdded(Server server, HostedConnection conn) {
        // DO NOTHING. Data will be created when player starts sending position.
    }

    public void connectionRemoved(Server server, HostedConnection conn) {

        removePlayer(getPlayerID(conn));
    }

    public String getPlayerID(HostedConnection conn) {
        return conn.getAddress().split(":")[0];
    }

    private void addPlayer(PositionMessage pm, String id) {
        ClientData cd = new ClientData();
        cd.posMsg = pm;
        cd.ip = id;
        cd.hp = 100;
        playerData.put(id, cd);

        System.out.println("SERVER: Player " + id + " added in player list.");
    }

    public void addBot() {

        if (bots == null) {
            bots = new HashMap<String, Bot>();
        }

        Bot bot = new Bot(this);
        bots.put(bot.getId(), bot);

        System.out.println("Bot added... TOTAL BOT COUNT: " + bots.size());

        bot.reportPosition();
    }

    public void removeBot(String name) {
        bots.remove(name);
    }

    public void handlePositionMessage(PositionMessage pm, String id) {


        if (playerData.containsKey(id)) {

            ClientData cd = playerData.get(id);
            cd.posMsg = pm;

        } else {
            addPlayer(pm, id);
        }

    }

    void updateBots() {

        if (bots != null && bots.size() > 0) {
            for (Bot b : bots.values()) {
                b.update();
            }
        }

    }
}
