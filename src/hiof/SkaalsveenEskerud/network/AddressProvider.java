/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.network.Client;
import com.jme3.network.Network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author anskaal
 */
public class AddressProvider {

    private static String HOST = "http://www.swe.no/site/jme3/";
    private static int ethFail = 0, ethSent = 0, g = 0;
    private static ArrayList<String> al;

    public static ArrayList<String> host() throws MalformedURLException, IOException {
        return getRequest("host");
    }

    public static ArrayList<String> connect() throws MalformedURLException, IOException {
        return getRequest("connect");
    }

    public static ArrayList<String> disconnect() throws MalformedURLException, IOException {
        return getRequest("disconnect");
    }

    public static ArrayList<String> hostDisconnect() throws MalformedURLException, IOException {
        return getRequest("reset");
    }

    private static ArrayList<String> getRequest(String action) throws MalformedURLException, IOException, ProtocolException {

        if(NetworkManager.USE_INTERNET){
            return new ArrayList<String>();
        }
        
        System.out.println("Sending http-request: " + action + "...");
        String url = HOST + "/" + action;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "jME3");

        //int responseCode = con.getResponseCode();

        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;

        StringBuilder response;
        response = new StringBuilder();

        Pattern ptrn = Pattern.compile("[\\.0-9]*");
        ArrayList<String> res = new ArrayList<String>();

        while ((inputLine = in.readLine()) != null) {

            Matcher mat = ptrn.matcher(inputLine);
            if (mat.matches()) {
                res.add(inputLine);
            }

            response.append(inputLine);
        }
        in.close();

        //print result
        //System.out.println("\t"+Arrays.toString(response.toString().split("\n")));

        return res;
    }

    /**
     * TODO: make it understandable.
     */
    public static String findLocalGames() {
        final String addressPrefix;
        // calculate the prefix by looking at addresses of local devices
        // discarding the loopback device
        {
            al = new ArrayList<String>();


            Enumeration<NetworkInterface> dev;
            Enumeration<InetAddress> addr;
            String address = null;
            try {
                dev = NetworkInterface.getNetworkInterfaces();
                while (dev.hasMoreElements()) {
                    NetworkInterface ni = dev.nextElement();
                    if (ni.isLoopback()) {
                        continue;
                    }

                    addr = ni.getInetAddresses();

                    while (addr.hasMoreElements()) {
                        InetAddress ip = addr.nextElement();

                        // check for IPv4 (IPv6 uses ":")
                        if (ip.toString().contains(".")) {
                            final String s = ip.toString();
                            address = s.substring(s.indexOf('/') + 1, s.lastIndexOf('.') + 1);
                            System.out.println("while: " + address);
                            break;
                        }
                    }
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (null == address) {
                return null;
            } else {
                addressPrefix = address;
            }
        }

        // send requests to all 256 local IPs
        final LinkedBlockingQueue<String> success;
        success = new LinkedBlockingQueue();
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 256; i++) {
            final int j = i;
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    final String addr = addressPrefix + j;

                    ethSent++;

                    al.add(addr);

                    try {
                        Client c = Network.connectToServer(addr, NetworkManager.PORT);
                        g++;
                        c.start();
                        success.add(addr);
                        c.close();
                    } catch (Exception e) {
                        // connection failed
                        ethFail++;
                        System.out.println("FAIL[" + ethFail + " / " + ethSent + " / " + g + "] failed.");

                        al.remove(addr);

                        if (ethFail > 240) {
                            System.out.println("arr: " + Arrays.toString(al.toArray(new String[0])));
                        }
                    }
                }
            });
        }

        try {

            pool.awaitTermination(2, TimeUnit.SECONDS);
            System.out.println("Thread pool terminated");

        } catch (InterruptedException e) {

            System.out.println("Thread pool shutting down");
            // shut down remaining entries
            pool.shutdownNow();
            System.out.println("Thread pool shut down");
        }

        // collect answers
        if (success.size() > 0) {

            System.out.println("eth returning " + success.size());

            return success.poll();
        } else {

            System.out.println("eth returning " + (success == null ? "null" : "0"));

            return null;
        }
    }

    public static void broadcastServerRequest() {
    }
}
