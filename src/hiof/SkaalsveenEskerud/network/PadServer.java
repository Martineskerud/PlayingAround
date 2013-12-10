/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hiof.SkaalsveenEskerud.network;

import com.jme3.input.controls.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author anskaal
 */
public class PadServer extends Thread implements ActionListener {

    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;

    public PadServer() {
        super();
        start();
    }

    @Override
    public void run() {

        try {
            //1. creating a server socket
            providerSocket = new ServerSocket(2004, 10);

            //2. Wait for connection
            System.out.println("Waiting for connection");
            connection = providerSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());

            //3. get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful");
            //4. The two parts communicate via the input and output streams
            do {
                try {
                    message = (String) in.readObject();
                    System.out.println("client>" + message);
                    if (message.equals("bye")) {
                        sendMessage("bye");
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Data received in unknown format");
                }
            } while (!message.equals("bye"));
        } catch (IOException ioException) {
            //ioException.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("server>" + msg);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void onAction(String name, boolean isPressed, float tpf) {

        if (isPressed) {
            System.out.println("Starting pad server...");
            start();
        }

    }

    public void closeConnection() {
        //4: Closing connection
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (providerSocket != null) {
                providerSocket.close();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
