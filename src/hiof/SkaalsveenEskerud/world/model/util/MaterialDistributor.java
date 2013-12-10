/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

/**
 *
 * @author martin
 *
 * v 6.01 This class clones materials to reduce disk use.
 */
public class MaterialDistributor extends Distributor<Material> {

    public final static String PATH_LIGHTING = "Common/MatDefs/Light/Lighting.j3md";
    public final static String PATH_UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
    public final static String PATH_MY_TEST_SHADER = "MatDefs/MyTestShader/MyTestShader.j3md";
    public final static String PATH_TEST="Materials/Electricity2_2.j3md";

    public MaterialDistributor(AssetManager am) {
        super(am);
    }

    @Override
    protected Material getClone(String input) {
        return get(input).clone();
    }

    @Override
    protected Material create(String input) {
        Material mat = new Material(am, input);
        put(input, mat);
        return mat;
    }
}
