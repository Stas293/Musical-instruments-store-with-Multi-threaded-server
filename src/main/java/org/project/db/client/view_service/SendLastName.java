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

public class SendLastName implements ActionListener {
    private static final Logger logger = Logger.getLogger(SendLastName.class.getName());
    private final JTextField tfLastName;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final DatabaseClient databaseClient;

    public SendLastName(JTextField tfLastName, ObjectOutputStream toServer, ObjectInputStream fromServer,
                        DatabaseClient databaseClient) {
        this.tfLastName = tfLastName;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("changeLastName");
            toServer.writeObject(databaseClient.getUserDto());
            toServer.writeObject(tfLastName.getText().trim());
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while changing last name", e1);
        }
    }
}
