package hiof.SkaalsveenEskerud;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import hiof.SkaalsveenEskerud.Main.MainContainer;
import hiof.SkaalsveenEskerud.appstate.InGameState;
import hiof.SkaalsveenEskerud.appstate.LoadingState;
import hiof.SkaalsveenEskerud.appstate.WalkableState;
import hiof.SkaalsveenEskerud.control.EvenBetterCharacterControl;
import hiof.SkaalsveenEskerud.network.ClientData;
import hiof.SkaalsveenEskerud.network.PlayerManager;
import hiof.SkaalsveenEskerud.network.NetworkManager;
import hiof.SkaalsveenEskerud.physics.MassFactory;
import hiof.SkaalsveenEskerud.world.World;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main acts as a root-component that creates all the objects necessary to run
 * the application.
 *
 */
public class Main extends SimpleApplication {

    private long startTime;
    public World world;
    public NetworkManager networkManager;
    public MassFactory massFactory;

    public static void main(String[] args) {

        AppSettings settings = new AppSettings(true);
        settings.setResolution(1080, 720);
        
        Main app = new Main();
        app.setShowSettings(false);
        settings.setVSync(true);
        app.setSettings(settings);
        app.start();

    }
    
    public PlayerManager playerManager;
    public EvenBetterCharacterControl myCharacterControl;
    public InGameState inGameState;
    public WalkableState walkableState;

    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
        startTime = System.currentTimeMillis();
        LoadingState loadingState = new LoadingState();
        getStateManager().attach(loadingState);
    }

    @Override
    public void destroy() {


        super.destroy();

        if (networkManager != null) {
            networkManager.close();
        }

    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public void handleFpsLimit(float tpf, int fps) {
        //  player.rotate(0, 2*tpf, 0); 
        float maxFps = 1000 / fps;
        if (tpf < maxFps) {
            try {
                Thread.sleep((long) (maxFps - tpf));
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Sleep failed...", ex);
            }
        }
    }

    public NiftyJmeDisplay getNIftyDisplay() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        return niftyDisplay;
    }
    public ActionListener escInputManager = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                stop();
            }
        }
    };

    public MainContainer getMainContainer() {
        return new Main.MainContainer();
    }

    public AppSettings getAppSettings() {
        return settings;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public String getGeneralInfo() {

        long dif = (System.currentTimeMillis() - startTime);
        int s = (int) (dif / 1000);
        int m = (int) Math.floor(s / 60);

        ClientData cd = playerManager.gameData.getMyData();

        String me = "\nMy name: " + cd.name + " | Health: " + cd.hp;

        return "Play time: " + (m > 0 ? m + "m " : "") + (s % 60) + "s" + me;
    }

    public class MainContainer {

        public Node guiNode;
        public Node rootNode;
        public AssetManager assetManager;
        public ViewPort vp;
        public BulletAppState bas;
        public InputManager im;
        public AppSettings settings;
        public Camera cam;
        public MassFactory massFactory;

        public MainContainer() {

            this.guiNode = Main.this.guiNode;
            this.rootNode = Main.this.rootNode;
            this.assetManager = Main.this.assetManager;
            this.vp = viewPort;
            this.settings = Main.this.settings;
            this.bas = stateManager.getState(BulletAppState.class);
            this.im = inputManager;
            this.cam = Main.this.cam;
            this.massFactory = Main.this.massFactory;
        }
    }
}
