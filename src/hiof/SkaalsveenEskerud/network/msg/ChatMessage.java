/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author root
 */
@Serializable
public class ChatMessage extends AbstractMessage {

    public String message;
    public long timestamp;
    public String player;

    public ChatMessage() {
    }

    public ChatMessage(String msg) {
        message = msg;
    }

    public ChatMessage(String msg, String origin) {
        message = msg;
        player = origin;
        timestamp = System.currentTimeMillis();
    }
}
