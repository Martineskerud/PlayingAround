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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author Martin
 */
public class Bench extends Node {

    private final MassFactory mf;
    private final TextureDistributor td;
    private final MaterialDistributor md;
    private final AssetManager am;
    private final Vector3f position;

    public Bench(AssetManager am, MassFactory mf, TextureDistributor td, MaterialDistributor md, Vector3f position) {
        name = "Bench";
        this.mf = mf;
        this.td = td;
        this.md = md;
        this.am = am;
        this.position = position;
        drawBench();
        assignMass();

    }

    private void assignMass() {
        mf.assignMass(this, 0, Vector3f.ZERO);
    }

    private void drawBench() {
        Node benchRotationNode = new Node("benchRotationNode");
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(90 * FastMath.DEG_TO_RAD, new Vector3f(0, 1, 0));
        benchRotationNode.setLocalRotation(deg90);
        Node tempBenchNode = new Node("tempBenchNode");
        Spatial seat = drawWall("seat", 10, 0.3f, 2, td.getAsset(TextureDistributor.TEXTURE_OLDWOOD), false, 1, 1, false, 0, Vector3f.ZERO);
        seat.setLocalTranslation(position);
        tempBenchNode.attachChild(seat);

        Spatial seatBack = seat.clone();
        Quaternion deg60 = new Quaternion();
        deg60.fromAngleAxis(FastMath.PI / 3, new Vector3f(1f, 0f, 0f));
        seatBack.setLocalRotation(deg60);
        seatBack.setLocalTranslation(new Vector3f(position.getX(), position.getY() + 2f, position.getZ() - 3f));
        tempBenchNode.attachChild(seatBack);


        Spatial leftLeg = drawWall("leftLeg", 0.3f, 2, 2, td.getAsset(TextureDistributor.TEXTURE_GREYWOOD), false, 1, 1, false, 0, Vector3f.ZERO);
        leftLeg.setLocalTranslation(new Vector3f(position.getX() - 6, position.getY() - 2.3f, position.getZ()));

        Spatial rightLeg = leftLeg.clone();
        rightLeg.setLocalTranslation(new Vector3f(position.getX() + 6, position.getY() - 2.3f, position.getZ()));
        tempBenchNode.attachChild(leftLeg);
        tempBenchNode.attachChild(rightLeg);
        benchRotationNode.attachChild(tempBenchNode);
        attachChild(benchRotationNode);
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
