/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;

/**
 *
 * @author Martin
 */
public class InvisibleWall extends Node {

    private final AssetManager am;
    private final MassFactory mf;
    private final MaterialDistributor md;

    public InvisibleWall(AssetManager am, MassFactory mf, MaterialDistributor md) {
        this.am = am;
        this.mf = mf;
        this.md = md;
        drawInvisibleWall();
        //removing the geometry so only the rigid bodies are left to create a forcefield keeping players from falling outside of the map.
        detachAllChildren();
    }

    public void assignMass(Spatial inputSpatial) {
        mf.assignMass(inputSpatial, 0, Vector3f.ZERO);
    }

    public void drawInvisibleWall() {

        Spatial leftInvisibleWall = drawWall("leftInvisibleWall", 500, 300, 3, new Vector3f(350, 0, -153));
        assignMass(leftInvisibleWall);
        attachChild(leftInvisibleWall);

        Spatial topInvisibleWall = drawWall("topInvisibleWall", 3, 500, 300, new Vector3f(853, -300, 150));
        assignMass(topInvisibleWall);
        attachChild(topInvisibleWall);

        Spatial rightInvisibleWall = drawWall("rightInvisibleWall", 500, 125, 3, new Vector3f(350, 100, 453));
        assignMass(rightInvisibleWall);
        attachChild(rightInvisibleWall);

        Spatial cornerInvisibleWall1 = drawWall("cornerInvisibleWall1", 3, 125, 50, new Vector3f(-147, 100, 506));
        assignMass(cornerInvisibleWall1);
        attachChild(cornerInvisibleWall1);

        Spatial cornerInvisibleWall2 = drawWall("cornerInvisibleWall2", 100, 500, 3, new Vector3f(-250, -300, 503));
        assignMass(cornerInvisibleWall2);
        attachChild(cornerInvisibleWall2);

        Spatial cornerInvisibleWall3 = drawWall("cornerInvisibleWall3", 3, 500, 100, new Vector3f(-353, -300, 453));
        assignMass(cornerInvisibleWall3);
        attachChild(cornerInvisibleWall3);

        Spatial cornerInvisibleWall4 = drawWall("cornerInvisibleWall4", 100, 500, 3, new Vector3f(-250, -300, 383));
        assignMass(cornerInvisibleWall4);
        attachChild(cornerInvisibleWall4);

        Spatial bottomInvisibleWall = drawWall("bottomInvisibleWall", 3, 500, 270, new Vector3f(-153, -300, 110));
        assignMass(bottomInvisibleWall);
        attachChild(bottomInvisibleWall);

        Spatial InvisibleRoof = drawWall("InvisibleRoof", 1000, 50, 1000, new Vector3f(400, 300, 400));
        assignMass(InvisibleRoof);
        attachChild(InvisibleRoof);
    }

    //vanilla
    public Spatial drawWall(String name, float x, float y, float z, Vector3f pos) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);

        Material wallMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
        wallGeo.setMaterial(wallMat);
        wallGeo.setLocalTranslation(pos);
        return wallGeo;
    }
}
