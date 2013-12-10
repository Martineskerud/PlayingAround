/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.water.SimpleWaterProcessor;
import hiof.SkaalsveenEskerud.Main.MainContainer;
import java.util.ArrayList;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author Martin
 */
public class Fountain {

    private AssetManager am;
    private ArrayList<Spatial> fountainParts = null;
    private final ViewPort viewPort;
    private SimpleWaterProcessor waterProcessor;
    private MaterialDistributor md;
    private Spatial waterPlane;
    private TextureDistributor td;
    private MainContainer mc;

    public Fountain(AssetManager assetManager, ViewPort vp, MaterialDistributor md, MainContainer mc) {
        this.am = assetManager;
        this.fountainParts = new ArrayList<Spatial>();
        this.viewPort = vp;
        this.md = md;
        this.mc = mc;
    }

    public ArrayList<Spatial> createFountainSpatials(Node rootNode) {

        Shack shack = new Shack(am, md, td, mc);

        Spatial poolWallBack = shack.drawWall("poolWallBack", 40f, 10f, 1f, "Textures/bluetiles.jpg", true, 2, 1, false);
        poolWallBack.setLocalTranslation(340, 7, 101);
        fountainParts.add(poolWallBack);

        Spatial poolWallFront = shack.drawWall("poolWallFront", 40f, 10f, 1f, "Textures/bluetiles.jpg", true, 2, 1, false);
        poolWallFront.setLocalTranslation(340, 7, -61);
        fountainParts.add(poolWallFront);

        Spatial poolWallLeft = shack.drawWall("poolWallLeft", 80f, 10f, 1f, "Textures/bluetiles.jpg", true, 2, 1, false, 90, new Vector3f(0, 1, 0));
        poolWallLeft.setLocalTranslation(299, 7, 20);
        fountainParts.add(poolWallLeft);
        Spatial poolWallRight = shack.drawWall("poolWallRight", 80f, 10f, 1f, "Textures/bluetiles.jpg", true, 2, 1, false, 90, new Vector3f(0, 1, 0));
        poolWallRight.setLocalTranslation(381, 7, 20);
        fountainParts.add(poolWallRight);
        return fountainParts;
    }

    public Spatial createFountainSpatialsV2(Node parentNode) {

        parentNode.attachChild(drawWall(new Vector3f(30, 30, 20), 10f, 4f, 1f));
        Spatial m = drawWall(new Vector3f(30, 30, 40), 10f, 4f, 1f);
        parentNode.attachChild(m);
        return m;
    }

    private Spatial drawWall(Vector3f pos, float x, float y, float z) {

        Box wall = new Box(pos, x, y, z);
        Geometry wallGeo = new Geometry("Box", wall);

        Texture wallTex = this.am.loadTexture(MaterialDistributor.PATH_LIGHTING);
        wallTex.setWrap(Texture.WrapMode.Repeat);
        Material wallMat = md.getAsset(MaterialDistributor.PATH_LIGHTING);

        wallMat.setBoolean("UseMaterialColors", true);
        wallMat.setTexture("DiffuseMap", wallTex);

        wall.scaleTextureCoordinates(new Vector2f(4, 4));
        wallGeo.setMaterial(wallMat);
        return wallGeo;

    }

    public void drawWater(Node parentNode, Node reflectionNode) {

        //create processor
        waterProcessor = new SimpleWaterProcessor(am);
        waterProcessor.setReflectionScene(reflectionNode);
        waterProcessor.setDebug(true);

        // we set wave properties
        waterProcessor.setWaterDepth(5f);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

        viewPort.addProcessor(waterProcessor);

        waterProcessor.setLightPosition(new Vector3f(0.55f, -0.82f, 0.15f));

        //create water quad
        //waterPlane = waterProcessor.createWaterGeometry(100, 100);
        waterPlane = (Spatial) am.loadModel("Models/WaterTest/WaterTest.mesh.xml");
        waterPlane.setMaterial(waterProcessor.getMaterial());

        waterPlane.setLocalTranslation(30, 30f, 30);
        waterPlane.setLocalScale(10f, 5f, 10f);

        parentNode.attachChild(waterPlane);
        /*
         SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(am);
         waterProcessor.setReflectionScene(parentNode);

         // we set the water plane
         Vector3f waterLocation = new Vector3f(0, -6, 0);
         waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Z)));
         viewPort.addProcessor(waterProcessor);

         // we set wave properties
         waterProcessor.setWaterDepth(1f);         // transparency of water
         waterProcessor.setDistortionScale(0.05f); // strength of waves
         waterProcessor.setWaveSpeed(0.05f);       // speed of waves

         Spatial poolWallLeft = shack.drawWall(80f, 10f, 1f, "Textures/bluetiles.jpg", true, 2, 1, false, 90, new Vector3f(0, 1, 0));
         poolWallLeft.setLocalTranslation(299, 7, 20);
         fountainNode.attachChild(poolWallLeft);

         Spatial poolWallRight = shack.drawWall(80f, 10f, 1f, "Textures/bluetiles.jpg", true, 2, 1, false, 90, new Vector3f(0, 1, 0));
         poolWallRight.setLocalTranslation(381, 7, 20);
         fountainNode.attachChild(poolWallRight);

         return fountainNode;
         * */
    }
}
