/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.sound;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;

/**
 *
 * @author Martin
 */
public class GunAudio extends Node {

    private final AssetManager am;
    private AudioNode audio_gun;
    private final String sound;
    public final static String RELOAD = "Sounds/reload.wav";
    public final static String RIFLE = "Sounds/rifle.wav";
    public final static String DEATH = "Sounds/death.wav";
    public final static String POWERUP = "Sounds/powerup.wav";
    public final static String SPAWN = "Sounds/spawn.wav";
    public final static String EMPTY = "Sounds/empty.wav";
    public final static String GLASS = "Sounds/glass.wav";

    public GunAudio(AssetManager am, String sound) {
        name = "gun";
        this.am = am;
        this.sound = sound;
        audio();
    }

    private void audio() {
        audio_gun = new AudioNode(am, sound, false);
        audio_gun.setPositional(false);
        audio_gun.setLooping(false);
        audio_gun.setVolume(2);
        attachChild(audio_gun);
    }

    public void playAudio() {
        audio_gun.stop();
        audio_gun.play();
    }
}
