package org.project.db.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RegisterListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public RegisterListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 2));
        infoPanel.add(new JLabel("Login"));
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
        JButton btRegister = new JButton("Register");
        JButton btBack = new JButton("Back");
        JPanel registerPanel = new JPanel();
        registerPanel.add(btBack);
        registerPanel.add(btRegister);
        mainPanel.add(registerPanel, BorderLayout.SOUTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.add(databaseClient.openButton);
        controlPanel.add(databaseClient.closeButton);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(450, 300);
        databaseClient.repaint();
        btBack.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            databaseClient.startView();
        });
        btRegister.addActionListener(new RegisterButtonListener(databaseClient));
    }
}
