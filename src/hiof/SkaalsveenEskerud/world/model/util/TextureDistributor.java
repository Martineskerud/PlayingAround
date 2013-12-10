/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model.util;

import com.jme3.asset.AssetManager;
import com.jme3.texture.Texture;

/**
 *
 * @author martin
 */
public class TextureDistributor extends Distributor<Texture> {

    public static final String TEXTURE_CONCRETE = "Textures/concrete.jpg";
    public static final String TEXTURE_TILES = "Textures/bluetiles.jpg";
    public static final String TEXTURE_BRICKWALL = "Textures/brickwall.jpg";
    public static final String TEXTURE_CROSSHAIR = "Textures/Crosshair.png";
    public static final String TEXTURE_GREYWOOD = "Textures/greyWood.jpg";
    public static final String TEXTURE_TEMPBENDER = "Textures/tempBender.bmp";
    public static final String TEXTURE_TEMPWALLE = "Textures/tempWalle.bmp";
    public static final String TEXTURE_SKYBOX_1 = "Textures/tex.png";
    public static final String TEXTURE_NO_POWER = "Textures/no_power.jpg";
    public static final String TEXTURE_TINROOF = "Textures/tinroof.jpg";
    public static final String TEXTURE_TIRE = "Textures/tire.jpg";
    public static final String TEXTURE_ANDERS = "Textures/anders.jpg";
    public static final String TEXTURE_ALUMINIUM = "Textures/aluminium.png";
    public static final String TEXTURE_GARAGEDOOR = "Textures/garagedoor.jpg";
    public static final String TEXTURE_CEILING = "Textures/ceiling.jpg";
    public static final String TEXTURE_DOWN = "Textures/down.png";
    public static final String TEXTURE_UP = "Textures/up.png";
    public static final String TEXTURE_SILVER = "Textures/silver.jpg";
    public static final String TEXTURE_ARM1 = "Textures/armtex1.png";
    public static final String TEXTURE_ARM1_1 = "Textures/armtex1_1.png";
    public static final String TEXTURE_ARM2 = "Textures/armtex2.png";
    public static final String TEXTURE_BULLETHOLE = "Textures/bullethole.png";
    public static final String TEXTURE_CARGO_1 = "Textures/cargo_1.png";
    public static final String TEXTURE_CARGO_1_SIDE = "Textures/cargo_1_side.jpg";
    public static final String TEXTURE_CARGO_2 = "Textures/cargo_2.png";
    public static final String TEXTURE_CARGO_2_SIDE = "Textures/cargo_2_side.jpg";
    public static final String TEXTURE_CARGO_3 = "Textures/cargo_3.png";
    public static final String TEXTURE_CARGO_3_SIDE = "Textures/cargo_3_side.jpg";
    public static final String TEXTURE_CARGO_4 = "Textures/cargo_4.png";
    public static final String TEXTURE_CARGO_4_SIDE = "Textures/cargo_4_side.jpg";
    public static final String TEXTURE_TRANSPARENT_STEPS = "Textures/transparent_steps.png";
    public static final String TEXTURE_HIOF = "Textures/hiof.jpg";
    public static final String TEXTURE_ENIGMA = "Textures/enigma.jpg";
    public static final String TEXTURE_OILDRUM = "Textures/oil.png";
    public static final String TEXTURE_OLDWOOD = "Textures/oldWood.jpg";
    public static final String TEXTURE_TRASHCAN = "Textures/trashcan.jpg";
    public static final String TEXTURE_TRASHCAN_LID = "Textures/trashcan_lid.jpg";
    public static final String TEXTURE_ROCKET = "Textures/rockets.png";
    public static final String TEXTURE_NAILS = "Textures/nails.png";
    public static final String TEXTURE_HEALTH100 = "Textures/health100.png";
    public static final String TEXTURE_HEALTH50 = "Textures/health50.png";
    public static final String TEXTURE_HEALTH25 = "Textures/health25.png";
    public static final String TEXTURE_HEALTH15 = "Textures/health15.png";
    public static final String TEXTURE_LAVA = "Textures/lava.png";
    public static final String TEXTURE_COMET="Textures/comet.jpg";
    
    public TextureDistributor(AssetManager am) {
        super(am);
    }

    @Override
    protected Texture getClone(String input) {
        return get(input).clone();
    }

    @Override
    protected Texture create(String input) {
        Texture tex = am.loadTexture(input);
        put(input, tex);
        return tex;
    }
}