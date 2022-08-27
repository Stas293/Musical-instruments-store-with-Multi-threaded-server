package org.project.db.Client;

import org.project.db.Dto.RegistrationDto;
import org.project.db.Model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class RegisterButtonListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
            if (login.length() == 0 || email.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || phone.length() == 0 || password.length() == 0) {
                return;
            }
            databaseClient.toServer.writeObject("register");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(login.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            password = sb.toString();
            databaseClient.toServer.writeObject(new RegistrationDto(login, firstName, lastName, email, password, phone));
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
