package org.project.db.client.view;

import org.project.db.client.constants.MainConstants;
import org.project.db.client.controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class ModifyProfileListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    private static final Logger logger = Logger.getLogger(ModifyProfileListener.class.getName());

    public ModifyProfileListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
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
        btChangeEmail.addActionListener(new ActionEmail());
        btChangeFirstName.addActionListener(new ActionFirstName());
        btChangeLastName.addActionListener(new ActionLastName());
        btChangePhone.addActionListener(new ActionChangePhone());
        btChangePassword.addActionListener(new ActionChangePassword());
    }

    private void addButtonRepaint(JPanel mainPanel1, JPanel infoPanel1, JButton btChangeEmail1) {
        infoPanel1.add(btChangeEmail1);
        mainPanel1.add(infoPanel1, BorderLayout.CENTER);
        JButton btBack1 = new JButton(MainConstants.BACK);
        btBack1.addActionListener((event1) -> databaseClient.loggedInUser());
        mainPanel1.add(btBack1, BorderLayout.SOUTH);
        databaseClient.add(mainPanel1);
        databaseClient.setSize(400, 150);
        databaseClient.repaint();
    }

    private class ActionEmail implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfEmail = new JTextField();
            infoPanel1.add(tfEmail);
            JButton btChangeEmail1 = new JButton("Change email");
            addButtonRepaint(mainPanel1, infoPanel1, btChangeEmail1);
            btChangeEmail1.addActionListener(new SendEmail(tfEmail, toServer, fromServer, databaseClient));
        }
    }

    private class ActionFirstName implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfFirstName = new JTextField();
            infoPanel1.add(tfFirstName);
            JButton btChangeFirstName1 = new JButton("Change first name");
            addButtonRepaint(mainPanel1, infoPanel1, btChangeFirstName1);
            btChangeFirstName1.addActionListener(new SendFirstName(tfFirstName, fromServer, toServer, databaseClient));
        }
    }

    private class ActionLastName implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfLastName = new JTextField();
            infoPanel1.add(tfLastName);
            JButton btChangeLastName1 = new JButton("Change last name");
            addButtonRepaint(mainPanel1, infoPanel1, btChangeLastName1);
            btChangeLastName1.addActionListener(new SendLastName(tfLastName, toServer, fromServer, databaseClient));
        }
    }

    private class ActionChangePhone implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfPhone = new JTextField();
            infoPanel1.add(tfPhone);
            JButton btChangePhone1 = new JButton("Change phone");
            addButtonRepaint(mainPanel1, infoPanel1, btChangePhone1);
            btChangePhone1.addActionListener(new SendPhone(tfPhone, toServer, fromServer, databaseClient));
        }
    }

    private class ActionChangePassword implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel1 = new JPanel();
            mainPanel1.setLayout(new BorderLayout());
            JPanel infoPanel1 = new JPanel();
            infoPanel1.setLayout(new GridLayout(1, 1));
            JTextField tfPassword = new JTextField();
            infoPanel1.add(tfPassword);
            JButton btChangePassword1 = new JButton("Change password");
            addButtonRepaint(mainPanel1, infoPanel1, btChangePassword1);
            btChangePassword1.addActionListener(new SendPassword(tfPassword, databaseClient, toServer, fromServer));
        }
    }

}
