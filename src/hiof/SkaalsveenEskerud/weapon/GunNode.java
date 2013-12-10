/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.weapon;

import com.jme3.scene.Node;

/**
 *
 * @author root
 */
public class GunNode extends Node {

    public long triggerDelay = 700;
    public float damage = 50f;
    public float accuracy = 0.5f;

    public GunNode(String name) {
        super(name);
    }

    public float getDamage() {
        return damage * (float) (Math.random() * (1 / accuracy));
    }
}
