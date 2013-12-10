/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.scene.Geometry;

/**
 *
 * @author root
 */
interface HitListener {

    public void onHit(Geometry geom, String name, String triggerKey);
}
