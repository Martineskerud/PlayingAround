/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.weapon;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Torus;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;
import java.util.HashMap;

/**
 *
 * @author martin
 */
public class Pistol {

    private AssetManager am;
    private TextureDistributor td;
    private MaterialDistributor md;
    public static String GUN1 = "pistol";
    public static String GUN2 = "rocketlauncher";
    public static HashMap<String, GunNode> guns;
    public static String currentWeapon;

    public Pistol(Node camNode, Camera cam, AssetManager am) {
        this.am = am;
        this.td = new TextureDistributor(am);
        this.md = new MaterialDistributor(am);

        guns = new HashMap<String, GunNode>();
        currentWeapon = GUN1;
    }

    public Node drawPistol() {

        Node gun1Node = new Node("gun1Node");

        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));

        //slices, other slices, radius, length
        Cylinder barrel = new Cylinder(35, 35, 0.75f, 15, true);
        Geometry barrelGeo = new Geometry("weapon1", barrel);

        Material barrelMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
        barrelMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        barrelMat.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_ARM1));
        barrelGeo.setMaterial(barrelMat);
        gun1Node.attachChild(barrelGeo);

        Geometry barrel1Misc = new Geometry("weapon1_1", barrel);
        Material mat2 = barrelMat.clone();
        mat2.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_ARM1_1));
        barrel1Misc.setMaterial(mat2);
        barrel1Misc.setLocalTranslation(new Vector3f(0.5f, -1, -2));
        gun1Node.attachChild(barrel1Misc);

        Torus torusMisc1 = new Torus(7, 7, 0.1f, 0.2f);
        Geometry barrel1Misc2 = new Geometry("weapon1_2", torusMisc1);
        Material mat3 = barrelMat.clone();

        mat3.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_ALUMINIUM));
        barrel1Misc2.setMaterial(mat3);
        for (int i = 0; i < 4; i++) {
            Spatial ring = barrel1Misc2.clone();
            ring.setLocalTranslation(new Vector3f(0.8f, 0, 4 - i * 1f));
            gun1Node.attachChild(ring);
        }

        return gun1Node;
    }

    public Node drawRocketLauncher() {

        Node gun2Node = new Node("gun2Node");

        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));

        //slices, other slices, radius, length
        Cylinder barrel = new Cylinder(35, 35, 0.75f, 15, false);
        Geometry barrelGeo = new Geometry("weapon1", barrel);

        Material barrelMat = md.getAsset(MaterialDistributor.PATH_UNSHADED);
        barrelMat.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_ALUMINIUM));
        barrelGeo.setMaterial(barrelMat);
        gun2Node.attachChild(barrelGeo);

        Geometry barrel1Misc = new Geometry("weapon1_1", barrel);
        Material mat2 = barrelMat.clone();
        mat2.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_ANDERS));
        barrel1Misc.setMaterial(mat2);
        barrel1Misc.setLocalTranslation(new Vector3f(0.5f, -1, -2));
        gun2Node.attachChild(barrel1Misc);

        Torus torusMisc1 = new Torus(7, 7, 0.1f, 0.2f);
        Geometry barrel1Misc2 = new Geometry("weapon1_2", torusMisc1);
        Material mat3 = barrelMat.clone();
        mat3.setTexture("ColorMap", td.getAsset(TextureDistributor.TEXTURE_ALUMINIUM));
        barrel1Misc2.setMaterial(mat3);
        for (int i = 0; i < 4; i++) {
            Spatial ring = barrel1Misc2.clone();
            ring.setLocalTranslation(new Vector3f(0.8f, 0, 4 - i * 1f));
            gun2Node.attachChild(ring);
        }

        return gun2Node;
    }
}
