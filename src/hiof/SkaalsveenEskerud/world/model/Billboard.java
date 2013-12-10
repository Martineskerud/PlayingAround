/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
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
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

public class Billboard extends Node {

    private final AssetManager am;
    private MaterialDistributor md;
    private MassFactory mf;
    private TextureDistributor td;
    private Vector3f position;
    private String input;

    public Billboard(AssetManager am, MassFactory mf, TextureDistributor td, Vector3f position, int rotation, String input) {
        name = "Billboard";
        this.am = am;
        md = new MaterialDistributor(am);
        this.mf = mf;
        this.td = td;
        this.input = input;
        this.position = position;
        drawBillboard(rotation, input);
        assignMass();
    }
    //non rotation constructor

    public Billboard(AssetManager am, MassFactory mf, TextureDistributor td, Vector3f position, String input) {
        this.am = am;
        md = new MaterialDistributor(am);
        this.mf = mf;
        this.td = td;
        this.input = input;
        this.position = position;
        drawBillboard();
        assignMass();
    }

    private void assignMass() {
        mf.assignMass(this, 0, Vector3f.ZERO);
    }
    //rotation method

    private void drawBillboard(int piParts, String str) {

        Node billBoardRotationNode = new Node("billBoardRotationNode");
        Quaternion deg45 = new Quaternion();
        deg45.fromAngleAxis(FastMath.PI / piParts, new Vector3f(0f, 1f, 0f));
        Spatial mainDisplay;
        if ("hiof".equals(str)) {
            mainDisplay = drawWall("billboardMain", 0.1f, 30, 60, td.getAsset(TextureDistributor.TEXTURE_HIOF), false, 1, 1, false, 0, Vector3f.ZERO);
        } else {
            mainDisplay = drawWall("billboardMain", 0.1f, 30, 60, td.getAsset(TextureDistributor.TEXTURE_ENIGMA), false, 1, 1, false, 0, Vector3f.ZERO);

        }

        mainDisplay.setLocalTranslation(new Vector3f(770, 50, -40));
        billBoardRotationNode.attachChild(mainDisplay);

        Spatial deck = drawWall("transparentDeck", 5f, 1f, 60, td.getAsset(TextureDistributor.TEXTURE_TRANSPARENT_STEPS), false, 1, 1, true, 0, Vector3f.ZERO);
        deck.setLocalTranslation(new Vector3f(765, 25, -40));
        billBoardRotationNode.attachChild(deck);

        Cylinder leg = new Cylinder(15, 15, 6, 30, true);
        Geometry billboardLeg = new Geometry("weapon1", leg);
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(1f, 0f, 0f));
        Material billboardLegMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
        billboardLegMat.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_SILVER));
        billboardLeg.setMaterial(billboardLegMat);
        billboardLeg.setLocalRotation(deg90);
        billboardLeg.setLocalTranslation(new Vector3f(765, 9, -40));
        billBoardRotationNode.attachChild(billboardLeg);
        billBoardRotationNode.setLocalRotation(deg45);
        billBoardRotationNode.setLocalTranslation(position);
        attachChild(billBoardRotationNode);
    }

    private void drawBillboard() {
        Spatial mainDisplay = drawWall("billboardMain", 0.1f, 30, 60, td.getAsset(TextureDistributor.TEXTURE_ENIGMA), false, 1, 1, false, 0, Vector3f.ZERO);

        mainDisplay.setLocalTranslation(new Vector3f(770, 50, -40));
        attachChild(mainDisplay);

        Spatial deck = drawWall("transparentDeck", 5f, 1f, 60, td.getAsset(TextureDistributor.TEXTURE_TRANSPARENT_STEPS), false, 1, 1, true, 0, Vector3f.ZERO);
        deck.setLocalTranslation(new Vector3f(765, 25, -40));
        attachChild(deck);

        Cylinder leg = new Cylinder(15, 15, 6, 30, true);
        Geometry billboardLeg = new Geometry("weapon1", leg);
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(1f, 0f, 0f));
        Material billboardLegMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
        billboardLegMat.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_SILVER));
        billboardLeg.setMaterial(billboardLegMat);
        billboardLeg.setLocalRotation(deg90);
        billboardLegMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        billboardLeg.setLocalTranslation(new Vector3f(765, 9, -40));
        setLocalTranslation(position);
        attachChild(billboardLeg);
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
                wallMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
                wallGeo.setQueueBucket(RenderQueue.Bucket.Transparent);
            }

            return wallGeo;
        }
    }
}
