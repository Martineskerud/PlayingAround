/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.input;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 *
 * @author anskaal
 *
 * A good place for declaring input keys...
 */
public class KeyMapper {

    public static final String SERVER_START = "Start Server";
    public static final String SERVER_START_PAD = "Start Client Pad";
    public static final String CLIENT_START = "Start Client";
    public static final String SHOW_PLAYER_LIST = "Show Player List";
    public static final String SHOW_CHAT = "Show Chat";
    public static final String HIDE_CHAT = "Hide Chat";
    public static final String CHAT_SEND = "Chat Send";
    public static final String BRING_ELEVATOR_UP = "Bring Elevator Up";
    public static final String BRING_ELEVATOR_DOWN = "Bring Elevator Down";
    public static final String PLAYER_SHOOT = "Shoot";
    public static final String PLAYER_SHOOT2 = "Shoot2";
    public static final String PLAYER_PICK = "Player Pick";
    public static final String MOVE_FORWARD = "Move Forward";
    public static final String MOVE_BACKWARD = "Move Backward";
    public static final String MOVE_LEFT = "Move Left";
    public static final String MOVE_RIGHT = "Move Right";
    public static final String MOVE_FAST = "Move Fast";
    public static final String PLAYER_JUMP = "Player Jump";
    public static final String PLAYER_DODGE= "Player Dodge";
    public static final String EQUIP_WEAPON_1 = "Equip Weapon 1";
    public static final String EQUIP_WEAPON_2 = "Equip Weapon 2";
    public static final String RELOAD_WEAPON = "Reload Weapon";
    public static final String MOUSE_LEFT = "FPS_CAM_LEFT";
    public static final String MOUSE_RIGHT = "FPS_CAM_RIGHT";
    public static final String MOUSE_UP = "FPS_CAM_UP";
    public static final String MOUSE_DOWN = "FPS_CAM_DOWN";
    public static final String MOUSE_ZOOM_IN = "FPS_CAM_ZOOM_IN";
    public static final String MOUSE_ZOOM_OUT = "FPS_CAM_ZOOM_OUT";

    public static final void setup(InputManager im) {

        im.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        im.addMapping(SimpleApplication.INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_F10));


        // Server and client
        //im.addMapping(SERVER_START, new KeyTrigger(KeyInput.KEY_X));
        //im.addMapping(CLIENT_START, new KeyTrigger(KeyInput.KEY_C));
        //im.addMapping(SERVER_START_PAD, new KeyTrigger(KeyInput.KEY_P)); 

        // UI
        im.addMapping(SHOW_PLAYER_LIST, new KeyTrigger(KeyInput.KEY_TAB));
        im.addMapping(SHOW_CHAT, new KeyTrigger(KeyInput.KEY_T));
        im.addMapping(HIDE_CHAT, new KeyTrigger(KeyInput.KEY_ESCAPE));
        im.addMapping(CHAT_SEND, new KeyTrigger(KeyInput.KEY_RETURN));

        //Gameplay  
        im.addMapping(BRING_ELEVATOR_UP, new KeyTrigger((KeyInput.KEY_F1)));
        im.addMapping(BRING_ELEVATOR_DOWN, new KeyTrigger((KeyInput.KEY_F2)));
        im.addMapping(EQUIP_WEAPON_1, new KeyTrigger((KeyInput.KEY_1)));
        //im.addMapping(EQUIP_WEAPON_2, new KeyTrigger((KeyInput.KEY_2)));
        im.addMapping(RELOAD_WEAPON, new KeyTrigger((KeyInput.KEY_R)));
        im.addMapping(MOVE_FORWARD, new KeyTrigger((KeyInput.KEY_W)));
        im.addMapping(MOVE_LEFT, new KeyTrigger((KeyInput.KEY_A)));
        im.addMapping(MOVE_BACKWARD, new KeyTrigger((KeyInput.KEY_S)));
        im.addMapping(MOVE_RIGHT, new KeyTrigger((KeyInput.KEY_D)));
        im.addMapping(PLAYER_JUMP, new KeyTrigger(KeyInput.KEY_SPACE));
        im.addMapping(MOVE_FAST, new KeyTrigger(KeyInput.KEY_LSHIFT));
        im.addMapping(PLAYER_DODGE, new KeyTrigger(KeyInput.KEY_LCONTROL));
        // Picking
        im.addMapping(PLAYER_SHOOT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        im.addMapping(PLAYER_SHOOT2, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        im.addMapping(PLAYER_PICK, new KeyTrigger((KeyInput.KEY_E)));


        // both mouse and button - rotation of cam
        im.addMapping(MOUSE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));
        im.addMapping(MOUSE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        im.addMapping(MOUSE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));
        im.addMapping(MOUSE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));
        im.addMapping(MOUSE_ZOOM_IN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true),
                new KeyTrigger(KeyInput.KEY_ADD));
        im.addMapping(MOUSE_ZOOM_OUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false),
                new KeyTrigger(KeyInput.KEY_SUBTRACT));


    }
}
