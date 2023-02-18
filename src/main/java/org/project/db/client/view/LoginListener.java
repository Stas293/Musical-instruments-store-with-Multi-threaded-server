package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;
import org.project.db.client.view_service.LoginButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public LoginListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
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
        infoPanel.setLayout(new GridLayout(2, 2));
        infoPanel.add(new JLabel(MainConstants.LOGIN));
        databaseClient.setTfLogin(new JTextField(20));
        infoPanel.add(databaseClient.getTfLogin());
        infoPanel.add(new JLabel("Password"));
        databaseClient.setTfPassword(new JTextField(20));
        infoPanel.add(databaseClient.getTfPassword());
        JButton btLogin = new JButton(MainConstants.LOGIN);
        JButton btBack = databaseClient.getStartView()
                .registerMainPanel(infoPanel, mainPanel, btLogin);
        databaseClient.setSize(400, 175);
        databaseClient.repaint();
        btBack.addActionListener(event -> {
            databaseClient.getContentPane().removeAll();
            databaseClient.getStartView().startView();
        });
        btLogin.addActionListener(
                new LoginButtonListener(
                        databaseClient,
                        toServer,
                        fromServer,
                        databaseClient.getTfLogin(),
                        databaseClient.getTfPassword()));
    }
}
