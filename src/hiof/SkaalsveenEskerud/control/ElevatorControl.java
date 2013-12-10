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
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author martin
 *
 * this whole class needs a rewrite and suffers from the old elevator not being
 * implemented. There were too many issues with the previous elevator some of
 * these issues were hacked to get around, and by simplifying the class again,
 * the hacks were then re-hacked. The first elevator control was also
 * implemented before we learned more of animation, the issues could be easily
 * cirumvented with our knowledge now.
 *
 * Currently the only use here is to open the top elevator doors.
 *
 */
public class ElevatorControl extends AbstractControl implements ActionListener, AnimEventListener {

    private HashMap<String, Animation> animationMap;
    private ArrayList<Spatial> spatials;
    private ArrayList<Node> nodes;
    InputManager im;
    private boolean elevatorUp;
    private boolean elevatorMoving;
    private float fieldTpf;

    public ElevatorControl(ArrayList<Spatial> inputSpatials, ArrayList<Node> inputNodes, boolean runAnim, InputManager im) {
        animationMap = new HashMap<String, Animation>();
        this.spatials = inputSpatials;
        this.nodes = inputNodes;
        this.im = im;
        elevatorUp = false;
        elevatorMoving = false;

    }
    /*the animation still used*/

    public void bringElevatorUp() {

        //move the elevator floor
        AnimationFactory af3 = new AnimationFactory(250 * fieldTpf, "BringElevatorUp", 30);
        Quaternion deg90 = new Quaternion();
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));

        Node insideElevatorNode = nodes.get(0);
        Vector3f insideElevatorLoc = insideElevatorNode.getLocalTranslation();

        af3.addTimeTranslation(0, insideElevatorLoc);
        af3.addTimeTranslation(250 * fieldTpf, new Vector3f(insideElevatorLoc.x, insideElevatorLoc.y + 176, insideElevatorLoc.z));

        animationMap.put("BringElevatorUp", af3.buildAnimation());
        AnimControl ac3 = new AnimControl();
        AnimChannel animChannel3 = ac3.createChannel();
        ac3.setAnimations(animationMap);
        animChannel3.setAnim("BringElevatorUp");
        animChannel3.setLoopMode(LoopMode.DontLoop);
        //insideElevatorNode.addControl(ac3);
        spatials.get(1).addControl(ac3);
        //move left door

        AnimationFactory af = new AnimationFactory(250 * fieldTpf, "LeftDoor", 30);
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));
        af.addTimeRotation(0, deg90);
        af.addTimeRotation(250 * fieldTpf, deg90);

        af.addTimeTranslation(0, spatials.get(0).getLocalTranslation());
        af.addTimeTranslation(187.5f * fieldTpf, new Vector3f(spatials.get(0).getLocalTranslation().getX(), spatials.get(0).getLocalTranslation().getY(), spatials.get(0).getLocalTranslation().getZ() + 20));
        af.addTimeTranslation(250 * fieldTpf, spatials.get(0).getLocalTranslation());

        animationMap.put("LeftDoor", af.buildAnimation());
        AnimControl ac = new AnimControl();
        AnimChannel animChannel = ac.createChannel();
        ac.setAnimations(animationMap);
        ac.addListener(this);
        animChannel.setAnim("LeftDoor");
        animChannel.setLoopMode(LoopMode.DontLoop);
        spatials.get(0).addControl(ac);

        //move right door
        AnimationFactory af2 = new AnimationFactory(250 * fieldTpf, "RightDoor", 30);
        af2.addTimeRotation(0, deg90);
        af2.addTimeRotation(250 * fieldTpf, deg90);

        af2.addTimeTranslation(0, spatials.get(1).getLocalTranslation());
        // af2.addTimeTranslation(timeToTop - 2, spatials.get(1).getLocalTranslation());
        af2.addTimeTranslation(187.5f * fieldTpf, new Vector3f(spatials.get(1).getLocalTranslation().getX(), spatials.get(1).getLocalTranslation().getY(), spatials.get(1).getLocalTranslation().getZ() - 20));
        af2.addTimeTranslation(250 * fieldTpf, spatials.get(1).getLocalTranslation());
        animationMap.put("RightDoor", af2.buildAnimation());
        AnimControl ac2 = new AnimControl();
        AnimChannel animChannel2 = ac2.createChannel();
        ac2.setAnimations(animationMap);
        ac2.addListener(this);
        animChannel2.setAnim("RightDoor");
        animChannel2.setLoopMode(LoopMode.DontLoop);
        spatials.get(1).addControl(ac2);
        elevatorUp = true;
        elevatorMoving = false;
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("LeftDoorUnderground")) {
            elevatorUp = false;
            elevatorMoving = false;
        }
        if (animName.equals("RightDoor")) {
            elevatorUp = true;
            elevatorMoving = false;
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        System.out.println("im a listener too ");
    }

    /*Deprecated*/
    public void bringElevatorDown() {
        im.addListener(this, "BringElevatorDown");
        elevatorMoving = true;
        //HACK to get open elevator doors when elevator reaches top.
        //loat animationTime = (FastMath.abs((nodes.get(0).getLocalTranslation().getY()) / 30));
        float animationTime = 4f;
        if (animationTime == 0.0f) {
            animationTime = (FastMath.abs(180 / 35));
        }

        //adding some delay to the time.
        animationTime++;

        //move the elevator floor
        AnimationFactory af3 = new AnimationFactory(4f, "BringElevatorDown", 30);
        Quaternion deg90 = new Quaternion();
        //TODO: create some kind of measurement of how long there is to the top to fix the elevator moving at random pace.
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));

        Node insideElevatorNode = nodes.get(0);
        Vector3f insideElevatorLoc = insideElevatorNode.getLocalTranslation();

        af3.addTimeTranslation(0, insideElevatorLoc);
        af3.addTimeTranslation(4f, new Vector3f(insideElevatorLoc.x, insideElevatorLoc.y - 176, insideElevatorLoc.z));

        animationMap.put("BringElevatorDown", af3.buildAnimation());
        AnimControl ac3 = new AnimControl();
        AnimChannel animChannel3 = ac3.createChannel();
        ac3.setAnimations(animationMap);
        animChannel3.setAnim("BringElevatorDown");
        animChannel3.setLoopMode(LoopMode.DontLoop);

        insideElevatorNode.addControl(ac3);

        //move left door

        AnimationFactory af = new AnimationFactory(animationTime, "LeftDoorUnderground", 30);
        deg90.fromAngleAxis(FastMath.PI / 2, new Vector3f(0f, 1f, 0f));
        af.addTimeRotation(0, deg90);
        af.addTimeRotation(animationTime, deg90);

        System.out.println("OpenAnimationTime=" + animationTime);
        af.addTimeTranslation(0, spatials.get(2).getLocalTranslation());
        //af.addTimeTranslation(animationTime - 2, spatials.get(2).getLocalTranslation());
        af.addTimeTranslation(animationTime - 1, new Vector3f(spatials.get(2).getLocalTranslation().getX(), spatials.get(2).getLocalTranslation().getY(), spatials.get(2).getLocalTranslation().getZ() + 20));
        af.addTimeTranslation(animationTime, spatials.get(2).getLocalTranslation());

        animationMap.put("LeftDoorUnderground", af.buildAnimation());
        AnimControl ac = new AnimControl();
        AnimChannel animChannel = ac.createChannel();
        ac.setAnimations(animationMap);
        animChannel.setAnim("LeftDoorUnderground");
        animChannel.setLoopMode(LoopMode.DontLoop);
        ac.addListener(this);
        spatials.get(2).addControl(ac);

        //move right door   
        AnimationFactory af2 = new AnimationFactory(animationTime, "RightDoorUnderground", 30);
        af2.addTimeRotation(0, deg90);
        af2.addTimeRotation(animationTime, deg90);

        af2.addTimeTranslation(0, spatials.get(3).getLocalTranslation());
        //af2.addTimeTranslation(animationTime - 2, spatials.get(3).getLocalTranslation());
        af2.addTimeTranslation(animationTime - 1, new Vector3f(spatials.get(3).getLocalTranslation().getX(), spatials.get(3).getLocalTranslation().getY(), spatials.get(3).getLocalTranslation().getZ() - 20));
        af2.addTimeTranslation(animationTime, spatials.get(3).getLocalTranslation());
        animationMap.put("RightDoorUnderground", af2.buildAnimation());
        AnimControl ac2 = new AnimControl();
        AnimChannel animChannel2 = ac2.createChannel();
        ac2.setAnimations(animationMap);
        animChannel2.setAnim("RightDoorUnderground");
        animChannel2.setLoopMode(LoopMode.DontLoop);
        spatials.get(3).addControl(ac2);

    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed && name.equals(KeyMapper.BRING_ELEVATOR_UP)) {

            if (!elevatorMoving) {
                if (elevatorUp == true) {
                    bringElevatorDown();
                }

                if (elevatorUp == false) {
                    bringElevatorUp();
                }
            }
        }

    }

    @Override
    protected void controlUpdate(float tpf) {
        fieldTpf = tpf;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
