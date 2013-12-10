/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.character;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import static hiof.SkaalsveenEskerud.character.PhysicalPlayer.HEIGHT;
import hiof.SkaalsveenEskerud.control.EvenBetterCharacterControl;
import hiof.SkaalsveenEskerud.control.ImprovedMovement;
import hiof.SkaalsveenEskerud.weapon.GunNode;

import hiof.SkaalsveenEskerud.weapon.Pistol;

/**
 *
 * @author martin
 */
public class Oto extends PhysicalPlayer {

    private Spatial model;
    private GunNode gun2Node;
    public GunNode gun1Node;
    public Node handNode;
    private Vector3f gunPos = new Vector3f(0, HEIGHT - 2, 0);
    //private Movement movement;
    public ImprovedMovement movement;
    private final boolean isMe;
    private Node playerNode;

    public Oto(AssetManager am, BulletAppState bulletAppState, boolean isMe) {
        super("Oto");
        this.isMe = isMe;
        spawn(am, bulletAppState);

    }

    private void spawn(AssetManager am, BulletAppState bulletAppState) {

        playerNode = new Node((isMe ? "Me" : "playerNode"));
        createPlayerControl();

        if (!isMe) {
            playerControl.getRigidBody().setKinematic(true);
        }

        PhysicsSpace pSpace = bulletAppState.getPhysicsSpace();
        pSpace.add(playerControl);

        model = am.loadModel("Models/Otwo/Oto/Oto.mesh.xml");
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.8f));
        model.addLight(al);
        model.setLocalScale(1f);
        model.setLocalTranslation(new Vector3f(0f, HEIGHT / 2, 0f));

        playerNode.attachChild(model);
        playerNode.addControl(playerControl);

        attachChild(playerNode);

        handNode = new Node("handNode");

        Pistol pistol = new Pistol(null, null, am);

        gun1Node = new GunNode("gun1Node");
        gun1Node.setLocalTranslation(-2f, 0f, 5);
        gun1Node.attachChild(pistol.drawPistol());

        gun2Node = new GunNode("gun2Node");
        gun2Node.setLocalTranslation(-2f, 0f, 5);
        gun2Node.attachChild(pistol.drawRocketLauncher());

        Pistol.guns.put(Pistol.GUN1, gun1Node);
        Pistol.guns.put(Pistol.GUN2, gun2Node);

        handNode.attachChild(gun1Node);
        gun2Node.setLocalTranslation(new Vector3f(-2f, HEIGHT - 2, 5));

        attachChild(handNode);
        setGunLocation();

        // movement = new Movement(this);
        movement = new ImprovedMovement(this);
        playerControl.setJumpListener(movement);
    }

    public ActionListener getMovementListener() {
        return movement;
    }

    public ImprovedMovement getMovement() {
        return movement;
    }
    /*
     public Movement getMovement() {
     return movement;
     }
 
     * */

    public EvenBetterCharacterControl getPlayerControl() {
        return playerControl;
    }

    public void setGunRotation(Quaternion b) {
        handNode.setLocalRotation(b);
    }

    public void setGunLocation() {
        Vector3f vec = playerControl.getPhysicsLocation().add(gunPos);
        handNode.setLocalTranslation(vec);
    }

    public void setLocation(Vector3f translation) {

        getPlayerControl().setPhysicsLocation(translation);
        setGunLocation();
    }

    public void setRotation(Quaternion rotation) {
        
        if (isMe) {
            getPlayerControl().setPhysicsRotation(rotation);
            
            
            
        } else {
            Matrix3f m = rotation.toRotationMatrix();
            float[] h = rotation.toAngles(new float[3]);
            h[0] = 0;
            h[2] = 0;
            Quaternion q = new Quaternion();
            
            q.fromAngles(h);
            
            
            model.setLocalRotation(q);
        }
        setGunRotation(rotation);

    }

    public void update(Vector3f pos, Quaternion rot) {

        
        setLocation(pos);
        setRotation(rot);

    }

    public void setGunDodgeFactor(float factor) {
        gunPos = new Vector3f(0, HEIGHT - 2 - factor, 0);
    }
}
