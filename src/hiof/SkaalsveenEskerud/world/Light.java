/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author anskaal
 */
public class Light {

    public Light(Node worldNode) {

        makeAmbientLight(worldNode);

        makeSpotLight(worldNode);

    }

    private void makeAmbientLight(Node worldNode) {

        //Add light 
        AmbientLight sun = new AmbientLight();
        sun.setColor(ColorRGBA.White.mult(1.6f));
        worldNode.addLight(sun);

    }

    public void makeSpotLight(Node worldNode) {

        SpotLight sl = new SpotLight();
        sl.setColor(ColorRGBA.White);
        sl.setDirection(new Vector3f(-1f, -1f, -1f));
        sl.setPosition(new Vector3f(0, 5, 0f));
        sl.setSpotRange(900f);
        worldNode.addLight(sl);

    }
}
