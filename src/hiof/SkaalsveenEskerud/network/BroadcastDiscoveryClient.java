/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public abstract class BroadcastDiscoveryClient {

    public void start() {

        try {

            boolean success = false;
            for (String bcIp : getBroadcastAddr()) {
                success = broadcast(bcIp);
                if (success) {
                    break;
                }
            }
            if (!success) {
                noServersResponded();
            }

            msg("WIll now close client discovery...");

        } catch (UnknownHostException ex) {
            Logger.getLogger(BroadcastDiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BroadcastDiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public abstract void onServerFound(String ip, String connectionString);

    public abstract void noServersResponded();

    public ArrayList<String> getBroadcastAddr() {

        ArrayList<String> listOfBroadcasts = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> dev = NetworkInterface.getNetworkInterfaces();
            while (dev.hasMoreElements()) {
                NetworkInterface ni = dev.nextElement();
                if (ni.isLoopback()) {
                    continue;
                }
                Iterator<InterfaceAddress> addr = ni.getInterfaceAddresses().iterator();

                while (addr.hasNext()) {

                    InterfaceAddress iaddr = (InterfaceAddress) addr.next();
                    if (iaddr == null) {
                        continue;
                    }
                    InetAddress broadcast = iaddr.getBroadcast();
                    if (broadcast != null) {
                        System.out.println("Found broadcast: " + broadcast);
                        listOfBroadcasts.add(broadcast.toString().replace("/", ""));
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (listOfBroadcasts.size() < 1) {
            return null;
        } else {
            return listOfBroadcasts;
        }

    }

    private void msg(String msg) {
        System.out.println(msg);
    }

    private boolean broadcast(String bcAddr) throws UnknownHostException, IOException {

        DatagramSocket s = new DatagramSocket();

        byte[] buf = new byte[1000];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        InetAddress hostAddress = InetAddress.getByName(bcAddr);
        msg("Broadcasting to servers. [broadcast address: " + bcAddr + "]");
        buf = "Hey my friend".getBytes();
        try {
            DatagramPacket out = new DatagramPacket(buf, buf.length, hostAddress, BroadcastDiscoveryListener.PORT);
            s.setSoTimeout(1000);
            s.send(out);
        } catch (BindException be) {
            System.out.println("Could not send broadcast on " + hostAddress.getHostAddress());
        }
        try {
            s.receive(dp);
            String addr = dp.getAddress() + ""; // need t oremove slash in order to make a valid IP
            onServerFound(addr.replace("/", ""), new String(dp.getData(), 0, dp.getLength()));

            return true;

        } catch (SocketTimeoutException ex) {
            return false;
        }
    }
}
