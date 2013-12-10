/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import hiof.SkaalsveenEskerud.control.LodBarrelControl;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.Fire;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author Martin
 */
public class OilDrum extends Node {

    public static final String NAME = "OilDrum";
    private final AssetManager am;
    private final Vector3f position;
    private final TextureDistributor td;
    private final MaterialDistributor materialDistributor;
    private final MassFactory mf;
    private final boolean burning;
    public Geometry drumGeo;

    public OilDrum(AssetManager am, Vector3f position, TextureDistributor td, MaterialDistributor md, MassFactory mf, boolean burning) {
        name = "OilDrum";
        this.am = am;
        this.position = position;
        this.td = td;
        this.materialDistributor = md;
        this.mf = mf;
        this.burning = burning;
        drawOilDrum();
        assignMass();
    }

    private void assignMass() {
        mf.assignMass(this, 0, Vector3f.ZERO);
    }

    private void drawOilDrum() {
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(1f, 0f, 0f));

        Mesh[] oilDrums = new Mesh[]{
            new Cylinder(3, 25, 4, 11),
            new Cylinder(3, 13, 4, 11),
            new Cylinder(3, 7, 4, 11),
            new Cylinder(3, 5, 4, 11),};

        drumGeo = new Geometry(name, oilDrums[0]);

        Material oilDrumMat = materialDistributor.getAsset(MaterialDistributor.PATH_LIGHTING);
        oilDrumMat.setTexture("DiffuseMap", td.getAsset(TextureDistributor.TEXTURE_OILDRUM));
        oilDrumMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        oilDrumMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        oilDrumMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        oilDrumMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        oilDrumMat.setBoolean("UseMaterialColors", true);
        drumGeo.setMaterial(oilDrumMat);
        drumGeo.setLocalTranslation(position);
        drumGeo.setLocalRotation(deg90);
        attachChild(drumGeo);
        
        if (burning) {
            Fire fire = new Fire(am, new Vector3f(position.getX(), position.getY() + 4, position.getZ()), "fire");
            fire.setName("fire");
            attachChild(fire);
        }

    }
}
