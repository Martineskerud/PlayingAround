/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.Random;
import hiof.SkaalsveenEskerud.world.model.util.RandomInt;

/**
 *
 * @author martin
 */
public class MeteorControl extends AbstractControl {

    private final Random rand;
    private final Spatial meteor;
    private final RigidBodyControl rbc;
    private long timeStamp;

    public MeteorControl(Geometry inputMeteor) {
        rand = new Random();
        this.meteor = inputMeteor;
        //this is null
        rbc = meteor.getControl(RigidBodyControl.class);
        timeStamp = System.currentTimeMillis();


    }

    @Override
    protected void controlUpdate(float tpf) {
        //Making it rain, respawn at random location every 1minute.
        if (System.currentTimeMillis() > timeStamp + 60000) {
            timeStamp = System.currentTimeMillis();
            float x = RandomInt.rangedRandomInt(0, 600, rand);
            float y = RandomInt.rangedRandomInt(100, 150, rand);
            float z = RandomInt.rangedRandomInt(0, 500, rand);

            rbc.setPhysicsLocation(new Vector3f(x, y, z));

        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
