/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.character;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import static hiof.SkaalsveenEskerud.character.Oto.HEIGHT;
import hiof.SkaalsveenEskerud.control.EvenBetterCharacterControl;

/**
 *
 * @author anskaal
 */
public class PhysicalPlayer extends Node {

    public final static float HEIGHT = 12f;
    public EvenBetterCharacterControl playerControl;

    public PhysicalPlayer(String name) {
        super(name);
        
        
    }

    protected void createPlayerControl() {
        playerControl = new EvenBetterCharacterControl(3f, HEIGHT, 1000f);
        playerControl.setSpatial(this);
        playerControl.setJumpForce(new Vector3f(0f, 33000f, 0f));
        
        PhysicsRigidBody rBody = playerControl.getRigidBody();
        rBody.setCollisionGroup(RigidBodyControl.COLLISION_GROUP_02);
        rBody.setCollideWithGroups(RigidBodyControl.COLLISION_GROUP_01);
    }
}
