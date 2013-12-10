/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import hiof.SkaalsveenEskerud.control.EvenBetterCharacterControl;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.character.Oto;
import hiof.SkaalsveenEskerud.control.GunControl;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.world.Portal;

/**
 *
 * @author root
 */
public class WalkableState extends AbstractAppState implements ActionListener {

    private boolean left, right, up, down, jumping;
    public Oto oto;
    private Vector3f walkDirection = new Vector3f(0, 0, 0); // stop
    private Vector3f gravityVector = new Vector3f(0f, -98.1f, 0);
    private float walkSpeed = 50f;
    public Camera cam;
    public EvenBetterCharacterControl character;
    private HiddenCursorState hiddenCursoState;
    private boolean firstTime = true;
    private ActionListener move;
    private Vector3f spawnPoint = new Vector3f(0, 200, 0);
    private boolean fast;

    public WalkableState() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        Main main = (Main) app;

        AssetManager assetManager = main.getAssetManager();
        Node rootNode = main.getRootNode();
        cam = main.getCamera();

        if (firstTime) {

            BulletAppState bas = stateManager.getState(BulletAppState.class);
            bas.getPhysicsSpace().setGravity(gravityVector);

            oto = new Oto(assetManager, bas, true);
            move = oto.getMovementListener();
            character = oto.getPlayerControl();
            rootNode.attachChild(oto);
            hiddenCursoState = new HiddenCursorState(oto, main.getInputManager());
            stateManager.attach(hiddenCursoState);

            Portal portal1 = new Portal(Portal.PORTAL_A, assetManager, oto, bas, main.massFactory);
            Portal portal2 = new Portal(Portal.PORTAL_B, assetManager, oto, bas, main.massFactory);
            
            portal1.connectTo(portal2);
            portal2.connectTo(portal1);
            
            main.world.worldNode.attachChild(portal1);
            main.world.worldNode.attachChild(portal2);

            //portal2.addControl(pc2);
            
            firstTime = false;
        }


        InputManager inputManager = main.getInputManager();

        GunControl gc = new GunControl(rootNode, main.getInputManager(), main, oto.handNode);
        gc.addAnimEventListener(main.playerManager.picker);
        rootNode.addControl(gc);
        inputManager.addListener(gc, KeyMapper.RELOAD_WEAPON);

        inputManager.addListener(this, KeyMapper.MOVE_LEFT, KeyMapper.MOVE_RIGHT);
        inputManager.addListener(this, KeyMapper.MOVE_FORWARD, KeyMapper.MOVE_BACKWARD);
        inputManager.addListener(this, KeyMapper.PLAYER_JUMP);
        inputManager.addListener(move, KeyMapper.MOVE_LEFT, KeyMapper.MOVE_RIGHT);
        inputManager.addListener(move, KeyMapper.MOVE_FORWARD, KeyMapper.MOVE_BACKWARD);
        inputManager.addListener(this, KeyMapper.MOVE_FAST);
        inputManager.addListener(this, KeyMapper.PLAYER_DODGE);
        inputManager.addListener(move, KeyMapper.PLAYER_DODGE);



    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        if (hiddenCursoState != null) {
            stateManager.attach(hiddenCursoState);
        }
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        if (hiddenCursoState != null) {
            stateManager.detach(hiddenCursoState);
        }
    }

    @Override
    public void update(float tpf) {
        float speed = fast ? walkSpeed * 2 : walkSpeed;

        Vector3f camDir = cam.getDirection().clone().multLocal(speed);
        Vector3f camLeft = cam.getLeft().clone().multLocal(speed);
        camDir.y = 0;
        camLeft.y = 0;
        walkDirection.set(0, 0, 0);

        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }

        if (jumping && oto.getPlayerControl().isOnGround()) {
            oto.getPlayerControl().jump();
        }

        // this is for making the character walk
        character.setWalkDirection(walkDirection);
        oto.setGunLocation();

        if (character.getPhysicsLocation().y < -1000f) {
            respawnSelf();
        }
    }

    public void onAction(String binding, boolean isPressed, float tpf) {

        if (binding.equals(KeyMapper.MOVE_LEFT)) {
            left = isPressed;
        } else if (binding.equals(KeyMapper.MOVE_RIGHT)) {
            right = isPressed;
        } else if (binding.equals(KeyMapper.MOVE_FORWARD)) {
            up = isPressed;
        } else if (binding.equals(KeyMapper.MOVE_BACKWARD)) {
            down = isPressed;
        } else if (binding.equals(KeyMapper.PLAYER_JUMP)) {
            jumping = isPressed;
        } else if (binding.equals(KeyMapper.MOVE_FAST)) {
            fast = isPressed;
        }
    }

    public void respawnSelf() {
        character.applyForce(spawnPoint, spawnPoint);
        character.setPhysicsLocation(spawnPoint);
        //cam.setLocation(spawnPoint);

    }
}
