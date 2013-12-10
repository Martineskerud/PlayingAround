/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author anskaal
 */
@Serializable
public class PingMessage extends AbstractMessage {

    public long timestamp;
    public boolean fromServer = false;

    public PingMessage() {
    }

    public PingMessage setClientTimestamp() {

        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public long getPing() {
        return (System.currentTimeMillis() - timestamp) / 2;
    }

    public PingMessage setFromServer(boolean b) {
        fromServer = b;
        return this;
    }

    public boolean isFromServer() {
        return fromServer;
    }
}
