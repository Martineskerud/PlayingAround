/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import hiof.SkaalsveenEskerud.network.GameClient2;
import hiof.SkaalsveenEskerud.network.msg.PositionMessage;

/**
 *
 * @author anskaal
 */
public class SpatialStalker extends AbstractControl{
    private final Spatial node;
    private final long frequency;
    private long lastStalk;
    private final GameClient2 client;

    public SpatialStalker(GameClient2 client, Spatial node, long frequency){
        this.node = node;
        this.frequency = frequency;
        this.client = client;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
        long now = System.currentTimeMillis();
        if(now - lastStalk > frequency){
           lastStalk = now;
           
           Vector3f t = node.getLocalTranslation();
           node.setLocalTranslation(t.x, t.y+1, t.z);
           stalk();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    private void stalk() {
        Vector3f pos = node.getLocalTranslation();
        Quaternion rot = node.getLocalRotation();
        String name = node.getName();
        
        client.send(new PositionMessage(pos, rot, name));
        System.out.println("Stalk...");
    }
    
}
