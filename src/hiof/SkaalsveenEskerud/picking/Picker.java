/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.picking;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import hiof.SkaalsveenEskerud.control.GunControl.BetterAnimEventListener;
import java.util.ArrayList;
import java.util.Iterator;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;

/**
 *
 * @author martin
 */
public abstract class Picker implements ActionListener, BetterAnimEventListener {

    private ArrayList<Spatial> spatials;
    private boolean keyPressed;
    private MaterialDistributor md;
    private CollisionResults results;
    protected final Node rootNode;
    protected final Camera cam;
    private boolean animInProgress = false;

    public Picker(Node rootNode, Camera cam) {
        this.rootNode = rootNode;
        this.cam = cam;

        populateList();

        // 1. Reset results list.
        results = new CollisionResults();
    }

    private void populateList() {
        if (spatials == null) {
            spatials = new ArrayList<Spatial>();
        }

        spatials.add(rootNode.getChild("world"));
    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (!keyPressed && isPressed && !animInProgress) {
            pick(name);
        }
    }

    public CollisionResults getResults() {
        return results;
    }

    public abstract void onHit(Geometry geom, Vector3f contactPoint, String name);

    public abstract void onMiss(String name);

    protected void pick(String triggerKey) throws UnsupportedCollisionException {
        //reset results
        results.clear();

        // 2. Aim the ray from cam loc to cam direction.        
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());

        // 3. Collect intersections between Ray and Shootables in results list.
        Iterator<Spatial> iterator = spatials.iterator();
        while (iterator.hasNext()) {
            iterator.next().collideWith(ray, results);
        }

        if (results.size() > 0) {

            Iterator<CollisionResult> it = results.iterator();
            while (it.hasNext()) {
                String nam = it.next().getGeometry().getName();
                System.out.println("c: " + nam);
            }

            final CollisionResult closestCollision = results.getClosestCollision();
            final Vector3f contactPoint = closestCollision.getContactPoint();
            
            onHit(closestCollision.getGeometry(), contactPoint, triggerKey);

        } else {
            onMiss(triggerKey);
        }

    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        animInProgress = false;
        System.out.println("ANIM DONE");
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        
    }

    public void onStart() {
        animInProgress = true;
    }
}
