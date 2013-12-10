/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.character.Oto;
import hiof.SkaalsveenEskerud.control.EvenBetterCharacterControl;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.picking.Picker;

/**
 *
 * @author root
 */
public class HiddenCursorState extends AbstractAppState implements AnalogListener {

    private static String[] mappings = new String[]{
        KeyMapper.MOUSE_LEFT, KeyMapper.MOUSE_RIGHT, KeyMapper.MOUSE_UP, KeyMapper.MOUSE_DOWN, KeyMapper.MOUSE_ZOOM_IN, KeyMapper.MOUSE_ZOOM_OUT,};
    protected float rotationSpeed = 1f;
    protected boolean invertY = false;
    private InputManager inputManager;
    private Camera cam;
    private Vector3f initialUpVec;
    private final EvenBetterCharacterControl character;
    private Vector3f camOffset = new Vector3f(0f, Oto.HEIGHT, 0f);
    private float maxThirdPersonValue = 100f;
    private final Oto oto;
    private boolean firstTime = true;
    private Picker picker;
    private Main main;
    private Vector3f camDodgeOffset = new Vector3f(0f, 0f, 0f);

    HiddenCursorState(Oto oto, InputManager inputManager) {
        super();
        this.oto = oto;
        this.inputManager = inputManager;
        character = oto.getPlayerControl();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        main = (Main) app;

        if (firstTime) {
            cam = main.getCamera();
            initialUpVec = cam.getUp().clone();
            firstTime = false;
            picker = main.playerManager.picker;
        }
        inputManager.addListener(this, mappings);
        inputManager.addListener(picker, KeyMapper.PLAYER_SHOOT, KeyMapper.PLAYER_SHOOT2);
        inputManager.addListener(picker, KeyMapper.PLAYER_PICK);
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        inputManager.setCursorVisible(false);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        inputManager.setCursorVisible(true);
        inputManager.removeListener(this);
        inputManager.removeListener(picker);

    }

    @Override
    public void update(float tpf) {

        if (oto.movement.isDodging()) {
            setCameraDodgeFactor(3f);
            oto.setGunDodgeFactor(3f);
        } else {
            setCameraDodgeFactor(0);
            oto.setGunDodgeFactor(0);

        }


        cam.setLocation(character.getPhysicsLocation().add(camOffset).add(camDodgeOffset));

    }

    public void onAnalog(String name, float value, float tpf) {

        if (isEnabled()) {

            if (name.equals(KeyMapper.MOUSE_LEFT)) {
                rotateCamera(value, initialUpVec);
            } else if (name.equals(KeyMapper.MOUSE_RIGHT)) {
                rotateCamera(-value, initialUpVec);
            } else if (name.equals(KeyMapper.MOUSE_UP)) {
                rotateCamera(-value * (invertY ? -1 : 1), cam.getLeft());
            } else if (name.equals(KeyMapper.MOUSE_DOWN)) {
                rotateCamera(value * (invertY ? -1 : 1), cam.getLeft());
            } else if (name.equals(KeyMapper.MOUSE_ZOOM_IN) && camOffset.getZ() < maxThirdPersonValue) {
                camOffset.addLocal(new Vector3f(0f, 0f, value));
            } else if (name.equals(KeyMapper.MOUSE_ZOOM_OUT) && camOffset.getZ() > 0) {
                camOffset.subtractLocal(new Vector3f(0f, 0f, value));
            }
        }

    }

    protected void rotateCamera(float value, Vector3f axis) {

        if (cam.getUp().y < 0) {
            cam.lookAtDirection(
                    new Vector3f(0, cam.getDirection().y, 0),
                    new Vector3f(cam.getUp().x,
                    0,
                    cam.getUp().z));
        }

        Matrix3f mat = new Matrix3f();
        mat.fromAngleNormalAxis(rotationSpeed * value, axis);

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        mat.mult(up, up);
        mat.mult(left, left);
        mat.mult(dir, dir);

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        cam.setAxes(q);

        character.setViewDirection(cam.getDirection());

        Quaternion b = new Quaternion();
        b.fromAxes(left, up, dir);
        b.normalizeLocal();

        oto.setGunRotation(b);
    }

    public void printVector(Vector3f g) {
        System.out.println("\t" + Math.round(g.x) + "\t" + Math.round(g.y) + "\t" + Math.round(g.z));
    }

    public void setCameraDodgeFactor(float height) {
        camDodgeOffset = new Vector3f(0, -height, 0);
    }
}
