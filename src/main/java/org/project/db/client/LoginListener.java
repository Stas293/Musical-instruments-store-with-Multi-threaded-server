package org.project.db.client;

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
        infoPanel.add(new JLabel(Constants.LOGIN));
        databaseClient.tfLogin = new JTextField(20);
        infoPanel.add(databaseClient.tfLogin);
        infoPanel.add(new JLabel("Password"));
        databaseClient.tfPassword = new JTextField(20);
        infoPanel.add(databaseClient.tfPassword);
        JButton btLogin = new JButton(Constants.LOGIN);
        JButton btBack = DatabaseClient.registerMainPanel(infoPanel, mainPanel, btLogin, databaseClient);
        databaseClient.setSize(400, 175);
        databaseClient.repaint();
        btBack.addActionListener(event -> {
            databaseClient.getContentPane().removeAll();
            databaseClient.startView();
        });
        btLogin.addActionListener(new LoginButtonListener(databaseClient));
    }
}
