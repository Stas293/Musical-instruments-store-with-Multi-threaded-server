package org.project.db.Client;

import org.project.db.Dto.UserDto;
import org.project.db.Dto.UserRoleDto;
import org.project.db.Model.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class AddRoleButtonListner implements ActionListener {
    private final DatabaseClient databaseClient;
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
                btAddRole.addActionListener((event) -> {
                    try {
                        databaseClient.toServer.writeObject("RoleForUser");
                        databaseClient.toServer.writeObject(new UserRoleDto(login, btAddRole.getText().split(" ")[2]));
                        Object object1 = databaseClient.fromServer.readObject();
                        System.out.println(object1);
                        databaseClient.loggedInUser();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            JScrollPane scroller = new JScrollPane(infoPanel);
            mainPanel.add(scroller, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(400, 75 * roles.size() + 70);
            databaseClient.repaint();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
