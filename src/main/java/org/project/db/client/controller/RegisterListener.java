package org.project.db.client.controller;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;
import org.project.db.client.service.RegisterButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public RegisterListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 2));
        infoPanel.add(new JLabel(MainConstants.LOGIN));
        databaseClient.tfLogin = new JTextField(20);
        infoPanel.add(databaseClient.tfLogin);
        infoPanel.add(new JLabel("Email"));
        databaseClient.tfEmail = new JTextField(20);
        infoPanel.add(databaseClient.tfEmail);
        infoPanel.add(new JLabel("First Name"));
        databaseClient.tfFirstName = new JTextField(20);
        infoPanel.add(databaseClient.tfFirstName);
        infoPanel.add(new JLabel("Last Name"));
        databaseClient.tfLastName = new JTextField(20);
        infoPanel.add(databaseClient.tfLastName);
        infoPanel.add(new JLabel("Phone"));
        databaseClient.tfPhone = new JTextField(20);
        infoPanel.add(databaseClient.tfPhone);
        infoPanel.add(new JLabel("Password"));
        databaseClient.tfPassword = new JTextField(20);
        infoPanel.add(databaseClient.tfPassword);
        JButton btRegister = new JButton(MainConstants.REGISTER);
        JButton btBack = DatabaseClient.registerMainPanel(infoPanel, mainPanel, btRegister, databaseClient);
        databaseClient.setSize(450, 300);
        databaseClient.repaint();
        btBack.addActionListener(event -> {
            databaseClient.getContentPane().removeAll();
            databaseClient.startView();
        });
        btRegister.addActionListener(new RegisterButtonListener(databaseClient, fromServer, toServer,
                databaseClient.tfLogin, databaseClient.tfEmail, databaseClient.tfFirstName, databaseClient.tfLastName,
                databaseClient.tfPhone, databaseClient.tfPassword));
    }

}
