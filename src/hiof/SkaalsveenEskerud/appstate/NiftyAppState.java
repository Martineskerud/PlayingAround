/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.hud.ChatHud;
import hiof.SkaalsveenEskerud.hud.Menu;
import java.util.HashMap;

/**
 * This appstate handles the nifty-screens.
 *
 * @author anskaal
 */
public class NiftyAppState extends AbstractAppState implements ScreenController {

    protected Nifty nifty;
    protected AppStateManager stateManager;
    public HashMap<String, NiftyScreenControl> modules;
    private String screen = NiftyScreenHandler.SCREEN_CHAT_READ_ONLY;

    public NiftyAppState(AppStateManager sm, NiftyJmeDisplay niftyDisplay) {
        this.nifty = niftyDisplay.getNifty();
        this.stateManager = sm;
        modules = new HashMap<String, NiftyScreenControl>();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
    }

    public void addScreen(NiftyScreenControl nsc) {
        modules.put(nsc.getName(), nsc);
        nifty.registerScreenController(nsc);
    }

    @Override
    public void update(float tpf) {

        if (modules != null) {
            if (modules.containsKey(screen)) {
                getScreen().update(tpf);
            }
        }
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
        super.stateAttached(stateManager);
        setScreen(NiftyScreenHandler.SCREEN_CHAT_READ_ONLY);
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
        super.stateDetached(stateManager);
        nifty.exit();
    }

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }

    public void setScreen(String screen) {

        System.out.println("NiftyAppState screen: " + screen);
        this.screen = screen;
        load();
    }

    public String getActiveScreenName() {
        return screen;
    }

    public void load() {

        String xml = "Interface/Nifty/MainNifty.xml";
        if (modules.containsKey(screen)) {

            getScreen().loadXML(nifty, xml);

        } else {
            nifty.fromXml(xml, screen, (ChatHud) modules.get(NiftyScreenHandler.SCREEN_CHAT));
        }

    }

    public NiftyScreenControl getScreenControl(String screen) {
        if (modules.containsKey(screen)) {
            return modules.get(screen);
        }
        return null;
    }

    private NiftyScreenControl getScreen() {
        return modules.get(screen);
    }

    public void addControls() {
        nifty.addControls();
    }

    public void addScreen(Menu menu, Main main) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
