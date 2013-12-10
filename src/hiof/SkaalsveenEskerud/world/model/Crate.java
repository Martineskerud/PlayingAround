/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class Crate extends Node {

    private final AssetManager am;
    private MaterialDistributor md;
    private MassFactory mf;
    private Vector3f position;
    private float rotation;
    private TextureDistributor td;

    public Crate(AssetManager am, MassFactory mf, TextureDistributor td, boolean closed, Vector3f position, Random rand) {
        name = "crate";
        this.am = am;
        md = new MaterialDistributor(am);
        this.mf = mf;
        this.td = td;
        this.position = position;
        int randomNum = hiof.SkaalsveenEskerud.world.model.util.RandomInt.rangedRandomInt(1, 5, rand);
        drawCrate(RF_BOUND, RF_BOUND, RF_BOUND, randomNum, position);
        assignMass();
    }

    public Crate(float x, float y, float z, AssetManager am, MassFactory mf, TextureDistributor td, Vector3f position) {
        this.am = am;
        md = new MaterialDistributor(am);
        this.mf = mf;
        this.td = td;
        this.position = position;
        drawCrate(x, y, z);
        assignMass();
    }

    public void drawCrate(float x, float y, float z) {
        Box crate = new Box(Vector3f.ZERO, x, y, z);
        Geometry crateGeo = new Geometry("Box", crate);
        Material crateMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        crateMat.setBoolean("UseMaterialColors", true);
        crateMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        crateMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        crateMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        Texture tex = td.getAsset(TextureDistributor.TEXTURE_GREYWOOD);
        crateMat.setTexture("DiffuseMap", tex);
        crateGeo.setMaterial(crateMat);
        setLocalTranslation(position);
        this.attachChild(crateGeo);
    }

    public void drawCrate(int x, int y, int z, int deg, Vector3f rotationAxis) {


        Quaternion rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);
        Box crate = new Box(Vector3f.ZERO, x, y, z);
        Geometry crateGeo = new Geometry("Box", crate);
        Material crateMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        Texture crateTex = td.getAsset(TextureDistributor.TEXTURE_GREYWOOD);
        crateMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        crateMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        crateMat.setColor("Specular", ColorRGBA.White.mult(0.65f));
        crateMat.setBoolean("UseMaterialColors", true);
        crateMat.setTexture("DiffuseMap", crateTex);
        crateGeo.setMaterial(crateMat);
        setLocalTranslation(position);
        crateGeo.setLocalRotation(rotationDeg);
        this.attachChild(crateGeo);
    }

    private void assignMass() {
        this.mf.assignMass(this, 0, Vector3f.ZERO);
    }
}
