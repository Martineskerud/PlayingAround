/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.hud;

import com.jme3.asset.AssetManager;
import com.jme3.ui.Picture;

/**
 *
 * @author martin
 *
 * anskaal: - removed extension to mainComponent - Changed contructor to only
 * take am, with & height - extended Picture
 */
public class CrossHair extends Picture {

    public CrossHair(AssetManager assetManager, int width, int height) {
        super("HUD Picture");


        setImage(assetManager, "Textures/Crosshair.png", true);

        float delta = width / height * 25;
        setPosition(width / 2 - (delta / 2), height / 2 - delta / 2);

        setWidth(delta);
        setHeight(delta);

    }
}
