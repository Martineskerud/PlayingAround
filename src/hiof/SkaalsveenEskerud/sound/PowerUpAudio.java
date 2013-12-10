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
public class PowerUpAudio extends Node {

    private final AssetManager am;
    private AudioNode audio_gun;

    public PowerUpAudio(AssetManager am) {
        name = "sound";
        this.am = am;
        audio();

    }

    private void audio() {
        audio_gun = new AudioNode(am, "Sounds/powerup.wav", false);
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
