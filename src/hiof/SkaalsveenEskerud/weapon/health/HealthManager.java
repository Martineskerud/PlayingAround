/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.weapon.health;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.scene.Spatial;
import hiof.SkaalsveenEskerud.control.PowerUpCollisionControl;
import hiof.SkaalsveenEskerud.control.PowerUpControl;
import hiof.SkaalsveenEskerud.hud.Hud;
import hiof.SkaalsveenEskerud.network.ClientData;
import hiof.SkaalsveenEskerud.world.model.Meteor;
import hiof.SkaalsveenEskerud.world.model.OilDrum;

/**
 *
 * @author root
 */
public class HealthManager implements PowerUpCollisionControl.MyCollisionListener, PhysicsCollisionListener{

    private final Hud hud;
    private long burnStamp = 0;
    private long BURN_TIME_DELAY = 250;
    private ClientData clientData;
    private String myName;
    
    
    public HealthManager(Hud hud) {
        this.hud = hud;        
    }
    
    public void addDamage(int hp){
        hud.sendDamage(hp, myName);
    }
    
    public void heal(int hp){
        hud.sendDamage(-hp, myName);
        hud.createLogBomb("+"+hp);
        
    }

    public void onCollision(PowerUpControl element, String name) {
        
        if(name!=null){
            String[] data = name.split(":");
            if(data != null && data.length >2 && data[0].equals(PowerUpControl.PLUS_HEALTH)){
                
                int h= Integer.parseInt(data[1]);
                if(h > 0){
                    heal(h);
                    System.out.println("K: HEALED "+h);
                }
            }
        }
        
        
    }

    public void onOtherObjects(String name) {
        
        if(OilDrum.NAME.equals(name)){
            maybeDamage(5);
            
        }
        
    }

    private int getHp(){
        if(clientData != null){
            return clientData.hp;
        }
        else{
            return 100;
        }
    }
    
    public void setHpSource(ClientData clientData) {
       
    }

    public void setHpSource(ClientData clientData, String key) {
         this.clientData = clientData;
         this.myName = key;
         updateHelathUi();
    }

    private void updateHelathUi() {
        hud.updateHealthUi(getHp());
    }

    public void collision(PhysicsCollisionEvent event) {
        
        Spatial nodeA = event.getNodeA();
        Spatial nodeB = event.getNodeB();
        
        String nameA = nodeA.getName();
        String nameB = nodeB.getName();
        
        if(Meteor.NAME.equals(nameA) && "Me".equals(nameB)){
            maybeDamage(1000);
        }
        else if(Meteor.NAME.equals(nameB) && "Me".equals(nameA)){
            maybeDamage(1000);
        }
        
    }

    private void maybeDamage(int hp) {
        final long now = System.currentTimeMillis();

        if(now - burnStamp > BURN_TIME_DELAY){
            burnStamp = now;
            
            addDamage(hp);
        }
    }
    
}
