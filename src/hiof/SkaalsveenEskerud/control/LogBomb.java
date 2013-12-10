/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author anskaal
 */
public class LogBomb extends AbstractControl {

    private Node node;
    private final BitmapText hudText;
    private float y = 0f;
    private final String spam;
    private final AssetManager assetManager;

    public LogBomb(AssetManager assetManager, String spam, ColorRGBA color) {
        node = new Node();
        this.spam = spam;
        this.assetManager = assetManager;
       
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/UbuntuMedium.fnt");
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setLocalScale(0.1f);
        hudText.setColor(color);
        node.attachChild(hudText);
        node.addControl(this);
        
        BillboardControl billboard = new BillboardControl();
        node.addControl(billboard);
    }

    @Override
    protected void controlUpdate(float tpf) {
        y += 25f * tpf;
        if (y < 1000f) {
            setText(spam);
            
            float z = hudText.getLineWidth()/5;
            hudText.setLocalTranslation(0, y, -z);
            
        } else {
            node.removeFromParent();
            node.removeControl(this);
        }

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void setText(String msg) {
        hudText.setText(msg);
    }

    public Node getNode() {
        return node;

    }
    
    public Geometry mkQuad(ColorRGBA color) {
        
        Quad quad = new Quad(1, 1);
        Geometry geom = new Geometry("impact indicator", quad);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        if(ColorRGBA.BlackNoAlpha == color){
            mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.FrontAndBack);
        }
        else{
            mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        }
        
        geom.setMaterial(mat);
        return geom;
    }
}
