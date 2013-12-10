/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import hiof.SkaalsveenEskerud.character.Oto;
import hiof.SkaalsveenEskerud.control.PortalControl;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;

/**
 *
 * @author martin
 */
public class Portal extends Geometry {

    public static final String PORTAL_A = "PortalA";
    public static final String PORTAL_B = "PortalB";
    private final AssetManager assetManager;
    private Vector3f position;
    private Portal other;

    public Portal(String name, AssetManager assetManager, final Oto oto, BulletAppState bas, MassFactory mf) {
        super(name, new Sphere(32, 32, 24f));
        this.position = getPortalPosition(name);
        this.assetManager = assetManager;

        drawPortal();
        mf.assignMass(this, 0, Vector3f.ZERO, true);

        PortalControl pc = new PortalControl(bas, name) {
            @Override
            protected void sendPlayerToPosition(Vector3f portalPosition) {


                oto.setLocation(portalPosition);

            }

            @Override
            protected void setRigidBody(boolean visible) {
                if (other != null) {
                    other.setEnabled(visible);
                }

            }
        };


        bas.getPhysicsSpace().addCollisionListener(pc);
        addControl(pc.tc);
    }

    private void drawPortal() {

        Material mat = new Material(assetManager, MaterialDistributor.PATH_MY_TEST_SHADER);
        mat.setInt("Time", (int) (System.currentTimeMillis() / 1000));
        setMaterial(mat);
        setLocalTranslation(position);

    }

    public static Vector3f getPortalPosition(String name) {

        Vector3f pos = Vector3f.ZERO;

        if (PORTAL_A.equals(name)) {
            pos = new Vector3f(-145, 10, 250);
        } else if (PORTAL_B.equals(name)) {
            pos = new Vector3f(668, 10, 270);
        }

        return pos;

    }

    public void collision(PhysicsCollisionEvent event) {
    }

    public void connectTo(Portal other) {
        this.other = other;
    }

    private void setEnabled(boolean visible) {
        getControl(RigidBodyControl.class).setEnabled(visible);
    }
}
