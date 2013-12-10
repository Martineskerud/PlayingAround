/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class BroadcastDiscoveryListener extends Thread {

    public static int PORT = 8383;
    private boolean listening = true;

    public BroadcastDiscoveryListener() {
        super("BroadcastDiscoveryListener-Thread");
    }

    @Override
    public void run() {

        try {

            byte[] buf = new byte[1000];
            DatagramPacket dgp = new DatagramPacket(buf, buf.length);
            DatagramSocket sk;

            sk = new DatagramSocket(PORT);
            sk.setSoTimeout(5000);

            while (listening) {

                try {
                    sk.receive(dgp);
                    String rcvd = new String(dgp.getData(), 0, dgp.getLength()) + ", from address: "
                            + dgp.getAddress() + ", port: " + dgp.getPort();
                    System.out.println("SBR" + rcvd);

                    buf = NetworkManager.makeConnectionString().getBytes();
                    DatagramPacket out = new DatagramPacket(buf, buf.length, dgp.getAddress(), dgp.getPort());
                    sk.send(out);
                } catch (SocketTimeoutException ste) {
                }
            }

        } catch (SocketException ex) {
            Logger.getLogger(BroadcastDiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BroadcastDiscoveryListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void close() {

        listening = false;
        boolean retry = true;

        while (retry) {

            try {
                join();
                retry = false;
                break;
            } catch (InterruptedException ex) {
            }
        }

        System.out.println("BroadcastDiscoveryListener shut down.");

    }

    private void msg(String msg) {
        System.out.println(msg);
    }
}
