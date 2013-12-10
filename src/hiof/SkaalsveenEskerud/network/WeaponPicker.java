package hiof.SkaalsveenEskerud.network;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import hiof.SkaalsveenEskerud.control.LogBomb;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.picking.Picker;
import hiof.SkaalsveenEskerud.sound.GunAudio;
import hiof.SkaalsveenEskerud.weapon.AmmoManager;
import hiof.SkaalsveenEskerud.world.World;
import hiof.SkaalsveenEskerud.world.model.util.MaterialDistributor;
import java.util.HashMap;

/**
 *
 * @author anderssk
 */
public abstract class WeaponPicker extends Picker {

    private final World world;
    private final HashMap<String, HitListener> pickHashMap;
    private Geometry bulletHole;
    private final AssetManager assetManager;
    private final AmmoManager ammoManager;
    private long triggerDelay;
    private long lastPick = 0;

    public WeaponPicker(PlayerManager playerManager) {
        super(playerManager.getRootNode(), playerManager.getCamera());

        this.ammoManager = playerManager.ammoManager;

        this.world = playerManager.getWorld();
        this.assetManager = playerManager.getAssetManager();
        pickHashMap = new HashMap<String, HitListener>();

        createImpactSphere();
    }

    @Override
    protected void pick(String name) {
        //Shoot MOUSEBUTTON
        if (name.equals(KeyMapper.PLAYER_SHOOT) && ammoManager.getMagazineCount() > 0) {
            if ((System.currentTimeMillis() - lastPick) > triggerDelay) {
                lastPick = System.currentTimeMillis();
                super.pick(name);
                onAction();
                createShootingSound();

                /*
                 * MUZZLEFLASH GOES HERE
                 */

            } else {
                System.out.println("Too fast picking for triggerDelay. :) You very fast!");
            }

        } //Pick 'E' 
        else if (name.equals(KeyMapper.PLAYER_PICK)) {

            super.pick(name);

        } else if (!name.equals(KeyMapper.PLAYER_SHOOT2)) {
            createGunEmptySound();
            onFailDueToEmptyMagazine();
        }
    }

    @Override
    public void onHit(Geometry geom, Vector3f contactPoint, String triggerKey) {
        String name = geom.getName();

        if (pickHashMap.containsKey(name)) {
            pickHashMap.get(name).onHit(geom, name, triggerKey);
            return;

        }


        if (triggerKey.equals(KeyMapper.PLAYER_SHOOT)) {

            if (geom.hasAncestor(world.characters)) {
                onPlayerHit(getPlayerName(geom.getParent()), triggerKey);
                return;
            }
            mkLove(ColorRGBA.Red, name, geom, contactPoint, "I Love You <3");
            return;
        }


        mkLove(ColorRGBA.Green, name, geom, contactPoint, "Pick Me! :D");
    }

    public Quaternion getRotationVectorFromCamera(Vector3f gv) {

        Quaternion q = new Quaternion();
        final Vector3f left = new Vector3f(gv.x, 0f, 0f);
        final Vector3f up = new Vector3f(0f, gv.y, 0f);
        final Vector3f dir = new Vector3f(0f, 0f, gv.z);

        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        return q;

    }

    public Quaternion getRotationVectorFromCamera() {

        Vector3f up = cam.getUp();
        Vector3f left = cam.getLeft();
        Vector3f dir = cam.getDirection();

        Quaternion q = new Quaternion();
        q.fromAxes(left, up, dir);
        q.normalizeLocal();

        return q;

    }

    public void setTriggerDelay(long i) {
        triggerDelay = i;
    }

    private String getPlayerName(Node g) {

        Node parent = g.getParent();
        if (parent != null) {
            String pName = parent.getName();
            if (pName != null && pName.equals("characters")) {
                return g.getName();
            } else {
                return getPlayerName(parent);
            }
        } else {
            return null;
        }

    }

    public void setHitListener(String name, HitListener l) {
        pickHashMap.put(name, l);
    }

    @Override
    public void onMiss(String name) {
    }

    private void mapNode(Geometry geom) {

        String identing = "";
        Node g = geom.getParent();

        System.out.println(g.getName());
        int i = 0;
        while (g.getParent() != null) {
            identing += " ";
            g = g.getParent();
            if (i++ > 10) {
                break;
            }
            System.out.println(identing + "+ " + g.getName());
        }
    }

    protected abstract void onPlayerHit(String playerName, String triggerKey);

    public abstract void onAction();

    public abstract void onFailDueToEmptyMagazine();

    private void createShootingSound() {
        GunAudio gunAudio = new GunAudio(assetManager, GunAudio.RIFLE);
        gunAudio.playAudio();
    }

    private void createGunEmptySound() {
        GunAudio gunAudio = new GunAudio(assetManager, GunAudio.EMPTY);
        gunAudio.playAudio();
    }

    private void createImpactSphere() {

        Sphere mesh = new Sphere(32, 32, 2f);
        bulletHole = new Geometry("impact indicator", mesh);
        Material mat = new Material(assetManager, MaterialDistributor.PATH_MY_TEST_SHADER);
        mat.setInt("Time", (int) (System.currentTimeMillis() / 1000));
        bulletHole.setQueueBucket(Bucket.Transparent);
        bulletHole.setMaterial(mat);
    }

    protected void mkLove(ColorRGBA color, String name, Geometry geom, Vector3f contactPoint, String spam) {
        System.out.println("geom is:" + name);

        final Quaternion worldRotation = geom.getWorldRotation();

        LogBomb bomb = new LogBomb(assetManager, spam, color);
        Node n = bomb.getNode();
        //n.setLocalRotation(worldRotation);
        n.setLocalTranslation(contactPoint);
        n.setQueueBucket(Bucket.Transparent);
        n.attachChild(bomb.mkQuad(color));

        rootNode.attachChild(n);
        mapNode(geom);
    }
}
