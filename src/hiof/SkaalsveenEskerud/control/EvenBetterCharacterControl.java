package hiof.SkaalsveenEskerud.control;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * Made a class that would enable us to use getPhysicsLocation. This was a
 * method missing in BetterCharacterControl for some reason.
 *
 * @author anskaal
 */
public class EvenBetterCharacterControl extends BetterCharacterControl {

    JumpListener jl;
    boolean jumpState;
    private boolean lastJumpState;

    public EvenBetterCharacterControl() {
        super();
        lastJumpState = !isOnGround();
        
    }

    public EvenBetterCharacterControl(float radius, float height, float mass) {
        super(radius, height, mass);
    }

    public Vector3f getPhysicsLocation() {
        return rigidBody.getPhysicsLocation();
    }

    public void applyForce(Vector3f force, Vector3f loc) {
        rigidBody.applyForce(force, loc);
    }

    public PhysicsRigidBody getRigidBody() {
        return rigidBody;
    }

    public Quaternion getPhysicsRotation() {
        return rigidBody.getPhysicsRotation();
    }

    @Override
    public void setPhysicsRotation(Quaternion q) {
        if (rigidBody != null && q != null) {
            rigidBody.setPhysicsRotation(q);
        }
    }

    @Override
    public void setPhysicsLocation(Vector3f vec) {
        if (rigidBody != null && vec != null) {
            rigidBody.setPhysicsLocation(vec);
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        jumpState = !isOnGround();
        if (jl != null && jumpState != lastJumpState) {
            lastJumpState = jumpState;
            jl.onJumpStateChange(lastJumpState, tpf);
        }

    }

    public void setJumpListener(JumpListener jl) {
        this.jl = jl;
    }

    public interface JumpListener {

        public void onJumpStateChange(boolean inAir, float tpf);
    }
}
