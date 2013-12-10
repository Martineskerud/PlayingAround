/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import hiof.SkaalsveenEskerud.control.PowerUpCollisionControl;
import hiof.SkaalsveenEskerud.control.PowerUpControl;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.sound.PowerUpAudio;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Martin
 */
public class PowerUp extends AbstractControl implements PowerUpCollisionControl.MyCollisionListener {

    public static final long SPAWN_TIME = 30000;
    public final MaterialDistributor md;
    public final TextureDistributor td;
    public final AssetManager am;
    public final MassFactory mf;
    private PowerUpAudio pua;
    private final Node node;
    private ArrayList<TimeoutObject> spawnQueue;
    private HashMap<String, PowerUpControl> powerUpMap;

    public PowerUp(MaterialDistributor md, TextureDistributor td, AssetManager am, MassFactory mf) {
        this.md = md;
        this.td = td;
        this.am = am;
        this.mf = mf;
        this.node = new Node("PowerUpsNode");
        this.powerUpMap = new HashMap<String, PowerUpControl>();

        node.addControl(this);

        spawnQueue = new ArrayList<TimeoutObject>();
        audio();
        drawPowerUps();
    }

    private void drawPowerUps() {

        makeHealth(new Vector3f(197, 0, 0), "25");
        makeHealth(new Vector3f(500, 0, 0), "25");
        makeHealth(new Vector3f(290, 45, 403), "50");
        makeHealth(new Vector3f(300, 0, -140), "15");
        makeHealth(new Vector3f(330, 0, -140), "15");
        makeHealth(new Vector3f(360, 0, -140), "15");
        makeHealth(new Vector3f(390, 0, -140), "15");
        makeHealth(new Vector3f(420, 0, 400), "100");
        makeAmmo(new Vector3f(-30, 0, 150), "nail");
    }

    private void setInSpawnQueue(String name) {

        PowerUpControl element = powerUpMap.get(name);

        if (element.isVisible) {
            element.setVisible(false);

            pua.playAudio();
            spawnQueue.add(new TimeoutObject(element));
        }

    }

    private void checkFirstInSpawnQueue() {
        long now = System.currentTimeMillis();

        if (spawnQueue.size() > 0) {
            TimeoutObject firstElement = spawnQueue.get(0);

            if (firstElement.isSpawnReady(now)) {

                final PowerUpControl element = firstElement.element;
                node.attachChild(element);

                element.setVisible(true);

                //pua.playAudio();

                spawnQueue.remove(0);
                checkFirstInSpawnQueue();
            }

        }

    }

    public PowerUpControl getSubNode(String name) {
        if (powerUpMap.containsKey(name)) {
            return powerUpMap.get(name);
        } else {
            return null;
        }
    }

    public void onCollision(PowerUpControl element, String name) {
        System.out.println("element picked up: " + name);

        if (powerUpMap.containsKey(name)) {
            setInSpawnQueue(name);

        } else {
            System.err.println("did not find element " + name + "in powerUpMap");
        }
    }

    public boolean isPowerUp(String name) {
        return powerUpMap.containsKey(name);
    }

    private void MakePowerUpControl(String type, Vector3f vec, String asset) {

        String name = type + ":" + asset + ":" + powerUpMap.size();
        PowerUpControl puc = new PowerUpControl(type, name, this, vec, asset);
        powerUpMap.put(name, puc);
        node.attachChild(puc);
    }

    public void onOtherObjects(String name) {
        
        
        
    }

    class TimeoutObject {

        PowerUpControl element;
        long timestamp;

        public TimeoutObject(PowerUpControl element) {
            this.element = element;
            this.timestamp = System.currentTimeMillis() + SPAWN_TIME;
        }

        boolean isSpawnReady(long now) {
            return (now > timestamp);
        }
    }

    private void audio() {

        pua = new PowerUpAudio(am);
        node.attachChild(pua);

    }

    public PowerUpAudio getPowerUpAudio() {
        return pua;
    }

    private void makeHealth(Vector3f vec, String asset) {

        MakePowerUpControl(PowerUpControl.PLUS_HEALTH, vec, asset);
    }

    private void makeAmmo(Vector3f vec, String asset) {

        MakePowerUpControl(PowerUpControl.NAILS, vec, asset);
    }

    @Override
    protected void controlUpdate(float tpf) {

        checkFirstInSpawnQueue();

    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Spatial getNode() {
        return node;
    }
}
