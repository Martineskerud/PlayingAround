package hiof.SkaalsveenEskerud.appstate;

import com.jme3.input.controls.ActionListener;
import hiof.SkaalsveenEskerud.input.KeyMapper;

/**
 * Class for taking care of the logic behind how the nifty-screens behave.
 *
 * @author anskaal
 */
public class NiftyScreenHandler implements ActionListener {

    public static final String SCREEN_DEFAULT = "default";
    public static final String SCREEN_PLAYERS = "players";
    public static final String SCREEN_CHAT = "chat";
    public static final String SCREEN_CHAT_READ_ONLY = "chatReadOnly";
    public static final String SCREEN_MENU = "menu";
    private boolean chat, returnToChat, menu, playerList;
    private final NiftyAppState nas;

    public NiftyScreenHandler(NiftyAppState nas) {
        this.nas = nas;
    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (isPressed) {
            if (name.equals(KeyMapper.SHOW_PLAYER_LIST)) {
                togglePlayerList(true);

            } else if (name.equals(KeyMapper.SHOW_CHAT)) {
                if (!chat) {
                    toggleChat(true);
                }

            } else if (name.equals(KeyMapper.HIDE_CHAT)) {
                if (chat) {
                    toggleChat(!chat);
                } else {
                    toggleMenu(!menu);
                }
            }
        } else {

            if (name.equals(KeyMapper.SHOW_PLAYER_LIST)) {
                togglePlayerList(false);
            }
        }

    }

    private void toggleChat(boolean enabled) {
        chat = enabled;

        if (playerList) {
            returnToChat = enabled;
        } else {
            if (enabled) {
                nas.setScreen(SCREEN_CHAT);
            } else {
                nas.setScreen(SCREEN_CHAT_READ_ONLY);
            }
        }
    }

    private void togglePlayerList(boolean enabled) {
        playerList = enabled;

        if (enabled) {
            if (chat) {
                returnToChat = true;
            } else {
                returnToChat = false;
            }
            nas.setScreen(SCREEN_PLAYERS);
        } else {
            toggleChat(returnToChat);
        }

    }

    private void toggleMenu(boolean enabled) {
        menu = enabled;

        if (enabled) {
            nas.setScreen(SCREEN_MENU);
        } else {
            nas.setScreen(SCREEN_CHAT_READ_ONLY);
        }
    }
}
