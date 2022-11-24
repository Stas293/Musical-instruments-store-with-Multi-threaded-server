package org.project.db.client.controller;

import org.project.db.client.view.DatabaseClient;
import org.project.db.dto.RegistrationDto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterButtonListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;

    private static final Logger logger = Logger.getLogger(RegisterButtonListener.class.getName());
    private final JTextField tfLogin;
    private final JTextField tfEmail;
    private final JTextField tfFirstName;
    private final JTextField tfLastName;
    private final JTextField tfPhone;
    private final JTextField tfPassword;

    public RegisterButtonListener(DatabaseClient databaseClient, ObjectInputStream fromServer, ObjectOutputStream toServer, JTextField tfLogin, JTextField tfEmail, JTextField tfFirstName, JTextField tfLastName, JTextField tfPhone, JTextField tfPassword) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.tfLogin = tfLogin;
        this.tfEmail = tfEmail;
        this.tfFirstName = tfFirstName;
        this.tfLastName = tfLastName;
        this.tfPhone = tfPhone;
        this.tfPassword = tfPassword;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String login = tfLogin.getText().trim();
            String email = tfEmail.getText().trim();
            String firstName = tfFirstName.getText().trim();
            String lastName = tfLastName.getText().trim();
            String phone = tfPhone.getText().trim();
            String password = tfPassword.getText().trim();
            if (login.length() == 0 || email.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || phone.length() == 0 || password.length() == 0)
                return;
            toServer.writeObject("register");
            password = DatabaseClient.checkUser(login, password);
            toServer.writeObject(new RegistrationDto(login, firstName, lastName, email, password, phone));
            DatabaseClient.printObject(databaseClient);
        } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while register", e1);
        }
    }
}
