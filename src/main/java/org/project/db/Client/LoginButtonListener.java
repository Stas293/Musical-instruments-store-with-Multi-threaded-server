package org.project.db.Client;

import org.project.db.Dto.LoginDto;
import org.project.db.Model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class LoginButtonListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(login.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            password = sb.toString();
            databaseClient.toServer.writeObject(new LoginDto(login, password));
            Object object = databaseClient.fromServer.readObject();
            if (object instanceof User) {
                databaseClient.user = (User) object;
                databaseClient.loggedInUser();
                System.out.println(object);
            } else if (object instanceof String) {
                System.out.println((String) object);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
