package org.project.db.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ModifyProfileListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ModifyProfileListener.class.getName());

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
        GetStatusesListener.addBackRepaint(mainPanel, infoPanel, databaseClient);
        btChangeEmail.addActionListener(new ActionEmail().invoke());
        btChangeFirstName.addActionListener(new ActionFirstName().invoke());
        btChangeLastName.addActionListener(new ActionLastName().invoke());
        btChangePhone.addActionListener(new ActionChangePhone().invoke());
        btChangePassword.addActionListener(new ActionChangePassword().invoke());
    }

    private void addButtonRepaint(JPanel mainPanel1, JPanel infoPanel1, JButton btChangeEmail1) {
        infoPanel1.add(btChangeEmail1);
        mainPanel1.add(infoPanel1, BorderLayout.CENTER);
        JButton btBack1 = new JButton(Constants.BACK);
        btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
        mainPanel1.add(btBack1, BorderLayout.SOUTH);
        databaseClient.add(mainPanel1);
        databaseClient.setSize(400, 150);
        databaseClient.repaint();
    }

    private class ActionEmail {
        public ActionListener invoke() {
            return (event) -> {
                databaseClient.getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfEmail = new JTextField();
                infoPanel1.add(tfEmail);
                JButton btChangeEmail1 = new JButton("Change email");
                addButtonRepaint(mainPanel1, infoPanel1, btChangeEmail1);
                btChangeEmail1.addActionListener(event1 -> {
                    try {
                        databaseClient.toServer.writeObject("changeEmail");
                        databaseClient.toServer.writeObject(databaseClient.getUserDto());
                        databaseClient.toServer.writeObject(tfEmail.getText().trim());
                        Object object = databaseClient.fromServer.readObject();
                        System.out.println(object);
                        databaseClient.loggedInUser();
                    } catch (IOException | ClassNotFoundException e1) {
                        logger.log(Level.WARNING, "Error while changing email", e1);
                    }
                });
            };
        }
    }

    private class ActionFirstName {
        public ActionListener invoke() {
            return (event) -> {
                databaseClient.getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfFirstName = new JTextField();
                infoPanel1.add(tfFirstName);
                JButton btChangeFirstName1 = new JButton("Change first name");
                addButtonRepaint(mainPanel1, infoPanel1, btChangeFirstName1);
                btChangeFirstName1.addActionListener(event1 -> {
                    try {
                        databaseClient.toServer.writeObject("changeFirstName");
                        databaseClient.toServer.writeObject(databaseClient.getUserDto());
                        databaseClient.toServer.writeObject(tfFirstName.getText().trim());
                        Object object = databaseClient.fromServer.readObject();
                        System.out.println(object);
                        databaseClient.loggedInUser();
                    } catch (IOException | ClassNotFoundException e1) {
                        logger.log(Level.WARNING, "Error while changing first name", e1);
                    }
                });
            };
        }
    }

    private class ActionLastName {
        public ActionListener invoke() {
            return (event) -> {
                databaseClient.getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfLastName = new JTextField();
                infoPanel1.add(tfLastName);
                JButton btChangeLastName1 = new JButton("Change last name");
                addButtonRepaint(mainPanel1, infoPanel1, btChangeLastName1);
                btChangeLastName1.addActionListener(event1 -> {
                    try {
                        databaseClient.toServer.writeObject("changeLastName");
                        databaseClient.toServer.writeObject(databaseClient.getUserDto());
                        databaseClient.toServer.writeObject(tfLastName.getText().trim());
                        Object object = databaseClient.fromServer.readObject();
                        System.out.println(object);
                        databaseClient.loggedInUser();
                    } catch (IOException | ClassNotFoundException e1) {
                        logger.log(Level.WARNING, "Error while changing last name", e1);
                    }
                });
            };
        }
    }

    private class ActionChangePhone {
        public ActionListener invoke() {
            return (event) -> {
                databaseClient.getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfPhone = new JTextField();
                infoPanel1.add(tfPhone);
                JButton btChangePhone1 = new JButton("Change phone");
                addButtonRepaint(mainPanel1, infoPanel1, btChangePhone1);
                btChangePhone1.addActionListener(event1 -> {
                    try {
                        databaseClient.toServer.writeObject("changePhone");
                        databaseClient.toServer.writeObject(databaseClient.getUserDto());
                        databaseClient.toServer.writeObject(tfPhone.getText().trim());
                        Object object = databaseClient.fromServer.readObject();
                        System.out.println(object);
                        databaseClient.loggedInUser();
                    } catch (IOException | ClassNotFoundException e1) {
                        logger.log(Level.WARNING, "Error while changing phone", e1);
                    }
                });
            };
        }
    }

    private class ActionChangePassword {
        public ActionListener invoke() {
            return (event) -> {
                databaseClient.getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfPassword = new JTextField();
                infoPanel1.add(tfPassword);
                JButton btChangePassword1 = new JButton("Change password");
                addButtonRepaint(mainPanel1, infoPanel1, btChangePassword1);
                btChangePassword1.addActionListener(event1 -> {
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(databaseClient.getUserDto().getLogin().getBytes());
                        String password = tfPassword.getText().trim();
                        byte[] bytes = md.digest(password.getBytes());
                        StringBuilder sb = new StringBuilder();

                        for (var aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
                        password = sb.toString();
                        databaseClient.toServer.writeObject("changePassword");
                        databaseClient.toServer.writeObject(databaseClient.getUserDto());
                        databaseClient.toServer.writeObject(password);
                        Object object = databaseClient.fromServer.readObject();
                        System.out.println(object);
                        databaseClient.loggedInUser();
                    } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e1) {
                        logger.log(Level.WARNING, "Error while changing password", e1);
                    }
                });
            };
        }
    }
}
