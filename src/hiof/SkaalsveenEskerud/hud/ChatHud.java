/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.hud;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import hiof.SkaalsveenEskerud.appstate.NiftyAppState;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenControl;
import hiof.SkaalsveenEskerud.appstate.NiftyScreenHandler;
import hiof.SkaalsveenEskerud.input.KeyMapper;
import hiof.SkaalsveenEskerud.network.msg.ChatMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author anskaal Class for displaying nifty-screen for chat-messages
 *
 */
public class ChatHud extends NiftyScreenControl implements ActionListener {

    private OnSendListener osl;
    private TextField textField;
    private ArrayList<ChatMessage> chatQueue;

    public ChatHud(NiftyAppState nas, InputManager im) {
        super(nas, NiftyScreenHandler.SCREEN_CHAT);

        chatQueue = new ArrayList<ChatMessage>();
        im.addListener(this, KeyMapper.CHAT_SEND);
    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (isPressed) {
            if (isEnabled() && name.equals(KeyMapper.CHAT_SEND)) {
                sendMessage();
            }
        }

    }

    public void addMessage(ChatMessage msg) {
        chatQueue.add(msg);
        if (chatQueue.size() > 20) {
            chatQueue.remove(0);
        }
        System.out.println("CharHud require refresh");
        requireRefresh();
    }

    public void addMessage(String msg) {
        addMessage(new ChatMessage(msg, "System"));
    }

    public String getChat() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        String res = "";
        for (ChatMessage line : chatQueue) {
            Date resultdate = new Date(line.timestamp);
            res += sdf.format(resultdate) + "\t " + line.player + ": " + line.message + "\n";
        }
        return res;
    }

    public void setOnSendListener(OnSendListener osl) {
        this.osl = osl;
    }

    public void sendMessage() {
        if (textField != null) {
            String msg = textField.getRealText();

            resetTextField();
            if (osl != null && msg != null && msg.length() > 0) {
                osl.onSend(msg);
            }
        }
    }

    private void resetTextField() {
        if (textField != null) {
            textField.setText("");
        }
    }

    @Override
    public void onPostLoad(float tpf) {
        Screen xml = getScreen();
        if (xml != null) {
            textField = xml.findNiftyControl("input", TextField.class);
        }
    }

    @Override
    public void loadXML(Nifty nifty, String xml) {
        nifty.fromXml(xml, getName(), ChatHud.this);
    }

    public interface OnSendListener {

        public void onSend(String msg);
    }
}
