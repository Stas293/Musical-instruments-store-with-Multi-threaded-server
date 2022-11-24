package org.project.db.client.service;

import org.project.db.client.DatabaseClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendPhone implements ActionListener {
    private final JTextField tfPhone;
    private final Logger logger = Logger.getLogger(SendPhone.class.getName());
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final DatabaseClient databaseClient;

    public SendPhone(JTextField tfPhone, ObjectOutputStream toServer, ObjectInputStream fromServer, DatabaseClient databaseClient) {
        this.tfPhone = tfPhone;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("changePhone");
            toServer.writeObject(databaseClient.getUserDto());
            toServer.writeObject(tfPhone.getText().trim());
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while changing phone", e1);
        }
    }
}
