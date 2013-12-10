/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.hud;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.BillboardControl;
import com.jme3.system.AppSettings;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.control.LogBomb;
import hiof.SkaalsveenEskerud.weapon.AmmoManager;
import java.util.ArrayList;

/**
 *
 * @author martin
 *
 * anskaal: - Extended node instead of mainComponent
 */
public class Hud extends Node implements AmmoManager.AmmoChangedListener {

    private BitmapText hudText;
    private BitmapFont guiFont;
    private BitmapText hudHpText;
    private BitmapText hudAmmoText;
    private final int width;
    private final int height;
    private String currentText;
    private ArrayList<TimeMessage> msgQueue;
    private final Main main;

    public Hud(Main main, AssetManager assetManager, AppSettings appSettings) {
        super("HUD");
        this.main = main;
        width = appSettings.getWidth();
        height = appSettings.getHeight();
        msgQueue = new ArrayList<TimeMessage>();
        attachChild(new CrossHair(assetManager, width, height));
        createHudText(assetManager, width, height);

        addControl(new AbstractControl() {
            private long lastDisplayTime = 0;
            private long lastDisplayTimestamp = 0;

            @Override
            protected void controlUpdate(float tpf) {

                final long now = System.currentTimeMillis();
                final boolean displayChangeTime = now - lastDisplayTimestamp > lastDisplayTime;

                if (msgQueue.size() > 0) {

                    final TimeMessage obj = msgQueue.get(0);

                    if (displayChangeTime) {

                        String msg = obj.msg;
                        lastDisplayTimestamp = now;
                        lastDisplayTime = obj.displayTime;

                        setText(msg);
                        msgQueue.remove(0);
                    }


                } else if (displayChangeTime) {
                    setText("");
                }
            }

            @Override
            protected void controlRender(RenderManager rm, ViewPort vp) {
            }
        });

    }

    private void createHudText(AssetManager assetManager, int width, int height) {

        guiFont = assetManager.loadFont("Interface/Fonts/UbuntuMedium.fnt");
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());
        hudText.setText("");
        hudText.setLocalTranslation(20, 25, 0);
        attachChild(hudText);


        BitmapFont myFont = assetManager.loadFont("Interface/Fonts/Impact.fnt");
        hudHpText = new BitmapText(myFont, false);
        hudHpText.setText("");
        attachChild(hudHpText);
        hudHpText.setLocalTranslation(20, hudHpText.getHeight() + 20, 0);


        hudAmmoText = new BitmapText(myFont, false);
        hudAmmoText.setText("150");
        attachChild(hudAmmoText);


        updateHealthUi(100);
    }

    public void queueText(String currentText, long time) {
        msgQueue.add(new TimeMessage(currentText, time));
    }

    public void sendDamage(int health, String name) {
        main.networkManager.gameClient.sendDamage(name, health);
    }

    public class TimeMessage {

        private final long displayTime;
        private final String msg;

        public TimeMessage(String msg, long displayTime) {
            this.msg = msg;
            this.displayTime = displayTime;

        }
    }

    private void setText(String msg) {
        if (hudText != null) {
            hudText.setText(msg);

            float x = width / 2 - hudText.getLineWidth() / 2;
            float y = height / 2 - hudText.getLineHeight() / 2;
            hudText.setLocalTranslation(x, y, 0);

        }
    }

    public BitmapFont getGuiFont() {
        return guiFont;
    }

    public void updateHealthUi(int hp) {
        hudHpText.setText("" + hp);
    }

    public void onAmmoCountChanged(int weapon, int totalCount, int bufferCount) {

        hudAmmoText.setText(bufferCount + " : " + totalCount);
        hudAmmoText.setLocalTranslation(width - (hudAmmoText.getLineWidth() + 20), hudAmmoText.getHeight() + 20, 0);

    }

    public void createLogBomb(String msg) {
        Camera cam = main.getCamera();

        if (cam != null) {

            Vector3f up = cam.getUp();
            Vector3f left = cam.getLeft();
            Vector3f dir = cam.getDirection();

            Quaternion q = new Quaternion();
            q.fromAxes(left, up, dir);
            q.normalizeLocal();

            //Quaternion roll180 = new Quaternion(); 
            //roll180.fromAngleAxis( FastMath.PI , new Vector3f(0,0,1) ); 


            Vector3f vec = cam.getLocation().add(dir.mult(30f));

            LogBomb bomb = new LogBomb(main.getAssetManager(), msg, ColorRGBA.White);
            Node n = bomb.getNode();
            n.setLocalRotation(q);
            n.setLocalTranslation(vec);

            n.setQueueBucket(RenderQueue.Bucket.Transparent);
            Geometry quad = bomb.mkQuad(ColorRGBA.BlackNoAlpha);
            n.attachChild(quad);

            main.getRootNode().attachChild(n);


        }
    }
}