/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author martin
 */
public class Meteor extends Node {
    public final static String NAME ="myMeteor";
    private final AssetManager assetManager;
    private final MassFactory massFactory;
    private Vector3f position;
    public Geometry meteor;

    public Meteor(AssetManager assetManager, MassFactory massFactory, Vector3f position) {
        super(NAME);
        this.assetManager = assetManager;
        this.massFactory = massFactory;
        this.position = position;
        drawMeteor();
        light();
        
    }
    
    private void drawMeteor() {
        //The meteor looks wrong compared to the texture when using lightning material, not unshaded. However, this was cooler.

        /**
         * Illuminated bumpy rock with shiny effect. Uses Texture from
         * jme3-test-data library! Needs light source!
         */
        Sphere sphereMesh = new Sphere(32, 32, 30f);
        meteor = new Geometry("Shiny rock", sphereMesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(TextureDistributor.TEXTURE_LAVA));
        meteor.setMaterial(mat);
        setLocalTranslation(position);
        attachChild(meteor);
    }

    /**
     * A cone-shaped spotlight with location, direction, range
     */
    private void light() {

        SpotLight spot = new SpotLight();
        spot.setSpotRange(100);
        spot.setSpotOuterAngle(20 * FastMath.DEG_TO_RAD);
        spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
        spot.setDirection(new Vector3f(-1, -1, -1));
        addLight(spot);
    }
}
