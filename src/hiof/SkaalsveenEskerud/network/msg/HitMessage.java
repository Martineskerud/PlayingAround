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
public class HitMessage extends AbstractMessage {

    public String target;
    public float damage;

    public HitMessage() {
    }

    public HitMessage(String target, float damage) {

        this.target = target;
        this.damage = damage;
    }
}
