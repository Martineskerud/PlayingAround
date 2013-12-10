/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 * Extend this class to access rootNode and assetManager
 *
 * @author anskaal
 */
public abstract class MainComponent {

    public Node rootNode;
    public Node guiNode;
    public AssetManager assetManager;
    public ViewPort vp;
    public AppSettings settings;
    public Camera cam;
    public BulletAppState bas;
    public InputManager im;

    public MainComponent(Main.MainContainer mainContainer) {
        this.guiNode = mainContainer.guiNode;
        this.rootNode = mainContainer.rootNode;
        this.assetManager = mainContainer.assetManager;
        this.vp = mainContainer.vp;
        this.settings = mainContainer.settings;
        this.bas = mainContainer.bas;
        this.im = mainContainer.im;
        this.cam = mainContainer.cam;
    }

    public MainComponent(MainComponent parentNode) {
        this.rootNode = parentNode.rootNode;
        this.assetManager = parentNode.assetManager;
        this.vp = parentNode.vp;
        this.settings = parentNode.settings;
        this.bas = parentNode.bas;
        this.im = parentNode.im;
        this.cam = parentNode.cam;
    }

    public abstract void init();

    public abstract void update(float tpf);
}
