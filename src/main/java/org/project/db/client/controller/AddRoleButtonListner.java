package org.project.db.client.controller;

import org.project.db.client.DatabaseClient;
import org.project.db.client.service.SendUserAndUpdateRole;
import org.project.db.model.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddRoleButtonListner implements ActionListener {
    private final DatabaseClient databaseClient;
    private static final Logger logger = Logger.getLogger(AddRoleButtonListner.class.getName());
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final String login;

    public AddRoleButtonListner(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer, String login) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.login = login;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            SendUserAndUpdateRole sendUserAndUpdateRole = new SendUserAndUpdateRole(databaseClient, toServer, fromServer, login);
            List<Role> roles = sendUserAndUpdateRole.getRoles();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(roles.size(), 1));
            for (Role role : roles) {
                JButton btAddRole = new JButton("Add role " + role.getName());
                infoPanel.add(btAddRole);
                btAddRole.addActionListener(sendUserAndUpdateRole.invoke(btAddRole));
            }
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(400, 75 * roles.size() + 70);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Error while getting roles", ex);
        }
    }


}
