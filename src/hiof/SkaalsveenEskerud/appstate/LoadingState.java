/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.FlyByCamera;
import com.jme3.scene.Node;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.character.Oto;
import hiof.SkaalsveenEskerud.hud.DisplayManager;
import hiof.SkaalsveenEskerud.hud.Hud;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anskaal
 */
public class LoadingState extends AbstractAppState {

    private AssetManager assetManager;
    private Node rootNode;
    private Main main;
    private DisplayManager displayManager;
    private boolean displayLoaded;
    private AppStateManager stateManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {



        System.err.println("Loading...");
        this.stateManager = stateManager;

        Logger.getLogger("").setLevel(Level.WARNING);

        main = (Main) app;
        assetManager = main.getAssetManager();
        rootNode = main.getRootNode();

        FlyByCamera flyCam = main.getFlyByCamera();
        flyCam.setEnabled(false);

        // Adding manager for the display
        setupDisplay(main);
        displayManager.setMessage("Loading");

    }

    @Override
    public void update(float tpf) {

        if (displayLoaded) {

            main.inGameState = new InGameState(displayManager);
            stateManager.attach(main.inGameState);
            setEnabled(false);
        }

        displayLoaded = true;

    }

    public void setupDisplay(Main main) {

        main.setDisplayStatView(false);
        main.setDisplayFps(false);

        //removing the vertice counter etc
        main.setDisplayStatView(false);
        //main.getsettings.setSettingsDialogImage("Textures/anders.jpg");
        //we want to see far as fuck away
        main.getCamera().setFrustumFar(1800f);

        Hud hud = new Hud(main, assetManager, main.getAppSettings());
        main.getGuiNode().attachChild(hud);

        displayManager = new DisplayManager(main, hud, main.getInputManager()) {
            @Override
            public Oto createPlayerObject(String playerKey) {
                Oto model = new Oto(assetManager, stateManager.getState(BulletAppState.class), false);
                model.setName(playerKey);


                main.world.characters.attachChild(model);

                return model;
            }
        };

        

    }
}
