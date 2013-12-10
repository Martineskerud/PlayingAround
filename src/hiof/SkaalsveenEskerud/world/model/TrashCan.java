/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

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
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.control.LodBarrelControl;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author Martin
 */
public class TrashCan extends Node {

    private final MassFactory mf;
    private final TextureDistributor td;
    private final MaterialDistributor md;
    private final AssetManager am;
    private final Vector3f position;
    private Quaternion rotationDeg;
    private Geometry trashGeo;

    public TrashCan(AssetManager am, MassFactory mf, TextureDistributor td, MaterialDistributor md, Vector3f position) {
        this.mf = mf;
        this.td = td;
        this.md = md;
        this.am = am;
        this.position = position;
        rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(90 * FastMath.DEG_TO_RAD, new Vector3f(1, 0, 0));
        drawTrashCan();
        assignMass();

    }

    public void assignMass() {
        mf.assignMass(this, 0, Vector3f.ZERO);
    }

    public void addLoDControl(Mesh[] trashMesh) {

        LodBarrelControl lbc = new LodBarrelControl(trashMesh, "trashcan");
        lbc.setDistTolerance(10f);
        //  trashMesh.addControl(lbc);
    }

    public void drawTrashCan() {
//body

        Mesh[] trashCans = new Mesh[]{
            new Cylinder(25, 25, 4.5f, 10, false, false),
            new Cylinder(13, 13, 4.5f, 10, false, false),
            new Cylinder(7, 7, 4.5f, 10, false, false),
            new Cylinder(3, 3, 4.5f, 10, false, false),};

        trashGeo = new Geometry(name, trashCans[0]);

        Material trashCanMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        trashCanMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        trashCanMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        trashCanMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        trashCanMat.setBoolean("UseMaterialColors", true);
        trashCanMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        trashCanMat.setTexture("DiffuseMap", td.getAsset(TextureDistributor.TEXTURE_TRASHCAN));
        trashGeo.setMaterial(trashCanMat);
        trashGeo.setLocalTranslation(position);
        trashGeo.setLocalRotation(rotationDeg);
        attachChild(trashGeo);

        LodBarrelControl lodControl = new LodBarrelControl(trashCans," Cylinder body of trash can");
        lodControl.setDistTolerance(10f);
        lodControl.setEnabled(true);
        trashGeo.addControl(lodControl);




        Mesh[] lids = new Mesh[]{
            new Sphere(25, 25, 4.2f),
            new Sphere(11, 11, 4.2f),
            new Sphere(7, 7, 4.2f),
            new Sphere(4, 4, 4.2f),};
        Geometry trashCanLidGeo = new Geometry("trashCanLid", lids[0]);
        Material trashCanLidMat = trashCanMat.clone();
        trashCanLidMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        trashCanLidMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        trashCanLidMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        trashCanLidMat.setBoolean("UseMaterialColors", true);
        trashCanLidMat.setTexture("DiffuseMap", td.getAsset(TextureDistributor.TEXTURE_TRASHCAN_LID));
        trashCanLidGeo.setMaterial(trashCanLidMat);

        trashCanLidGeo.setLocalTranslation(new Vector3f(position.getX(), position.getY() + 5, position.getZ()));
        trashCanLidGeo.setLocalScale(1, 0.3f, 1);
        attachChild(trashCanLidGeo);

        LodBarrelControl lodControl2 = new LodBarrelControl(lids, "sphere lid of trash can");
        lodControl2.setDistTolerance(10f);
        lodControl2.setEnabled(true);
        trashCanLidGeo.addControl(lodControl2);


        /*
        
         * 
         * 
         Sphere lid = new Sphere(11, 11, 4.2f);
         Geometry trashCanLidGeo = new Geometry("trashCanLid", lid);
         Material trashCanLidMat = trashCanMat.clone();
         trashCanLidMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
         trashCanLidMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
         trashCanLidMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
         trashCanLidMat.setBoolean("UseMaterialColors", true);
         trashCanLidMat.setTexture("DiffuseMap", td.getAsset(TextureDistributor.TEXTURE_TRASHCAN_LID));
         trashCanLidGeo.setMaterial(trashCanLidMat);

         trashCanLidGeo.setLocalTranslation(new Vector3f(position.getX(), position.getY() + 5, position.getZ()));
         trashCanLidGeo.setLocalScale(1, 0.3f, 1);
         attachChild(trashCanLidGeo);
         */



    }

    public Spatial drawWall(String name, float x, float y, float z, Texture tex, boolean repeat, int repx, int repy, boolean transparent, int deg, Vector3f rotationAxis) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);


        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);

        if (repeat) {
            tex.setWrap(Texture.WrapMode.Repeat);
            Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            wallMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
            wallMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
            wallMat.setColor("Specular", ColorRGBA.White.mult(0.65f));

            wallMat.setBoolean("UseMaterialColors", true);
            wallMat.setTexture("DiffuseMap", tex);
            if (transparent == true) {
                wallMat.setBoolean("UseAlpha", true);
                wallGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
            }


            wall.scaleTextureCoordinates(new Vector2f(repx, repy));
            wallGeo.setMaterial(wallMat);
            wallGeo.setLocalRotation(rotationDeg);
            return wallGeo;
        } else {
            Material wallMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
            wallGeo.setMaterial(wallMat);
            wallMat.setTexture("ColorMap", tex);
            if (transparent == true) {
                //this doesnt look as "white" when transparent.
                wallMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                wallGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
            }

            return wallGeo;
        }
    }
}
