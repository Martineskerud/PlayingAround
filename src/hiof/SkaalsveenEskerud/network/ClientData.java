/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.network.serializing.Serializable;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;

/**
 *
 * @author anskaal
 */
@Serializable
public class ClientData {

    public String ip;
    public long ping;
    public int score;
    public int deaths;
    public PositionMessage posMsg;
    public String name;
    public int hp = 100;

    @Override
    public String toString() {
        return "CD: " + posMsg;
    }
}
