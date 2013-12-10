/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 *
 * @author anskaal
 */
public class Skybox {

    public static final int DEFAULT = 0;
    public static final int BRIGHT_SKY = 1;
    public static final int NIGHT = 2;
    private static final String[] PATH = new String[]{
        "Skybox.dds",
        "Bright/BrightSky.dds",
        "Night/Night_dxt1.dds"
    };

    static void create(Node rootNode, AssetManager assetManager, int type) {
        Spatial sky = SkyFactory.createSky(assetManager, "Textures/Sky/" + PATH[type], false);
        rootNode.attachChild(sky);
    }
}
