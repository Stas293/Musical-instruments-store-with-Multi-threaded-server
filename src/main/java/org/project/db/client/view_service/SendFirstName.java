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

public class SendFirstName implements ActionListener {
    private final JTextField tfFirstName;
    private static final Logger logger = Logger.getLogger(SendFirstName.class.getName());
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;
    private final DatabaseClient databaseClient;

    public SendFirstName(JTextField tfFirstName, ObjectInputStream fromServer, ObjectOutputStream toServer, DatabaseClient databaseClient) {
        this.tfFirstName = tfFirstName;
        this.fromServer = fromServer;
        this.toServer = toServer;
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("changeFirstName");
            toServer.writeObject(databaseClient.getUserDto());
            toServer.writeObject(tfFirstName.getText().trim());
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while changing first name", e1);
        }
    }
}
