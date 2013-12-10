/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model.util;

import com.jme3.asset.AssetManager;
import java.util.Hashtable;

/**
 *
 * @author martin
 */
public abstract class Distributor<T> extends Hashtable<String, T> {

    public final static String PATH_LIGHTING = "Common/MatDefs/Light/Lighting.j3md";
    public final static String PATH_UNSHADED = "Common/MatDefs/Misc/Unshaded.j3md";
    protected final AssetManager am;
    /*  Light/Lighting.j3md
     Misc/Unshaded.j3md
     */

    public Distributor(AssetManager am) {
        super();
        this.am = am;
    }

    public T getAsset(String input) {
        if (containsKey(input)) {
            return getClone(input);
        } else {

            return create(input);
        }

    }

    protected abstract T getClone(String input);

    protected abstract T create(String input);
}
