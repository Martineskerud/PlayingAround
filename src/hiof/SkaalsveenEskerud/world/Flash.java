/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author martin
 */
public class Flash extends Node {

    private final AssetManager am;
    private final Vector3f position;

    public Flash(AssetManager am, Vector3f position) {
        this.am = am;
        this.position = position;
        createFlash();

    }

    private void createFlash() {
        ParticleEmitter flash = new ParticleEmitter("muzzleflash", ParticleMesh.Type.Triangle, 30);
        flash.setLocalTranslation(position);
        Material flash_mat = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        flash_mat.setTexture("Texture", am.loadTexture("Effects/Explosion/flash.png"));
        flash.setMaterial(flash_mat);
        flash.setImagesX(2); // columns
        flash.setImagesY(2); // rows
        flash.setNumParticles(20);
        flash.setSelectRandomImage(true);
        attachChild(flash);
    }
}
