/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.Message;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import hiof.SkaalsveenEskerud.network.ClientData;
import hiof.SkaalsveenEskerud.network.ServerDataHandler;
import java.util.HashMap;

/**
 *
 * @author anskaal
 */
@Serializable
public class ServerSummarizeMessage extends AbstractMessage {

    public static Message getInstance(ServerDataHandler dataHandler) {

        ServerSummarizeMessage ssm = new ServerSummarizeMessage();
        ssm.playerData = dataHandler.getPlayerData();
        return ssm;
    }
    public HashMap<String, ClientData> playerData;

    public ServerSummarizeMessage() {
        Serializer.registerClass(ClientData.class);
    }
}
