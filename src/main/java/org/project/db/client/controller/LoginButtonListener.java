package org.project.db.client.controller;

import org.project.db.client.view.DatabaseClient;
import org.project.db.dto.LoginDto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginButtonListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final JTextField tfLogin;
    private final JTextField tfPassword;

    private static final Logger logger = Logger.getLogger(LoginButtonListener.class.getName());

    public LoginButtonListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer, JTextField tfLogin, JTextField tfPassword) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.tfLogin = tfLogin;
        this.tfPassword = tfPassword;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String login = tfLogin.getText().trim();
            String password = tfPassword.getText().trim();
            if (login.length() == 0 || password.length() == 0) {
                return;
            }
            toServer.writeObject("login");
            password = DatabaseClient.checkUser(login, password);
            toServer.writeObject(new LoginDto(login, password));
            DatabaseClient.printObject(databaseClient);
        } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while login", e1);
        }
    }
}
