/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Martin
 */
public class Fire extends Node {

    private final AssetManager am;
    private final Vector3f position;
    private final String type;

    public Fire(AssetManager am, Vector3f position, String type) {
        this.am = am;
        this.type = type;
        this.position = position;
        chooseAction(type);
    }

    private void chooseAction(String type) {
        if (type == "fire") {
            ignite();
        } else if (type == "explosion") {
            explode();
        }
    }

    public void ignite() {

        ParticleEmitter fire =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", am.loadTexture(
                "Effects/Explosion/flame.png"));
        fire.setShape(new EmitterSphereShape(new Vector3f(0, 0, 0), 4f));
        fire.setMaterial(mat_red);

        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(4f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setLocalTranslation(position);
        attachChild(fire);



    }

    private void explode() {
        ParticleEmitter fire =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", am.loadTexture(
                "Effects/Explosion/flame.png"));
        fire.setShape(new EmitterSphereShape(new Vector3f(0, 0, 0), 4f));
        fire.setMaterial(mat_red);

        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 1f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.8f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(1.5f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(4f);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);
        fire.setLocalTranslation(position);
        attachChild(fire);

    }
}
