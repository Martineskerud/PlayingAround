/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.appstate;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.control.PowerUpCollisionControl;
import hiof.SkaalsveenEskerud.control.PortalControl;
import hiof.SkaalsveenEskerud.hud.DisplayManager;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.network.GameClient2;
import hiof.SkaalsveenEskerud.network.PlayerManager;
import hiof.SkaalsveenEskerud.network.NetworkManager;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.Portal;
import hiof.SkaalsveenEskerud.world.World;
import hiof.SkaalsveenEskerud.world.model.PowerUp;

/**
 *
 * @author root
 */
public class InGameState extends AbstractAppState implements ActionListener {

    private Main main;
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private Node rootNode;
    private DisplayManager displayManager;
    private boolean physicsDebugging = false;
    private String TOGGLE_DEBUG = "Toggle Debug";
    private PowerUp powerUp;

    public InGameState(DisplayManager displayManager) {
        this.displayManager = displayManager;

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        main = (Main) app;
        rootNode = main.getRootNode();
        assetManager = main.getAssetManager();

        System.err.println("Loading complete.");



        // Create the world with all it's models and props
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        main.massFactory = new MassFactory(bulletAppState);

        // Creating world and player models
        main.world = new World(main.getMainContainer());
        main.world.init();

        InputManager inputManager = main.getInputManager();

        // Adding manager object for the players and their data
        main.playerManager = new PlayerManager(main, displayManager, main.getMainContainer());
        main.walkableState = new WalkableState();
        stateManager.attach(main.walkableState);



        // Adding networking
        main.networkManager = new NetworkManager(rootNode, main);
        displayManager.setChatMessageListener(main.networkManager.getClient());

        GameClient2 gc2 = main.networkManager.getClient();
        gc2.bindWalkableState(main.walkableState);

        // Connect buttons to ActionListeners
        KeyMapper.setup(inputManager);
        inputManager.addListener(main.escInputManager, SimpleApplication.INPUT_MAPPING_EXIT);

        displayManager.setMessage("");

        inputManager.addMapping(TOGGLE_DEBUG, new KeyTrigger(KeyInput.KEY_F9));
        inputManager.addListener(this, new String[]{TOGGLE_DEBUG});

        //Adding the power ups
        //this should be done in some kind of update loop
        powerUp = new PowerUp(main.world.md, main.world.textureDistributor, assetManager, main.massFactory);
        main.world.worldNode.attachChild(powerUp.getNode());

        PowerUpCollisionControl cc = new PowerUpCollisionControl(bulletAppState, powerUp);
        cc.addMyCollisionListener(powerUp);
        cc.addMyCollisionListener(main.playerManager.ammoManager);
        cc.addMyCollisionListener(main.playerManager.healthManager);
    }

    @Override
    public void update(float tpf) {

        main.handleFpsLimit(tpf, 180);
        main.world.update(tpf);


        //making the oto-character go in x-direction ...
        //root-node of character is kind of fucked up thogh.

        //oto.getPlayerControl().setWalkDirection(new Vector3f(1000f, 0, 0).mult(tpf));

    }

    public void togglePhysicsDebugMode() {

        PhysicsSpace pSpace = bulletAppState.getPhysicsSpace();
        if (!physicsDebugging) {
            System.out.println("Enabling physics debuging");
            pSpace.enableDebug(assetManager);
        } else {
            System.out.println("Disabling physics debuging");
            pSpace.disableDebug();
        }

        physicsDebugging = !physicsDebugging;

    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(TOGGLE_DEBUG) && isPressed) {

            togglePhysicsDebugMode();

        }
    }
}
