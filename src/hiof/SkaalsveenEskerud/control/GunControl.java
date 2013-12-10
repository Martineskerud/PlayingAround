/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.Animation;
import com.jme3.animation.AnimationFactory;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.network.WeaponPicker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author martin
 */
public class GunControl extends AbstractControl implements ActionListener, AnimEventListener {

    private HashMap<String, Animation> animationMap;
    private boolean currentlyChanging = false;
    private InputManager im;
    private static String IDLE_WEAPON = "idleweapon";
    private static String LOWER_WEAPON = "lowerweapon";
    private static String RAISE_WEAPON = "raiseweapon";
    private Main main;
    private Node rootNode;
    private AssetManager assetManager;
    private Node characterNode;
    private final ArrayList<BetterAnimEventListener> listeners;
    private long timeStamp;
    private float fieldTpf;

    public GunControl(Node rootNode, InputManager im, Main main, Node characterNode) {
        this.im = im;
        animationMap = new HashMap<String, Animation>();
        this.main = main;
        this.rootNode = rootNode;
        this.characterNode = characterNode;
        listeners = new ArrayList<BetterAnimEventListener>();
        timeStamp = System.currentTimeMillis();
    }

    public void changeWeapon(Spatial weapon) {

        currentlyChanging = true;

        System.out.println("im changing the weapon now");
        System.out.println(fieldTpf);
        AnimationFactory af = new AnimationFactory(125f * fieldTpf, LOWER_WEAPON, 30/* *tps*/);
        Vector3f weaponPos = weapon.getLocalTranslation();
        af.addTimeTranslation(0f, weaponPos);
        af.addTimeTranslation(62.5f * fieldTpf, new Vector3f(weaponPos.x, weaponPos.y - 3, weaponPos.z - 3));
        af.addTimeTranslation(125f * fieldTpf, new Vector3f(weaponPos));

        animationMap.put("lowerweapon", af.buildAnimation());
        AnimControl ac = new AnimControl();
        AnimChannel animChannel = ac.createChannel();
        ac.setAnimations(animationMap);
        animChannel.setAnim("lowerweapon");
        animChannel.setLoopMode(LoopMode.DontLoop);
        ac.addListener(this);
        for (BetterAnimEventListener ael : listeners) {
            ac.addListener(ael);
            ael.onStart();
        }

        weapon.addControl(ac);

    }

    @Override
    protected void controlUpdate(float tpf) {
        fieldTpf = tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public interface BetterAnimEventListener extends AnimEventListener {

        public void onStart();
    }

    public void weaponIdle(Spatial weapon) {

        System.out.println("this weapon is idle");
        AnimationFactory af = new AnimationFactory(2f, IDLE_WEAPON, 30/* *tps*/);
        Vector3f weaponPos = weapon.getLocalTranslation();
        af.addTimeTranslation(0f, weaponPos);
        af.addTimeTranslation(1f, new Vector3f(weaponPos.x, weaponPos.y - 3, weaponPos.z - 3));
        af.addTimeTranslation(2f, new Vector3f(weaponPos));

        animationMap.put(IDLE_WEAPON, af.buildAnimation());
        AnimControl ac = new AnimControl();
        AnimChannel animChannel = ac.createChannel();
        ac.setAnimations(animationMap);
        animChannel.setAnim(IDLE_WEAPON);
        animChannel.setLoopMode(LoopMode.DontLoop);
        ac.addListener(this);
        weapon.addControl(ac);
    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (isPressed && name.equals(KeyMapper.RELOAD_WEAPON)) {
            long tempTime = System.currentTimeMillis();

            if (tempTime - 3000 > timeStamp) {
                timeStamp = tempTime;
                final List<Spatial> handChildren = characterNode.getChildren();
                if (handChildren.size() > 0) {

                    for (Spatial c : handChildren) {
                        changeWeapon(c);
                    }
                }
            }

        }
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

        if (animName.equals(LOWER_WEAPON)) {
            currentlyChanging = false;
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        System.out.println("im calling the gun controlUpdate");
    }

    public void addAnimEventListener(BetterAnimEventListener listener) {
    }
}
