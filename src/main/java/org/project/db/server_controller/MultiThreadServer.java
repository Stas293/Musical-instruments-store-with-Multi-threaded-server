package org.project.db.server_controller;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiThreadServer extends JFrame implements Runnable {
    private final JTextArea ta;
    private static final Logger logger = Logger.getLogger(MultiThreadServer.class.getName());

    private int clientNo = 0;

    public MultiThreadServer() {
        ta = new JTextArea(10, 10);
        JScrollPane sp = new JScrollPane(ta);
        this.add(sp);
        this.setTitle("MultiThreadServer");
        this.setSize(400, 200);
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {

            ta.append("MultiThreadServer started at " + new Date() + '\n');

            while (true) {
                Socket socket = serverSocket.accept();

                clientNo++;

                ta.append("Starting thread for client " + clientNo +
                        " at " + new Date() + '\n');

                InetAddress inetAddress = socket.getInetAddress();
                ta.append("client " + clientNo + "'s host name is "
                        + inetAddress.getHostName() + "\n");
                ta.append("client " + clientNo + "'s IP Address is "
                        + inetAddress.getHostAddress() + "\n");
                new Thread(new HandleAClient(socket)).start();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

}