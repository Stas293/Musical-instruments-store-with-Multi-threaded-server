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

public class SendEmail implements ActionListener {
    private final JTextField tfEmail;
    private final Logger logger = Logger.getLogger(SendEmail.class.getName());
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final DatabaseClient databaseClient;

    public SendEmail(JTextField tfEmail, ObjectOutputStream toServer, ObjectInputStream fromServer, DatabaseClient databaseClient) {
        this.tfEmail = tfEmail;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("changeEmail");
            toServer.writeObject(databaseClient.getUserDto());
            toServer.writeObject(tfEmail.getText().trim());
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while changing email", e1);
        }
    }
}
