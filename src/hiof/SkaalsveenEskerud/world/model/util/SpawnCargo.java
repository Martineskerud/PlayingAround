/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.world.model.util;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.model.Cargo;
import java.util.Random;

/**
 *
 * @author Martin
 */
public class SpawnCargo extends Node {
    
    private final AssetManager am;
    private final MassFactory mf;
    private final TextureDistributor td;
    private final Random rand;
    
    public SpawnCargo(AssetManager am, MassFactory mf, TextureDistributor td, Random rand) {
        name = "SpawnCargo";
        this.am = am;
        this.mf = mf;
        this.td = td;
        this.rand = rand;
        attachCargo();
    }
    
    public void mkCargo(String prefix, boolean closed, float rot, Vector3f... placements) {
        
        int i = 0;
        for (Vector3f vec : placements) {
            i++;
            if (rot == 0f) {
                attachChild(new Cargo(prefix + i, am, mf, td, closed, vec, rand));
            } else {
                attachChild(new Cargo(prefix + i, am, mf, td, closed, vec, rand, rot));
            }
        }
    }
    
    public void attachCargo() {

        //mkCargo("cargoOpen", false, 2f, new Vector3f(140, 6, 150), new Vector3f(120, 6, 110), new Vector3f(-170, 6, 225));
        //mkCargo("cargoOpen", false, 0f, new Vector3f(140, 6, 150), new Vector3f(120, 6, 110), new Vector3f(-170, 6, 225));

        Cargo cargo = new Cargo("cargo1", am, mf, td, true, new Vector3f(140, 6, 150), rand);
        Cargo cargo2 = new Cargo("cargo2", am, mf, td, false, new Vector3f(120, 6, 110), rand);
        Cargo cargo4 = new Cargo("cargo4", am, mf, td, false, new Vector3f(180, 27f, 150), rand);
        Cargo cargo5 = new Cargo("cargo5", am, mf, td, false, new Vector3f(-100, 6, 270), rand, 2f);
        Cargo cargo6 = new Cargo("cargo6", am, mf, td, false, new Vector3f(-100, 6, 350), rand, 2f);
        Cargo cargo7 = new Cargo("cargo7", am, mf, td, false, new Vector3f(-100, 6, 350), rand, 4f);
        Cargo cargo8 = new Cargo("cargo8", am, mf, td, false, new Vector3f(240, 6, 170), rand);
        Cargo cargo9 = new Cargo("cargo9", am, mf, td, false, new Vector3f(240, 27f, 170), rand);
        Cargo cargo10 = new Cargo("cargo10", am, mf, td, true, new Vector3f(-100, 6, 326), rand, 2f);
        Cargo cargo11 = new Cargo("cargo11", am, mf, td, true, new Vector3f(-260, 6, 225), rand, 2f);
        Cargo cargo12 = new Cargo("cargo12", am, mf, td, true, new Vector3f(700, 6, -60), rand);
        Cargo cargo13 = new Cargo("cargo13", am, mf, td, true, new Vector3f(250, 6, 135), rand);
        Cargo cargo14 = new Cargo("cargo14", am, mf, td, true, new Vector3f(450, 6, 135), rand);
        Cargo cargo15 = new Cargo("cargo15", am, mf, td, false, new Vector3f(-100, 6, 500), rand, 2f);
        Cargo cargo16 = new Cargo("cargo16", am, mf, td, false, new Vector3f(470, 6, 180), rand);
        Cargo cargo17 = new Cargo("cargo17", am, mf, td, false, new Vector3f(-80, 6, 550), rand, 2f);
        Cargo cargo18 = new Cargo("cargo18", am, mf, td, false, new Vector3f(-120, 6, 590), rand, 2f);
        Cargo cargo19 = new Cargo("cargo19", am, mf, td, false, new Vector3f(550, 27f, 80), rand);
        Cargo cargo20 = new Cargo("cargo20", am, mf, td, false, new Vector3f(490, 27f, 80), rand);
        Cargo cargo21 = new Cargo("cargo21", am, mf, td, false, new Vector3f(100, 6, 0), rand, 4f);
        Cargo cargo22 = new Cargo("cargo22", am, mf, td, false, new Vector3f(-50, 6f, 650), rand, 2f);
        Cargo cargo23 = new Cargo("cargo23", am, mf, td, true, new Vector3f(460, 6, -50), rand);
        Cargo cargo24 = new Cargo("cargo24", am, mf, td, true, new Vector3f(650, 6, 100), rand);
        Cargo cargo25 = new Cargo("cargo25", am, mf, td, false, new Vector3f(0, 6, 300), rand, 2f);
        Cargo cargo26 = new Cargo("cargo26", am, mf, td, true, new Vector3f(400, 6, 20), rand);
        Cargo cargo27 = new Cargo("cargo27", am, mf, td, false, new Vector3f(-30, 6, 200), rand, 2f);
        Cargo cargo28 = new Cargo("cargo28", am, mf, td, true, new Vector3f(30, 6, 350), rand, 2f);
        Cargo cargo29 = new Cargo("cargo29", am, mf, td, true, new Vector3f(605, 6, 300), rand);
        Cargo cargo30 = new Cargo("cargo30", am, mf, td, true, new Vector3f(550, 6, 350), rand);
        Cargo cargo31 = new Cargo("cargo31", am, mf, td, false, new Vector3f(600, 27.5f, 300), rand);
        Cargo cargo32 = new Cargo("cargo32", am, mf, td, false, new Vector3f(450, 6, 250), rand);
        Cargo cargo33 = new Cargo("cargo33", am, mf, td, false, new Vector3f(-200, 6, 650), rand, 2);
        Cargo cargo34 = new Cargo("cargo34", am, mf, td, false, new Vector3f(650, 48, 300), rand);
        Cargo cargo35 = new Cargo("cargo35", am, mf, td, false, new Vector3f(-300, 6, 680), rand, 2);
        Cargo cargo36 = new Cargo("cargo36", am, mf, td, true, new Vector3f(-300, 27, 680), rand, 2);
        Cargo cargo37 = new Cargo("cargo37", am, mf, td, true, new Vector3f(540, 6, 300), rand);
        Cargo cargo38 = new Cargo("cargo38", am, mf, td, false, new Vector3f(537, 27, 300), rand);
        Cargo cargo39 = new Cargo("cargo39", am, mf, td, false, new Vector3f(400, 6, 400), rand);
        Cargo cargo40 = new Cargo("cargo40", am, mf, td, true, new Vector3f(550, 6, 200), rand);
        Cargo cargo41 = new Cargo("cargo41", am, mf, td, false, new Vector3f(550, 27, 200), rand);
        Cargo cargo42 = new Cargo("cargo42", am, mf, td, false, new Vector3f(480, 48, 310), rand);
        Cargo cargo43 = new Cargo("cargo43", am, mf, td, true, new Vector3f(-270, 48, 680), rand, 2);
        Cargo cargo44 = new Cargo("cargo44", am, mf, td, true, new Vector3f(-200, 27, 650), rand, 2);
        Cargo cargo45 = new Cargo("cargo45", am, mf, td, false, new Vector3f(-200, 48, 650), rand, 2);
        Cargo cargo46 = new Cargo("cargo7", am, mf, td, false, new Vector3f(590, 69, -320), rand, -4f);
        attachChild(cargo);
        attachChild(cargo2);
        attachChild(cargo4);
        attachChild(cargo5);
        attachChild(cargo6);
        attachChild(cargo7);
        attachChild(cargo8);
        attachChild(cargo9);
        attachChild(cargo10);
        attachChild(cargo11);
        attachChild(cargo12);
        attachChild(cargo13);
        attachChild(cargo14);
        attachChild(cargo15);
        attachChild(cargo16);
        attachChild(cargo17);
        attachChild(cargo18);
        attachChild(cargo19);
        attachChild(cargo20);
        attachChild(cargo21);
        attachChild(cargo22);
        attachChild(cargo23);
        attachChild(cargo24);
        attachChild(cargo25);
        attachChild(cargo26);
        attachChild(cargo27);
        attachChild(cargo28);
        attachChild(cargo29);
        attachChild(cargo30);
        attachChild(cargo31);
        attachChild(cargo32);
        attachChild(cargo33);
        attachChild(cargo34);
        attachChild(cargo35);
        attachChild(cargo36);
        attachChild(cargo37);
        attachChild(cargo38);
        attachChild(cargo39);
        attachChild(cargo40);
        attachChild(cargo41);
        attachChild(cargo42);
        attachChild(cargo43);
        attachChild(cargo44);
        attachChild(cargo45);
        attachChild(cargo46);
    }
}
