package org.project.db.client;

import org.project.db.dto.UserDto;
import org.project.db.dto.UserRoleDto;
import org.project.db.model.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class AddRoleButtonListner implements ActionListener {
    private final DatabaseClient databaseClient;
    private static final Logger logger = Logger.getLogger(AddRoleButtonListner.class.getName());
    private final String login;

    public AddRoleButtonListner(DatabaseClient databaseClient, String login) {
        this.databaseClient = databaseClient;
        this.login = login;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            databaseClient.toServer.writeObject("addRoleForUser");
            databaseClient.toServer.writeObject(new UserDto(login));
            Object object = databaseClient.fromServer.readObject();
            ArrayList<Role> roles = (ArrayList<Role>) object;
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(roles.size(), 1));
            for (Role role : roles) {
                JButton btAddRole = new JButton("Add role " + role.getName());
                infoPanel.add(btAddRole);
                btAddRole.addActionListener(event -> {
                    try {
                        databaseClient.toServer.writeObject("RoleForUser");
                        databaseClient.toServer.writeObject(new UserRoleDto(login, btAddRole.getText().split(" ")[2]));
                        Object object1 = databaseClient.fromServer.readObject();
                        System.out.println(object1);
                        databaseClient.loggedInUser();
                    } catch (IOException | ClassNotFoundException e1) {
                        logger.log(Level.WARNING, "Error while adding role", e1);
                    }
                });
            }
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(400, 75 * roles.size() + 70);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Error while getting roles", ex);
        }
    }
}
