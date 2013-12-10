/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import hiof.SkaalsveenEskerud.GameData;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.Main.MainContainer;
import hiof.SkaalsveenEskerud.control.ElevatorControl;
import hiof.SkaalsveenEskerud.control.MeteorControl;
import hiof.SkaalsveenEskerud.hud.DisplayManager;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.sound.GunAudio;
import hiof.SkaalsveenEskerud.weapon.AmmoManager;
import hiof.SkaalsveenEskerud.weapon.Pistol;
import hiof.SkaalsveenEskerud.weapon.health.HealthManager;
import hiof.SkaalsveenEskerud.world.World;
import hiof.SkaalsveenEskerud.world.model.Meteor;
import hiof.SkaalsveenEskerud.world.model.OilDrum;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import hiof.SkaalsveenEskerud.world.model.util.TextureDistributor;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anskaal
 *
 * A class for handeling players.
 */
public class PlayerManager implements MessageListener<Client> {

    private final AssetManager assetManager;
    public WeaponPicker picker;
    private final World world;
    public GameData gameData;
    private DisplayManager displayManager;
    private GameClient2 networkClient;
    private HashMap<String, HitListener> pickHashMap;
    private final Main main;
    private final Node rootNode;
    private ElevatorControl ec;
    private long openTime;
    public final AmmoManager ammoManager;
    private boolean meteorStarted = false;
    private MassFactory massFactory;
    private final BulletAppState bulletAppState;
    private Geometry meteorGeo;
    private MeteorControl meteorControl;
    private RigidBodyControl meteorRigidBodyControl;
    public final HealthManager healthManager;

    public PlayerManager(Main main, DisplayManager displayManager, MainContainer mc) {
        this.main = main;
        this.rootNode = main.getRootNode();
        this.assetManager = main.getAssetManager();
        this.world = main.world;
        this.displayManager = displayManager;
        this.ec = main.world.elevator.sc;
        openTime = System.currentTimeMillis();
        gameData = new GameData();
        gameData.addDataChangeListener(displayManager);
        massFactory = mc.massFactory;
        bulletAppState = mc.bas;


        ammoManager = new AmmoManager();
        ammoManager.addAmmoChangedListener(displayManager.hud);
        main.getInputManager().addListener(ammoManager, KeyMapper.RELOAD_WEAPON);

        healthManager = new HealthManager(displayManager.hud);
        bulletAppState.getPhysicsSpace().addCollisionListener(healthManager);

        setupPicker();

    }

    /**
     * An HashMap is created for storing the actions according to hit-objects. A
     * picker will trigger by the mousebuttons and fire an event.
     */
    private void setupPicker() {

        picker = new WeaponPicker(this) {
            @Override
            protected void onPlayerHit(String playerName, String triggerKey) {

                System.out.println("PLAYER " + playerName);
                if (gameData.isPlayer(playerName)) {

                    if (networkClient != null) {
                        networkClient.damagePlayerByWeapon(playerName, Pistol.currentWeapon, triggerKey);
                    } else {
                        System.err.println("Network Client is null");
                    }
                }
            }

            protected void displayBillboardImage() {
                new Thread() {
                    @Override
                    public void run() {

                        Spatial bbm = rootNode.getChild("billboardMain");

                        if (bbm != null) {
                            Material mat = new Material(assetManager, MaterialDistributor.PATH_LIGHTING);
                            Texture tex = assetManager.loadTexture(TextureDistributor.TEXTURE_NO_POWER);
                            mat.setTexture("DiffuseMap", tex);
                            bbm.setMaterial(mat);
                        }

                        boolean running = true;
                        while (running) {
                            try {
                                join();
                                running = false;
                            } catch (InterruptedException ex) {
                                Logger.getLogger(PlayerManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }.start();
            }

            @Override
            public void onAction() {
                ammoManager.fireCurrentGun();
            }

            @Override
            public void onFailDueToEmptyMagazine() {
                displayManager.hud.createLogBomb("Press R to reload the magazine!");
            }
        };
        picker.setHitListener("buttonDown", doorsDown);
        picker.setHitListener("specialButton", meteorDrop);
        picker.setHitListener("myMeteor", meteorShot);
        picker.setHitListener("Window", disappear);
        picker.setTriggerDelay(150);
    }
    HitListener disappear = new HitListener() {
        public void onHit(Geometry geom, String name, String triggerKey) {
            if (triggerKey.equals(KeyMapper.PLAYER_SHOOT)) {
                geom.getControl(RigidBodyControl.class).setEnabled(false);
                geom.removeFromParent();
                GunAudio glassAudio = new GunAudio(assetManager, GunAudio.GLASS);
                glassAudio.playAudio();
            }
        }
    };
    HitListener doorsDown = new HitListener() {
        public void onHit(Geometry geom, String name, String triggerKey) {
            long tempTime = System.currentTimeMillis();
            if (tempTime >= openTime + 4000) {
                ec.bringElevatorUp();
                openTime = tempTime;
            }
        }
    };
    HitListener meteorDrop = new HitListener() {
        public void onHit(Geometry geom, String name, String triggerKey) {
            if (!meteorStarted) {
                meteorStarted = true;

                Meteor meteor = new Meteor(assetManager, massFactory, new Vector3f(100, 100, 100));

                meteorRigidBodyControl = new RigidBodyControl(1);
                meteorRigidBodyControl.setSpatial(meteor);
                bulletAppState.getPhysicsSpace().add(meteorRigidBodyControl);
                meteorRigidBodyControl.setKinematic(false);

                meteorGeo = meteor.meteor;
                meteorGeo.setName("myMeteor");
                meteorGeo.addControl(meteorRigidBodyControl);
                meteorControl = new MeteorControl(meteorGeo);
                meteor.addControl(meteorControl);
                world.worldNode.attachChild(meteor);


            }

        }
    };
    HitListener meteorShot = new HitListener() {
        public void onHit(Geometry geom, String name, String triggerKey) {
            if (meteorStarted) {

                System.out.println("you shot the meteor, you bastards!");
                Vector3f pickDirection = main.getCamera().getDirection();
                System.out.println(pickDirection);
                if (pickDirection.length() > 0) {
                    pickDirection.normalize();
                }
                //we have to apply a lot of force as this is a pretty huge object.
                pickDirection.multLocal(5000);
                meteorRigidBodyControl.applyCentralForce(pickDirection);

            }
        }
    };

    public void messageReceived(Client source, Message m) {

        if (gameData != null) {
            gameData.messageReceived(source, m);
        }

    }

    public void setServer(boolean b) {
        displayManager.setServer(b);
    }

    public DisplayManager getDisplaymanager() {
        return displayManager;
    }

    void attachNetworkClient(GameClient2 gameClient) {
        this.networkClient = gameClient;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Camera getCamera() {
        return main.getCamera();
    }

    public World getWorld() {
        return world;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
