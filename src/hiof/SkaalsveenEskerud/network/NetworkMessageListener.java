/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import hiof.SkaalsveenEskerud.network.msg.TestMessage;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class NetworkMessageListener<T> implements MessageListener<T> {

    public void messageReceived(T source, Message m) {
        /*
         if (m instanceof PositionMessage) {
         PositionMessage msg = (PositionMessage) m;
         System.out.println("CLIENT POS: "+ msg);
         }
         else */
        if (m instanceof TestMessage) {
            TestMessage msg = (TestMessage) m;
            System.out.println("Message: " + msg);
        }


    }
}
