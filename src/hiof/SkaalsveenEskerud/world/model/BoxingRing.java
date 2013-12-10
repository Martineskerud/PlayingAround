/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import java.util.ArrayList;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;

/**
 *
 * @author martin
 */
public class BoxingRing {

    private AssetManager am;
    private ArrayList<Spatial> boxingRingParts = null;
    private MaterialDistributor md;

    public BoxingRing(AssetManager am, MaterialDistributor md) {
        this.am = am;
        this.boxingRingParts = new ArrayList<Spatial>();
        this.md = md;

    }

    public ArrayList<Spatial> createBoxingRingSpatials() {

        boxingRingParts.add(drawRing());
        boxingRingParts.add(drawStairs());
        boxingRingParts.add(drawRingCorners());
        boxingRingParts.add(drawRingRopes());


        return boxingRingParts;
    }

    private Spatial drawRing() {
        //The ring
        Box floor = new Box(Vector3f.ZERO, 15f, 4f, 15f);
        Geometry boxingRing = new Geometry("Box", floor);

        Material ringMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        ringMat.setFloat("Shininess", 120);
        ringMat.setBoolean("UseMaterialColors", true);
        ringMat.setColor("Ambient", ColorRGBA.Blue.mult(0.3f));
        ringMat.setColor("Diffuse", ColorRGBA.Blue.mult(0.35f));
        ringMat.setColor("Specular", ColorRGBA.Blue.mult(0.6f));

        boxingRing.setMaterial(ringMat);

        return boxingRing;
    }

    private Spatial drawStairs() {
        //The stairs
        Material stepMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        stepMat.setFloat("Shininess", 12);
        stepMat.setBoolean("UseMaterialColors", true);
        stepMat.setColor("Ambient", ColorRGBA.Gray.mult(0.3f));
        stepMat.setColor("Diffuse", ColorRGBA.Gray.mult(0.35f));
        stepMat.setColor("Specular", ColorRGBA.Gray.mult(0.6f));
        Node stairStepNode = new Node("stairSteps");
        for (int i = 0; i < 4; i++) {
            Box stairStep = new Box(Vector3f.ZERO, 1f, 4f - (i), 4f);
            Geometry stairStepGeo = new Geometry("Box", stairStep);
            stairStepGeo.setLocalTranslation(new Vector3f(16 + i * 2, 0f + (i * -1), 11));
            stairStepGeo.setMaterial(stepMat);
            stairStepNode.attachChild(stairStepGeo);
        }
        return stairStepNode;
    }

