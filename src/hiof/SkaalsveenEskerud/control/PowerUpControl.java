/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.PowerUp;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;

/**
 *
 * @author Martin
 */
public class PowerUpControl extends Node {
    public static final String NAILS = "nails";
    public static final String PLUS_HEALTH = "plusHealth";
    
    public static int n = 0;

    private MaterialDistributor md;
    private TextureDistributor td;
    private AssetManager am;
    private MassFactory mf;
    private Vector3f position;
    private String asset;
    private Texture tex;
    public Geometry geom;
    public boolean isVisible = true;
    public final String type;
    
    public PowerUpControl(String type, String name, PowerUp powerUp, Vector3f position, String asset) {
        super("PowerUpControl");
        this.md = powerUp.md;
        this.td = powerUp.td;
        this.am = powerUp.am;
        this.mf = powerUp.mf;
        this.position = position;
        this.asset = asset;
        this.type = type;
        
        assignAsset();
        drawPowerUp(tex, name);
    }

    private void assignAsset() {

        if (asset.equals("25")) {
            tex = td.getAsset(TextureDistributor.TEXTURE_HEALTH25);
        } else if (asset.equals("nail")) {
            tex = td.getAsset(TextureDistributor.TEXTURE_NAILS);
        } else if (asset.equals("50")) {
            tex = td.getAsset(TextureDistributor.TEXTURE_HEALTH50);
        } else if (asset.equals("15")) {
            tex = td.getAsset(TextureDistributor.TEXTURE_HEALTH15);
        } else if (asset.equals("100")) {
            tex = td.getAsset(TextureDistributor.TEXTURE_HEALTH100);
        }

    }

    private void drawPowerUp(Texture tex, String name) {
        BillboardControl billboard = new BillboardControl();
        
        geom = new Geometry(name, new Quad(7, 7));
        assignMass(geom);
        geom.setLocalTranslation(position);
        
        Material mat = md.getAsset(MaterialDistributor.PATH_LIGHTING);
        mat.setColor("Ambient", ColorRGBA.White.mult(0.3f));
        mat.setColor("Diffuse", ColorRGBA.White.mult(0.6f));
        mat.setColor("Specular", ColorRGBA.White.mult(0.65f));

        mat.setBoolean("UseMaterialColors", true);
        mat.setBoolean("UseAlpha", true);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        geom.setMaterial(mat);
        mat.setTexture("DiffuseMap", tex);
        geom.addControl(billboard);
        attachChild(geom);
    }

    private void assignMass(Geometry inputGeo) {
        mf.assignMass(inputGeo, 0, Vector3f.ZERO, true);

    }
    
    public void setVisible(boolean visible){
        
        geom.getControl(RigidBodyControl.class).setEnabled(visible);
        
        if(visible){
            attachChild(geom);
        }
        else{
            geom.removeFromParent();
        }
        
        isVisible = visible;
    }

    
}
