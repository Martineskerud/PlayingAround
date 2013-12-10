package hiof.SkaalsveenEskerud.control;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.AreaUtils;

public class LodBarrelControl extends AbstractControl {

    private float trianglesPerPixel = 1f;
    private float distanceThreshold = 1f;
    private float prevDistance = 0f;
    private int prevLevel = 0;
    private int numLevels;
    private int[] numTris;
    private Mesh[] cylinders;
    Vector3f fixedCamLocation = null;
    private final String name;

    public LodBarrelControl(Mesh[] cylinders, String name) {
        this.cylinders = cylinders;
        this.name = name;
    }

    public float getDistTolerance() {
        return distanceThreshold;
    }

    public void setDistTolerance(float distTolerance) {
        this.distanceThreshold = distTolerance;
    }

    public float getTrisPerPixel() {
        return trianglesPerPixel;
    }

    public void setTrisPerPixel(float trisPerPixel) {
        this.trianglesPerPixel = trisPerPixel;
    }

    @Override
    public void setSpatial(Spatial spatial) {

        super.setSpatial(spatial);
        numLevels = cylinders.length;
        numTris = new int[numLevels];
        for (int i = numLevels - 1; i >= 0; i--) {
            numTris[i] = cylinders[i].getTriangleCount();
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        float newDistance;
        int level;
        BoundingVolume bv = spatial.getWorldBound();
        Camera cam = vp.getCamera();
        float atanNH = FastMath.atan(cam.getFrustumNear() * cam.getFrustumTop());
        float ratio = (FastMath.PI / (8f * atanNH));
        newDistance = bv.distanceTo(vp.getCamera().getLocation()) / ratio;


        if (Math.abs(newDistance - prevDistance) <= distanceThreshold) {
            level = prevLevel; // we haven't moved relative to the model, send the old measurement back.
        } else if (prevDistance > newDistance && prevLevel == 0) {
            level = prevLevel; // we're already at the lowest setting and we just got closer to the model, no need to keep trying.
        } else if (prevDistance < newDistance && prevLevel == numLevels - 1) {
            level = prevLevel; // we're already at the highest setting and we just got further from the model, no need to keep trying.
        } else {
            prevDistance = newDistance;
            // estimate area of polygon via bounding volume
            float area = AreaUtils.calcScreenArea(bv, prevDistance, cam.getWidth());
            float trisToDraw = area * trianglesPerPixel;
            level = numLevels - 1;
            for (int i = numLevels; --i >= 0;) {
                if (trisToDraw - numTris[i] < 0) {
                    break;
                }
                level = i;
            }
        }

        if(prevLevel!=level){
            System.out.println("This is the Level Of Detail control, currently drawing " + cylinders[level].getTriangleCount() +" triangles on the " + name);
        }

        prevLevel = level;
        ((Geometry) spatial).setMesh(cylinders[level]);
    }
}