    private Spatial drawRingCorners() {
        Cylinder corner1 = new Cylinder(13, 13, 1, 7, true);
        Geometry corner1Geo = new Geometry("Cylinder", corner1);
        Geometry corner2Geo = new Geometry("Cylider", corner1);
        Geometry corner3Geo = new Geometry("Cylinder", corner1);
        Geometry corner4Geo = new Geometry("Cylinder", corner1);
        Material corner1Mat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        corner1Mat.setFloat("Shininess", 12);
        corner1Mat.setBoolean("UseMaterialColors", true);
        corner1Mat.setColor("Ambient", ColorRGBA.Red.mult(0.3f));
        corner1Mat.setColor("Diffuse", ColorRGBA.Red.mult(0.35f));
        corner1Mat.setColor("Specular", ColorRGBA.Red.mult(0.6f));

        corner1Geo.setMaterial(corner1Mat);
        corner2Geo.setMaterial(corner1Mat);
        corner3Geo.setMaterial(corner1Mat);
        corner4Geo.setMaterial(corner1Mat);
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(1f, 0f, 0f));
        Node cornersNode = new Node("corners");
        corner1Geo.setLocalRotation(deg90);
        corner1Geo.setLocalTranslation(new Vector3f(14f, 7f, 14f));
        corner2Geo.setLocalRotation(deg90);
        corner2Geo.setLocalTranslation(new Vector3f(14f, 7f, -14f));
        corner3Geo.setLocalRotation(deg90);
        corner3Geo.setLocalTranslation(new Vector3f(-14f, 7f, -14f));
        corner4Geo.setLocalRotation(deg90);
        corner4Geo.setLocalTranslation(new Vector3f(-14f, 7f, 14f));
        cornersNode.attachChild(corner1Geo);
        cornersNode.attachChild(corner2Geo);
        cornersNode.attachChild(corner3Geo);
        cornersNode.attachChild(corner4Geo);
        return cornersNode;
    }

    private Spatial drawRingRopes() {
        //TODO: Refactor this shit into a loop
        Cylinder rope1 = new Cylinder(5, 5, 0.15f, 28, true);
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));
        Node ropesNode = new Node("ropes");

        //left
        for (int i = 0; i < 3; i++) {

            Geometry ropeGeo = new Geometry("Cylinder", rope1);

            Material ropeMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            ropeMat.setFloat("Shininess", 12);
            ropeMat.setBoolean("UseMaterialColors", true);
            ropeMat.setColor("Ambient", ColorRGBA.Brown.mult(0.3f));
            ropeMat.setColor("Diffuse", ColorRGBA.Brown.mult(0.35f));
            ropeMat.setColor("Specular", ColorRGBA.Brown.mult(0.6f));

            ropeGeo.setMaterial(ropeMat);
            ropeGeo.setLocalTranslation(new Vector3f(-14f, 9f - (i * 2), 0f));
            ropesNode.attachChild(ropeGeo);
        }
        //right
        for (int i = 0; i < 3; i++) {

            Geometry ropeGeo = new Geometry("Cylinder", rope1);

            Material ropeMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            ropeMat.setFloat("Shininess", 12);
            ropeMat.setBoolean("UseMaterialColors", true);
            ropeMat.setColor("Ambient", ColorRGBA.Brown.mult(0.3f));
            ropeMat.setColor("Diffuse", ColorRGBA.Brown.mult(0.35f));
            ropeMat.setColor("Specular", ColorRGBA.Brown.mult(0.6f));
            ropeGeo.setMaterial(ropeMat);
            ropeGeo.setLocalTranslation(new Vector3f(14f, 9f - (i * 2), 0f));
            ropesNode.attachChild(ropeGeo);

        }
//Front
        for (int i = 0; i < 3; i++) {
            Geometry ropeGeo = new Geometry("Cylinder", rope1);

            Material ropeMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            ropeMat.setFloat("Shininess", 12);
            ropeMat.setBoolean("UseMaterialColors", true);
            ropeMat.setColor("Ambient", ColorRGBA.Brown.mult(0.3f));
            ropeMat.setColor("Diffuse", ColorRGBA.Brown.mult(0.35f));
            ropeMat.setColor("Specular", ColorRGBA.Brown.mult(0.6f));

            ropeGeo.setMaterial(ropeMat);
            ropeGeo.setLocalRotation(deg90);

            ropeGeo.setLocalTranslation(new Vector3f(0f, 9f - (i * 2), 14f));
            ropesNode.attachChild(ropeGeo);

        }
        //BACK
        for (int i = 0; i < 3; i++) {

            Geometry ropeGeo = new Geometry("Cylinder", rope1);

            Material ropeMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
            ropeMat.setFloat("Shininess", 12);
            ropeMat.setBoolean("UseMaterialColors", true);
            ropeMat.setColor("Ambient", ColorRGBA.Brown.mult(0.3f));
            ropeMat.setColor("Diffuse", ColorRGBA.Brown.mult(0.35f));
            ropeMat.setColor("Specular", ColorRGBA.Brown.mult(0.6f));

            ropeGeo.setMaterial(ropeMat);
            ropeGeo.setLocalRotation(deg90);

            ropeGeo.setLocalTranslation(new Vector3f(0f, 9f - (i * 2), -14f));
            ropesNode.attachChild(ropeGeo);
        }

        return ropesNode;

    }
}
