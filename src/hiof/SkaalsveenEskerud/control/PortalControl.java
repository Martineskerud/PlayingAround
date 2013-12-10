/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import hiof.SkaalsveenEskerud.world.Portal;
import java.util.HashMap;

/*
 * @author martin
 */
public abstract class PortalControl extends RigidBodyControl implements PhysicsCollisionListener {

    private static long lastTeleported = 0;
    private final HashMap<String, String> portalMap;
    public static final long COOLDOWN_TIME = 3000;
    private final String portalName;
    private boolean disabled = false;
    public final TimeoutControl tc;

    public PortalControl(BulletAppState bas, String portalName) {
        this.portalName = portalName;

        portalMap = new HashMap<String, String>();
        portalMap.put(Portal.PORTAL_A, Portal.PORTAL_B);
        portalMap.put(Portal.PORTAL_B, Portal.PORTAL_A);
        
        tc = new TimeoutControl();

    }

    public void collision(PhysicsCollisionEvent event) {

        if (event != null) {


            Spatial a = event.getNodeA();
            Spatial b = event.getNodeB();


            if (b != null && a != null) {

                final String nameA = a.getName();
                final String nameB = b.getName();

                final boolean isPortal = portalName.equals(nameB);
                final boolean isPlayer = "Me".equals(nameA);

                if (isPortal && isPlayer) {

                    System.out.println(nameB + " : " + nameA);

                    if (timeIsRight()) {
                        lastTeleported = System.currentTimeMillis();

                        System.out.println("A: " + nameA + "\t B: " + nameB);
                        disabled = true;
                        setRigidBody(false);
                        sendPlayerToPosition(Portal.getPortalPosition(portalMap.get(nameB)));

                    } else {

                        System.out.println("!A: " + nameA + "\t B: " + nameB);

                    }
                }
            }
        }


    }

    protected boolean timeIsRight() {

        final long now = System.currentTimeMillis();
        final long time = now - lastTeleported;

        return (time > COOLDOWN_TIME);
    }

    protected abstract void sendPlayerToPosition(Vector3f portalPosition);

    protected abstract void setRigidBody(boolean visible);

    class TimeoutControl extends AbstractControl {

        @Override
        protected void controlUpdate(float tpf) {

            if (disabled) {
                if (timeIsRight()) {
                    setRigidBody(true);

                }
            }

        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }
    }
}
