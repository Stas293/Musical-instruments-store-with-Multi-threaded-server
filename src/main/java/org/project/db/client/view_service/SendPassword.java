package org.project.db.client.view_service;

import org.project.db.client.DatabaseClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendPassword implements ActionListener {
    private final Logger logger = Logger.getLogger(SendPassword.class.getName());
    private final JTextField tfPassword;
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public SendPassword(JTextField tfPassword, DatabaseClient databaseClient, ObjectOutputStream toServer,
                        ObjectInputStream fromServer) {
        this.tfPassword = tfPassword;
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("changePassword");
            toServer.writeObject(databaseClient.getUserDto());
            toServer.writeObject(tfPassword.getText());
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while changing password", e1);
        }
    }
}
