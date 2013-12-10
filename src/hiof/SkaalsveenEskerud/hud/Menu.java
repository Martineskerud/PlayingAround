/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.hud;

import de.lessvoid.nifty.Nifty;
import hiof.SkaalsveenEskerud.Main;
import hiof.SkaalsveenEskerud.appstate.NiftyAppState;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenControl;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenHandler;

/**
 *
 * @author anskaal
 */
public class Menu extends NiftyScreenControl {

    private final Main main;
    public long lastUpdate = 0;

    public Menu(NiftyAppState nas, Main main) {
        super(nas, NiftyScreenHandler.SCREEN_MENU);
        this.main = main;
    }

    @Override
    protected void update(float tpf) {

        long now = System.currentTimeMillis();
        if (now - lastUpdate > 1000) {
            lastUpdate = now;
            requireRefresh();
        }

        super.update(tpf);
    }

    @Override
    public void onPostLoad(float tpf) {
    }

    @Override
    public void loadXML(Nifty nifty, String xml) {
        nifty.fromXml(xml, getName(), Menu.this);
    }

    public String getGeneralInfo() {
        return "General: " + main.getGeneralInfo();
    }

    public String getNetworkInfo() {
        return main.networkManager.getReport();
    }

    @Override
    public void onStartScreen() {
        super.onStartScreen();

        main.getStateManager().detach(main.walkableState);
    }

    @Override
    public void onEndScreen() {
        super.onEndScreen();

        main.getStateManager().attach(main.walkableState);
    }

    public void quitGame() {
        System.out.println("GTFO!");
        main.stop();
    }
}
