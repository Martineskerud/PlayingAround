/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model.component;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 * Convenience-class for creating Nodes with many children created in a
 * factory-object.
 *
 * @author anskaal
 */
public class ArrayListNode extends Node {

    public ArrayListNode(String name) {
        super(name);
    }

    /*
     * Class could be extended to also make the ArrayList avilable...
     */
    public void attach(ArrayList<Spatial> spatials) {

        for (Spatial s : spatials) {
            attachChild(s);
        }
    }
}
