package org.project.db.client;

import org.project.db.dto.RegistrationDto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

class RegisterButtonListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(RegisterButtonListener.class.getName());

    public RegisterButtonListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String login = databaseClient.tfLogin.getText().trim();
            String email = databaseClient.tfEmail.getText().trim();
            String firstName = databaseClient.tfFirstName.getText().trim();
            String lastName = databaseClient.tfLastName.getText().trim();
            String phone = databaseClient.tfPhone.getText().trim();
            String password = databaseClient.tfPassword.getText().trim();
            if (login.length() == 0 || email.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || phone.length() == 0 || password.length() == 0)
                return;
            databaseClient.toServer.writeObject("register");
            password = DatabaseClient.checkUser(login, password);
            databaseClient.toServer.writeObject(new RegistrationDto(login, firstName, lastName, email, password, phone));
            DatabaseClient.printObject(databaseClient);
        } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while register", e1);
        }
    }
}
