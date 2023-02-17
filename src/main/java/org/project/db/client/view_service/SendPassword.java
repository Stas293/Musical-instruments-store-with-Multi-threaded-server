package org.project.db.client.view_service;

import org.project.db.client.DatabaseClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendPassword implements ActionListener {
    private final Logger logger = Logger.getLogger(SendPassword.class.getName());
    private final JTextField tfPassword;
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public SendPassword(JTextField tfPassword, DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.tfPassword = tfPassword;
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(databaseClient.getUserDto().login().getBytes());
            String password = tfPassword.getText().trim();
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (var aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            password = sb.toString();
            toServer.writeObject("changePassword");
            toServer.writeObject(databaseClient.getUserDto());
            toServer.writeObject(password);
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e1) {
            logger.log(Level.WARNING, "Error while changing password", e1);
        }
    }
}
