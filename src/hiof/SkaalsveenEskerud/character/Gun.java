/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.character;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import static hiof.SkaalsveenEskerud.character.Oto.HEIGHT;
import hiof.SkaalsveenEskerud.weapon.Pistol;

/**
 *
 * @author Martin
 */
public class Gun extends Node {

    private final AssetManager am;

    public Gun(AssetManager am) {
        this.am = am;
        name = "Gun";

    }

    public Node attachGun(String name, Node character) {

        //remove the only child of the character, the gun.
        character.detachAllChildren();
        Pistol pistol = new Pistol(null, null, am);
        //add another gun in its place.
        Node gunNode = new Node();
        gunNode.setName("gunNode");

        if (name.equals(Pistol.GUN1)) {
            gunNode.attachChild(pistol.drawPistol());
            // Pistol.guns.put(Pistol.GUN1, gunNode);
        } else if (name.equals(Pistol.GUN2)) {
            gunNode.setName("gunNode");
            gunNode.attachChild(pistol.drawRocketLauncher());
        }

        gunNode.getChild(0).setLocalTranslation(new Vector3f(-2f, HEIGHT - 2, 5));
        //Pistol.guns.put(Pistol.GUN2, gunNode);
        return gunNode;
    }
}
