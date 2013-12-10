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
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;
import hiof.SkaalsveenEskerud.world.model.util.RandomInt;
import java.util.Random;

/**
 *
 * @author martin
 */
public class Cargo extends Node {

    private final AssetManager am;
    private MaterialDistributor md;
    private MassFactory mf;
    private Vector3f position;
    private float rotation;
    private Texture tex;
    private Texture texSide;
    private TextureDistributor td;
    private String name;
    private final boolean usingRotation;

    public Cargo(String name, AssetManager am, MassFactory mf, TextureDistributor td, boolean closed, Vector3f position, Random rand) {

        this.am = am;
        md = new MaterialDistributor(am);
        this.mf = mf;
        this.td = td;
        this.position = position;
        this.name = name;
        usingRotation = false;
        int randomNum = RandomInt.rangedRandomInt(1, 5, rand);
        setColor(randomNum);
        drawHorizontal();
        drawVertical();
        if (closed) {
            drawShortEnd();
        }
        assignMass();
    }

    //Constructor with rotation
    //@param rotation is pi/rotation. 1=1pi rotation in rads. 45deg=4, 90deg =2
    public Cargo(String name, AssetManager am, MassFactory mf, TextureDistributor td, boolean closed, Vector3f position, Random rand, float rotation) {
        this.am = am;
        md = new MaterialDistributor(am);
        this.mf = mf;
        this.td = td;
        this.position = position;
        this.name = name;
        this.rotation = rotation;
        usingRotation = true;

        int randomNum = RandomInt.rangedRandomInt(1, 5, rand);
        setColor(randomNum);
        drawHorizontal();
        drawVertical();
        if (closed) {
            drawShortEnd();
        }
        assignMass();
    }

    private void assignMass() {
        mf.assignMass(this, 0, Vector3f.ZERO);
    }

    private void setColor(int inputColor) {
        if (inputColor == (1)) {
            tex = td.getAsset(TextureDistributor.TEXTURE_CARGO_1);
            texSide = td.getAsset(TextureDistributor.TEXTURE_CARGO_1_SIDE);
        } else if (inputColor == (2)) {
            tex = td.getAsset(TextureDistributor.TEXTURE_CARGO_2);
            texSide = td.getAsset(TextureDistributor.TEXTURE_CARGO_2_SIDE);
        } else if (inputColor == 3) {
            tex = td.getAsset(TextureDistributor.TEXTURE_CARGO_4);
            texSide = td.getAsset(TextureDistributor.TEXTURE_CARGO_4_SIDE);
        } else if (inputColor == 4) {
            tex = td.getAsset(TextureDistributor.TEXTURE_CARGO_3);
            texSide = td.getAsset(TextureDistributor.TEXTURE_CARGO_3_SIDE);
        } else {
            tex = td.getAsset(TextureDistributor.TEXTURE_CARGO_1);
            texSide = td.getAsset(TextureDistributor.TEXTURE_CARGO_1_SIDE);
        }

    }

    private void drawShortEnd() {
        Spatial containerWall3 = drawWall("container1_2", 0.5f, 9f, 10f, texSide, false, 1, 1, false, 0, Vector3f.ZERO);
        containerWall3.setLocalTranslation(position);

        Spatial containerWall2 = containerWall3.clone();
        containerWall2.setLocalTranslation(new Vector3f(position.getX() + 60, position.getY(), position.getZ()));
        if (usingRotation) {
            Node shortendRotationPoint = new Node("shortendRotationPoint");

            Quaternion rot = new Quaternion();
            rot.fromAngleAxis(FastMath.PI / rotation, new Vector3f(0f, 1f, 0f));

            shortendRotationPoint.attachChild(containerWall3);
            shortendRotationPoint.attachChild(containerWall2);
            shortendRotationPoint.setLocalRotation(rot);
            this.attachChild(shortendRotationPoint);
        } else {
            attachChild(containerWall3);
            attachChild(containerWall2);
        }
    }

    private void drawVertical() {

        Spatial containerWall1 = drawWall("container1_1", 30f, 10f, 0.5f, tex, false, 1, 1, false, 0, Vector3f.ZERO);
        containerWall1.setLocalTranslation(new Vector3f(position.getX() + 30, position.getY(), position.getZ() - 10.5f));



        Spatial containerWall2 = containerWall1.clone();
        containerWall2.setLocalTranslation(new Vector3f(position.getX() + 30, position.getY(), position.getZ() + 10.5f));
        if (usingRotation) {
            Node verticalRotationPoint = new Node("verticalRotationPoint");

            Quaternion rot = new Quaternion();
            rot.fromAngleAxis(FastMath.PI / rotation, new Vector3f(0f, 1f, 0f));

            verticalRotationPoint.attachChild(containerWall1);
            verticalRotationPoint.attachChild(containerWall2);
            verticalRotationPoint.setLocalRotation(rot);
            this.attachChild(verticalRotationPoint);
        } else {
            attachChild(containerWall1);
            attachChild(containerWall2);
        }
    }

    private void drawHorizontal() {
        //floor
        Spatial containerWall3 = drawWall("container1_3", 30f, 0.5f, 010f, tex, false, 1, 1, false, 0, Vector3f.ZERO);
        containerWall3.setLocalTranslation(position.getX() + 30, position.getY() - 9.5f, position.getZ());
        //roof
        Spatial containerWall4 = containerWall3.clone();
        containerWall4.setLocalTranslation(new Vector3f(position.getX() + 30, position.getY() + 9.5f, position.getZ()));
        if (usingRotation) {
            Node horizontalRotationNode = new Node("horizontalRotationNode");

            Quaternion rot = new Quaternion();
            rot.fromAngleAxis(FastMath.PI / rotation, new Vector3f(0f, 1f, 0f));

            // containerWall3.setLocalRotation(rot);
            //containerWall2.setLocalRotation(rot);
            horizontalRotationNode.attachChild(containerWall3);
            horizontalRotationNode.attachChild(containerWall4);
            horizontalRotationNode.setLocalRotation(rot);
            this.attachChild(horizontalRotationNode);
        } else {
            attachChild(containerWall3);
            attachChild(containerWall4);
        }
    }

    public Spatial drawWall(String name, float x, float y, float z, Texture tex, boolean repeat, int repx, int repy, boolean transparent, int deg, Vector3f rotationAxis) {
        Box wall = new Box(Vector3f.ZERO, x, y, z);
        Geometry wallGeo = new Geometry(name, wall);


        Quaternion rotationDeg = new Quaternion();
        rotationDeg.fromAngleAxis(deg * FastMath.DEG_TO_RAD, rotationAxis);

        if (repeat) {
            tex.setWrap(Texture.WrapMode.Repeat);
            Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            
            wallMat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
            wallMat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
            wallMat.setColor("Specular", ColorRGBA.White.mult(0.65f));

            wallMat.setBoolean("UseMaterialColors", true);
            //wallMat.setTexture("DiffuseMap", tex);
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
                wallMat.setBoolean("UseAlpha", true);
                wallGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
            }

            return wallGeo;
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
