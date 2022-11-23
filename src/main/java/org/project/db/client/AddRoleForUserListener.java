package org.project.db.client;

import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

class AddRoleForUserListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private static final Logger logger = Logger.getLogger(AddRoleForUserListener.class.getName());

    public AddRoleForUserListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            databaseClient.toServer.writeObject("allUserDtos");
            Object object = databaseClient.fromServer.readObject();
            ArrayList<UserDto> users = (ArrayList<UserDto>) object;
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(users.size(), 1));
            for (UserDto userDto : users) {
                JButton btAddRole = new JButton("Add role for " + userDto.getLogin());
                infoPanel.add(btAddRole);
                btAddRole.addActionListener(new AddRoleButtonListner(databaseClient, btAddRole.getText().split(" ")[3]));
            }
            EnableUserListener.addScroller(users, mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(java.util.logging.Level.WARNING, "Error while getting users", ex);
        }
    }
}
