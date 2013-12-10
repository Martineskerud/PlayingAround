/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.network.serializing.Serializable;
import java.util.HashMap;

/**
 *
 * @author anskaal
 *
 * Organizing worldData as hashMap in order to increese performance
 */
@Serializable
public class WorldData extends HashMap<String, Float> {

    public WorldData() {
        put("window", 1.0f);
        put("elevatorProgressValue", 1.0f);
    }
}
