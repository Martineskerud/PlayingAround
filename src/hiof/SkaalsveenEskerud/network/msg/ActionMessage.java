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
public class ActionMessage extends AbstractMessage {

    public static String ACTION_JUMP_PLAYER = "jumpShootPlayer";
    public static String ACTION_RESPAWN_PLAYER = "player respawn";
    public float value2;
    public String value1;
    public String action;

    public ActionMessage() {
    }

    public ActionMessage(String action, String value1, float value2) {
        this.action = action;
        this.value1 = value1;
        this.value2 = value2;

    }
}
