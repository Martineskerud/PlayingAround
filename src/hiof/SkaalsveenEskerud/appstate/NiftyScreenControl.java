/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.appstate;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author anskaal
 */
public abstract class NiftyScreenControl implements ScreenController {

    private final NiftyAppState nas;
    private boolean requireRefresh = true;
    private final String screenName;

    public NiftyScreenControl(NiftyAppState nas, String screen) {
        this.nas = nas;
        this.screenName = screen;
    }

    protected void update(float tpf) {

        if (requireRefresh) {
            requireRefresh = false;
            nas.load();
            onPostLoad(tpf);
        }

    }

    protected void requireRefresh() {
        requireRefresh = true;
    }

    public Screen getScreen() {
        return nas.nifty.getScreen(screenName);
    }

    public String getName() {
        return screenName;
    }

    public boolean isEnabled() {
        return nas.getActiveScreenName().equals(screenName);
    }

    public abstract void onPostLoad(float tpf);

    public abstract void loadXML(Nifty nifty, String xml);

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
    }

    public void onEndScreen() {
    }
}
