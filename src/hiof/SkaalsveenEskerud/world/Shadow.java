/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.shadow.BasicShadowRenderer;

/**
 *
 * @author martin
 */
public class Shadow {

    private final AssetManager am;
    ViewPort vp;

    public Shadow(AssetManager am, ViewPort vp) {
        BasicShadowRenderer bsr = new BasicShadowRenderer(am, 256);
        bsr.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal()); // light direction
        vp.addProcessor(bsr);
        this.am = am;



    }

    public void setShadowCastAndReceieve(Node n) {
        n.setShadowMode(ShadowMode.CastAndReceive);
    }

    public void setShadowCast(Node n) {
        n.setShadowMode(ShadowMode.Cast);
    }

    public void setShadowReceive(Node n) {
        n.setShadowMode(ShadowMode.Receive);
    }

    public void setShadowOff(Node n) {
        n.setShadowMode(ShadowMode.Off);
    }
}
