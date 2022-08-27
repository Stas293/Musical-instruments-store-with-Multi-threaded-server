package org.project.db.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoginListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public LoginListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 2));
        infoPanel.add(new JLabel("Login"));
        databaseClient.tfLogin = new JTextField(20);
        infoPanel.add(databaseClient.tfLogin);
        infoPanel.add(new JLabel("Password"));
        databaseClient.tfPassword = new JTextField(20);
        infoPanel.add(databaseClient.tfPassword);
        JButton btLogin = new JButton("Login");
        JButton btBack = new JButton("Back");
        JPanel loginPanel = new JPanel();
        loginPanel.add(btBack);
        loginPanel.add(btLogin);
        mainPanel.add(loginPanel, BorderLayout.SOUTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.add(databaseClient.openButton);
        controlPanel.add(databaseClient.closeButton);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 175);
        databaseClient.repaint();
        btBack.addActionListener((event) -> {
            databaseClient.getContentPane().removeAll();
            databaseClient.startView();
        });
        btLogin.addActionListener(new LoginButtonListener(databaseClient));
    }
}
