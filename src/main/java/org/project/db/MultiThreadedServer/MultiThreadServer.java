package org.project.db.MultiThreadedServer;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class MultiThreadServer extends JFrame implements Runnable {
    private final JTextArea ta;

    Connection connection;

    private int clientNo = 0;

    public MultiThreadServer() throws SQLException {
        ta = new JTextArea(10, 10);
        JScrollPane sp = new JScrollPane(ta);
        this.add(sp);
        this.setTitle("MultiThreadServer");
        this.setSize(400, 200);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
                        System.getenv("DB_SCHEMA"), System.getenv("MYSQL_USERNAME"),
                System.getenv("MYSQL_PASSWORD"));
        connection.setAutoCommit(false);
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            ta.append("MultiThreadServer started at "
                    + new Date() + '\n');


            while (true) {
                Socket socket = serverSocket.accept();

                clientNo++;

                ta.append("Starting thread for client " + clientNo +
                        " at " + new Date() + '\n');

                InetAddress inetAddress = socket.getInetAddress();
                ta.append("Client " + clientNo + "'s host name is "
                        + inetAddress.getHostName() + "\n");
                ta.append("Client " + clientNo + "'s IP Address is "
                        + inetAddress.getHostAddress() + "\n");
                new Thread(new HandleAClient(this, socket, clientNo)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}