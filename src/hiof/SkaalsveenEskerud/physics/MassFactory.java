/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.physics;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author martin
 */
public class MassFactory {

    private BulletAppState bas;
    private RigidBodyControl rbc;

    public MassFactory(BulletAppState bas) {
        this.bas = bas;
        rbc = new RigidBodyControl();
    }

    public void assignMass(Spatial inputGeo, float value, Vector3f inputGrav) {
        rbc = new RigidBodyControl(value);
        inputGeo.addControl(rbc);

        if (bas != null) {
            bas.getPhysicsSpace().add(rbc);
        } else {
            System.err.println("BulletAppState is null!!!");
        }
        if (inputGrav == null) {
            //70 is the new 9.81 ~
            rbc.setGravity(new Vector3f(0, -70f, 0));
        } else {
            rbc.setGravity(inputGrav);
        }
    }

    public void assignMass(Spatial inputGeo, float value, Vector3f inputGrav, boolean kinematic) {
        rbc = new RigidBodyControl(value);
        inputGeo.addControl(rbc);
        if (kinematic) {
            rbc.setKinematic(true);
        }
        if (bas != null) {
            bas.getPhysicsSpace().add(rbc);
        } else {
            System.err.println("BulletAppState is null!!!");
        }
        if (inputGrav == null) {
            //70 is the new 9.81 ~
            rbc.setGravity(new Vector3f(0, -70f, 0));
        } else {
            rbc.setGravity(inputGrav);
        }
    }
}