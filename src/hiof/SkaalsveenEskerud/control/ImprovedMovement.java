/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.control;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import hiof.SkaalsveenEskerud.character.Oto;
import hiof.SkaalsveenEskerud.input.KeyMapper;

/**
 *
 * @author martin
 */
public class ImprovedMovement implements ActionListener, AnimEventListener, EvenBetterCharacterControl.JumpListener {

    AnimControl animationControl;
    AnimChannel animationChannel;
    AnimChannel topBodyChannel;
    AnimChannel lowerBodyChannel;
    private Node model;
    private final Oto meOto;
    private boolean backward;
    private boolean forward;
    private boolean right;
    private boolean left;
    private boolean inAir;
    private boolean dodge;

    public ImprovedMovement(Oto me) {
        this.meOto = me;
        initMovement();
    }

    private void initMovement() {

        model = (Node) meOto.getChild("Oto-ogremesh");
        animationControl = model.getControl(AnimControl.class);
        animationControl.addListener(this);
        animationChannel = animationControl.createChannel();
        topBodyChannel = animationControl.createChannel();
        lowerBodyChannel = animationControl.createChannel();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals(KeyMapper.MOVE_BACKWARD)) {
            backward = isPressed;
        } else if (name.equals(KeyMapper.MOVE_FORWARD)) {
            forward = isPressed;
        } else if (name.equals(KeyMapper.MOVE_RIGHT)) {
            right = isPressed;
        } else if (name.equals(KeyMapper.MOVE_LEFT)) {
            left = isPressed;
        } else if (name.equals(KeyMapper.PLAYER_DODGE)) {
            dodge = isPressed;
        }


        String animation = (!inAir && isMoving() ? "walkGun" : "stand");
        if (!dodge) {
            resetChannels();
            setAnim(animation, LoopMode.Loop, 0.1f * tpf);
        } else if (dodge && anyMovement()) {
            resetChannels();
            dodgeAnim();
            walkAnim();
        } else if (dodge) {
            resetChannels();
            dodgeAnim();
            //setAnim(animation, LoopMode.DontLoop, tpf);
        }

    }

    private void setAnim(String animName, LoopMode loopMode, float time) {

        animationChannel.setAnim(animName, 0.3f);
        animationChannel.setSpeed(time * 2000);
        if (animationChannel.getLoopMode() != loopMode) {
            animationChannel.setLoopMode(loopMode);
        }
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }

    public void onJumpStateChange(boolean inAir, float tpf) {
        this.inAir = inAir;

        if (inAir) {
            resetChannels();
            setAnim("stand", LoopMode.Loop, 0.1f * tpf);
        } else if (isMoving()) {
            resetChannels();
            setAnim("walkGun", LoopMode.Loop, 0.1f * tpf);
        }
    }

    void resetChannels() {

        animationChannel.reset(true);
        lowerBodyChannel.reset(true);
        topBodyChannel.reset(true);
    }

    private void dodgeAnim() {

        topBodyChannel.addBone("head");
        topBodyChannel.addBone("arm.right");
        topBodyChannel.addBone("arm.left");
        topBodyChannel.addBone("hand.right");
        topBodyChannel.addBone("hand.left");
        topBodyChannel.addBone("spinehigh");
        topBodyChannel.addBone("uparm.right");
        topBodyChannel.addBone("uparm.left");
        topBodyChannel.setAnim("dodge", 2f);
        topBodyChannel.setLoopMode(LoopMode.DontLoop);
    }

    private void walkAnim() {

        lowerBodyChannel.addBone("spine");
        lowerBodyChannel.addBone("hip.right");
        lowerBodyChannel.addBone("hip.left");
        lowerBodyChannel.addBone("leg.right");
        lowerBodyChannel.addBone("leg.left");
        lowerBodyChannel.setAnim("walk", 2f);
        lowerBodyChannel.setLoopMode(LoopMode.Loop);
    }

    private boolean isMoving() {
        return backward || forward || right || left;
    }

    private boolean anyMovement() {
        if (backward || forward || right || left) {
            return true;
        }
        return false;
    }

    public boolean isDodging() {
        return dodge;
    }
}
