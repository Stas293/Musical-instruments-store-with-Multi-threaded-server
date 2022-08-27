package org.project.db.Client;

import org.project.db.Dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

class ModifyProfileListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public ModifyProfileListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 1));
        JButton btChangeEmail = new JButton("Change email");
        infoPanel.add(btChangeEmail);
        JButton btChangeFirstName = new JButton("Change first name");
        infoPanel.add(btChangeFirstName);
        JButton btChangeLastName = new JButton("Change last name");
        infoPanel.add(btChangeLastName);
        JButton btChangePhone = new JButton("Change phone");
        infoPanel.add(btChangePhone);
        JButton btChangePassword = new JButton("Change password");
        infoPanel.add(btChangePassword);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JButton btBack = new JButton("Back");
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 400);
        databaseClient.repaint();
        btChangeEmail.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfEmail = new JTextField();
            infoPanel1.add(tfEmail);
            JButton btChangeEmail1 = new JButton("Change email");
            infoPanel1.add(btChangeEmail1);
            mainPanel1.add(infoPanel1, BorderLayout.CENTER);
            JButton btBack1 = new JButton("Back");
            btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
            mainPanel1.add(btBack1, BorderLayout.SOUTH);
            databaseClient.add(mainPanel1);
            databaseClient.setSize(400, 150);
            databaseClient.repaint();
            btChangeEmail1.addActionListener((event1) -> {
                try {
                    databaseClient.toServer.writeObject("changeEmail");
                    databaseClient.toServer.writeObject(new UserDto(databaseClient.user.getLogin()));
                    databaseClient.toServer.writeObject(tfEmail.getText().trim());
                    Object object = databaseClient.fromServer.readObject();
                    System.out.println(object);
                    databaseClient.loggedInUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        btChangeFirstName.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfFirstName = new JTextField();
            infoPanel1.add(tfFirstName);
            JButton btChangeFirstName1 = new JButton("Change first name");
            infoPanel1.add(btChangeFirstName1);
            mainPanel1.add(infoPanel1, BorderLayout.CENTER);
            JButton btBack1 = new JButton("Back");
            btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
            mainPanel1.add(btBack1, BorderLayout.SOUTH);
            databaseClient.add(mainPanel1);
            databaseClient.setSize(400, 150);
            databaseClient.repaint();
            btChangeFirstName1.addActionListener((event1) -> {
                try {
                    databaseClient.toServer.writeObject("changeFirstName");
                    databaseClient.toServer.writeObject(new UserDto(databaseClient.user.getLogin()));
                    databaseClient.toServer.writeObject(tfFirstName.getText().trim());
                    Object object = databaseClient.fromServer.readObject();
                    System.out.println(object);
                    databaseClient.loggedInUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        btChangeLastName.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfLastName = new JTextField();
            infoPanel1.add(tfLastName);
            JButton btChangeLastName1 = new JButton("Change last name");
            infoPanel1.add(btChangeLastName1);
            mainPanel1.add(infoPanel1, BorderLayout.CENTER);
            JButton btBack1 = new JButton("Back");
            btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
            mainPanel1.add(btBack1, BorderLayout.SOUTH);
            databaseClient.add(mainPanel1);
            databaseClient.setSize(400, 150);
            databaseClient.repaint();
            btChangeLastName1.addActionListener((event1) -> {
                try {
                    databaseClient.toServer.writeObject("changeLastName");
                    databaseClient.toServer.writeObject(new UserDto(databaseClient.user.getLogin()));
                    databaseClient.toServer.writeObject(tfLastName.getText().trim());
                    Object object = databaseClient.fromServer.readObject();
                    System.out.println(object);
                    databaseClient.loggedInUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        btChangePhone.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfPhone = new JTextField();
            infoPanel1.add(tfPhone);
            JButton btChangePhone1 = new JButton("Change phone");
            infoPanel1.add(btChangePhone1);
            mainPanel1.add(infoPanel1, BorderLayout.CENTER);
            JButton btBack1 = new JButton("Back");
            btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
            mainPanel1.add(btBack1, BorderLayout.SOUTH);
            databaseClient.add(mainPanel1);
            databaseClient.setSize(400, 150);
            databaseClient.repaint();
            btChangePhone1.addActionListener((event1) -> {
                try {
                    databaseClient.toServer.writeObject("changePhone");
                    databaseClient.toServer.writeObject(new UserDto(databaseClient.user.getLogin()));
                    databaseClient.toServer.writeObject(tfPhone.getText().trim());
                    Object object = databaseClient.fromServer.readObject();
                    System.out.println(object);
                    databaseClient.loggedInUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
        btChangePassword.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfPassword = new JTextField();
            infoPanel1.add(tfPassword);
            JButton btChangePassword1 = new JButton("Change password");
            infoPanel1.add(btChangePassword1);
            mainPanel1.add(infoPanel1, BorderLayout.CENTER);
            JButton btBack1 = new JButton("Back");
            btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
            mainPanel1.add(btBack1, BorderLayout.SOUTH);
            databaseClient.add(mainPanel1);
            databaseClient.setSize(400, 150);
            databaseClient.repaint();
            btChangePassword1.addActionListener((event1) -> {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(databaseClient.user.getLogin().getBytes());
                    String password = tfPassword.getText().trim();
                    byte[] bytes = md.digest(password.getBytes());
                    StringBuilder sb = new StringBuilder();

                    for (byte aByte : bytes) {
                        sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                                .substring(1));
                    }
                    password = sb.toString();
                    databaseClient.toServer.writeObject("changePassword");
                    databaseClient.toServer.writeObject(new UserDto(databaseClient.user.getLogin()));
                    databaseClient.toServer.writeObject(password);
                    Object object = databaseClient.fromServer.readObject();
                    System.out.println(object);
                    databaseClient.loggedInUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException | NoSuchAlgorithmException ex) {
                    System.out.println(Arrays.toString(ex.getStackTrace()) + " " + ex.getMessage());
                }
            });
        });
    }
}
