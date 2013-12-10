/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import hiof.SkaalsveenEskerud.world.model.PowerUp;
import java.util.ArrayList;

/**
 *
 * @author Martin
 */
public class PowerUpCollisionControl extends RigidBodyControl implements PhysicsCollisionListener {
    public static final String PLAYER_NODE = "playerNode";

    private final ArrayList<MyCollisionListener> myCollisionListeners;
    private final PowerUp powerUp;

    public PowerUpCollisionControl(BulletAppState bas, PowerUp powerUp) {
        this.powerUp = powerUp;
        myCollisionListeners = new ArrayList<MyCollisionListener>();
        bas.getPhysicsSpace().addCollisionListener(this);
    }

    /**
     * This method triggers two times for each collision.
     * You only need to check node A for name since it switches the
     * nodes and fires method again after first one.
     * 
     */
    public void collision(PhysicsCollisionEvent event) {

        
        triggerRemoveOnPowerUp(event.getNodeB());
  /*
        //collision with fire.
        if (("OilDrum".equals(a)) && "playerNode".equals(b) || ("OilDrum".equals(b)) && "playerNode".equals(a)) {

            // System.out.println("Svidd barn blir pyroman!");
            if ("OilDrum".equals(a)) {
                // nodeA.getControl(RigidBodyControl.class).setEnabled(false);
                //reduce health by 1
            }
            if ("OilDrum".equals(b)) {
                //  nodeB.getControl(RigidBodyControl.class).setEnabled(false);
                //reduce health by 1
            }
        }

*/
    }

    public void addMyCollisionListener(MyCollisionListener mcl) {

        myCollisionListeners.add(mcl);

    }

    private void triggerRemoveOnPowerUp(Spatial spatial) {
        
        if(spatial != null){
            final String name = spatial.getName();

            if(powerUp.isPowerUp(name)){

                for (MyCollisionListener mcl : myCollisionListeners) {
                    mcl.onCollision(powerUp.getSubNode(name), name);
                }
                
            }
            else{
                triggerOtherCollision(name);
            }
        }
    }

    private void triggerOtherCollision(final String name) {
        for (MyCollisionListener mcl : myCollisionListeners) {
            mcl.onOtherObjects(name);
        }
    }

    public interface MyCollisionListener {

        void onCollision(PowerUpControl element, String name);
        void onOtherObjects(String name);
        
    }
}
