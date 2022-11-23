package org.project.db.client;

import org.project.db.dto.LoginDto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

class LoginButtonListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(LoginButtonListener.class.getName());

    public LoginButtonListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String login = databaseClient.tfLogin.getText().trim();
            String password = databaseClient.tfPassword.getText().trim();
            if (login.length() == 0 || password.length() == 0) {
                return;
            }
            databaseClient.toServer.writeObject("login");
            password = DatabaseClient.checkUser(login, password);
            databaseClient.toServer.writeObject(new LoginDto(login, password));
            DatabaseClient.printObject(databaseClient);
        } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while login", e1);
        }
    }
}
