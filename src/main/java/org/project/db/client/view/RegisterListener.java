package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;
import org.project.db.client.view_service.RegisterButtonListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

public class RegisterListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;

    public RegisterListener(DatabaseClient databaseClient, ObjectOutputStream toServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 2));
        infoPanel.add(new JLabel(MainConstants.LOGIN));
        databaseClient.setTfLogin(new JTextField(20));
        infoPanel.add(databaseClient.getTfLogin());
        infoPanel.add(new JLabel("Email"));
        JTextField tfEmail = new JTextField(20);
        infoPanel.add(tfEmail);
        infoPanel.add(new JLabel("First Name"));
        JTextField tfFirstName = new JTextField(20);
        infoPanel.add(tfFirstName);
        infoPanel.add(new JLabel("Last Name"));
        JTextField tfLastName = new JTextField(20);
        infoPanel.add(tfLastName);
        infoPanel.add(new JLabel("Phone"));
        JTextField tfPhone = new JTextField(20);
        infoPanel.add(tfPhone);
        infoPanel.add(new JLabel("Password"));
        infoPanel.add(new JTextField(20));
        JButton btRegister = new JButton(MainConstants.REGISTER);
        JButton btBack = databaseClient.getStartView().registerMainPanel(infoPanel, mainPanel, btRegister);
        databaseClient.setSize(450, 300);
        databaseClient.repaint();
        btBack.addActionListener(event -> {
            databaseClient.getContentPane().removeAll();
            databaseClient.getStartView().startView();
        });
        btRegister.addActionListener(new RegisterButtonListener(databaseClient, toServer,
                databaseClient.getTfLogin(), tfEmail, tfFirstName, tfLastName,
                tfPhone, databaseClient.getTfPassword()));
    }

}
