/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network.msg;

/**
 *
 * @author anskaal
 */
public interface PingListener {

    public void onPingCalculated(long ping);
}
