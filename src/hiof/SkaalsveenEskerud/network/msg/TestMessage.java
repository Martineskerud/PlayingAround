/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author anskaal
 */
@Serializable
public class TestMessage extends AbstractMessage {

    public String hello;       // custom message data
    public String playerKey;

    public TestMessage() {
    }

    public TestMessage(String s) {
        hello = s;
    }

    @Override
    public String toString() {
        return hello;
    }
}
