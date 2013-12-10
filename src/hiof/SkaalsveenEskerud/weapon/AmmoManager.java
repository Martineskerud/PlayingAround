package hiof.SkaalsveenEskerud.weapon;

import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;
import hiof.SkaalsveenEskerud.control.PowerUpCollisionControl.MyCollisionListener;
import hiof.SkaalsveenEskerud.control.PowerUpControl;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author anskaal
 */
public class AmmoManager implements ActionListener, MyCollisionListener {

    public static final int PISTOL = 1;
    public static final int ROCKET_LAUNCHER = 2;
    private int current = PISTOL;
    private final ArrayList<AmmoChangedListener> listeners;
    private final HashMap<Integer, Integer> totalBulletCount, allowedBulletsCount, ammoBuffer;

    public AmmoManager() {
        super();
        listeners = new ArrayList<AmmoChangedListener>();

        totalBulletCount = new HashMap<Integer, Integer>();

        allowedBulletsCount = new HashMap<Integer, Integer>();
        allowedBulletsCount.put(PISTOL, 10);
        allowedBulletsCount.put(ROCKET_LAUNCHER, 5);

        ammoBuffer = new HashMap<Integer, Integer>();
        setupStartAmmo();
        onAmmoCountChanged();
    }

    /**
     * Reloads the weapon.
     * 
     */
    public void reload() {
        final int magSize = getMagSize(current);
        final int magazineCount = getMagazineCount(current);
        
        if (magazineCount < magSize) { // can to reload
            
            if(magazineCount > getTotalBulletCount(current)){
                int bullets = magSize - magazineCount;
                moveBulletsToBuffer(current, bullets);
            }
            else{
                
                moveBulletsToBuffer(current, getTotalBulletCount(current));
            }

        }
    }

    /**
     * get the total amount of bullets except the ones in the magazine.
     * 
     */
    public int getTotalBulletCount(int weapon) {
        if (totalBulletCount.containsKey(weapon)) {
            return totalBulletCount.get(weapon);
        } else {
            return 0;
        }
    }

    /**
     * adds a number of bullets to the total amount for that weapon.
     * @param weapon use static int from this class
     * @param count the number of bullets picked up
     * 
     */
    public void addToTotalBulletCount(int weapon, int count) {
        totalBulletCount.put(weapon, getTotalBulletCount(weapon) + count);
    }

    /**
     * change current weapon
     * @param weapon the weapon you are changing to
     * 
     */
    public void setCurrentWeapon(int weapon) {
        current = weapon;
    }

    /**
     * get the totalt bullet count of the currently selected weapon 
     * except for the bullets in the magazine.
     * 
     */
    public int getTotalBulletCount() {
        return getTotalBulletCount(current);
    }

    /**
     * add bullets to the current used weapons total bullet count
     * @param count number of bullets to add
     * 
     */
    public void addCount(int count) {
        addToTotalBulletCount(current, current);
    }

    /**
     * decrese currently used magazines bullet count by 1.
     */
    public void fireCurrentGun() {
        int count = getMagazineCount(current);
        ammoBuffer.put(current, count - 1);
        onAmmoCountChanged();
    }

    
    /**
     * add listner to be triggered every time ammo-count changes.
     * 
     */
    public void addAmmoChangedListener(AmmoChangedListener listener) {
        if(listener != null){
            listeners.add(listener);
        }
        else{
            System.err.println("Listener you are trying to set is null! Dude..");
        }
        onAmmoCountChanged();
    }

    /**
     * get the allowed count of bullets for the currently used magazine.
     * 
     */
    private int getMagSize(int weapon) {
        if (allowedBulletsCount.containsKey(weapon)) {
            return allowedBulletsCount.get(weapon);
        } else {
            return 1;
        }
    }

    /**
     * get bullet count from the currently used magazine.
     * 
     */
    public int getMagazineCount() {
        return getMagazineCount(current);
    }
    
    /**
     * get bullet count from the selected guns magazine.
     * @param weapon the gun of the magazine you want to check
     */
    public int getMagazineCount(int weapon) {
        if (ammoBuffer.containsKey(weapon)) {
            return ammoBuffer.get(weapon);
        } else {
            return 0;
        }
    }

    /**
     * transfer a number of bullets from backpack or whatever to magazine.
     * @param bullets the bullet count to be transfered
     * 
     */
    private void moveBulletsToBuffer(int weapon, int bullets) {
        ammoBuffer.put(weapon, getMagazineCount(weapon) + bullets);
        totalBulletCount.put(current, getTotalBulletCount() - bullets);
        onAmmoCountChanged();
    }
    
    /**
     * notify the registered listeners.
     * 
     */
    private void onAmmoCountChanged() {
        
        
        for (AmmoChangedListener listener : listeners) {
            final Integer bulletCount = totalBulletCount.get(current);
            final Integer bufferCount = getMagazineCount(current);
            
            if(listener != null){
                
                
                listener.onAmmoCountChanged(current, bulletCount, bufferCount);
            }
            else{
                System.err.println("We have a null pointer in the AmmoChangedListener-list");
            }
        }
    }

    /**
     * adds starting ammo and reloads the guns.
     * 
     */
    private void setupStartAmmo() {
        
        addToTotalBulletCount(PISTOL, 30);
        moveBulletsToBuffer(PISTOL, getMagSize(PISTOL));
                
        addToTotalBulletCount(ROCKET_LAUNCHER, 10);
        moveBulletsToBuffer(ROCKET_LAUNCHER, getMagSize(ROCKET_LAUNCHER));
        
    }

    /**
     * handles key-actions.
     * 
     */
    public void onAction(String name, boolean isPressed, float tpf) {
        
        if(isPressed && name.equals(KeyMapper.RELOAD_WEAPON)){
            reload();
        }
        
    }

    public void onCollision(PowerUpControl element, String name) {
        
        if(element.type.equals(PowerUpControl.NAILS)){
            addToTotalBulletCount(PISTOL, 10);
            onAmmoCountChanged();
        }
        
    }

    public void onOtherObjects(String name) {
        
    }

    public interface AmmoChangedListener {

        public void onAmmoCountChanged(int weapon, int totalCount, int bufferCount);
    }
}
