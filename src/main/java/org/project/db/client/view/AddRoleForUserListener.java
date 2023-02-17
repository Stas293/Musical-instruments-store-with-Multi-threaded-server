package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AddRoleForUserListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private static final Logger logger = Logger.getLogger(AddRoleForUserListener.class.getName());

    public AddRoleForUserListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            toServer.writeObject("allUserDtos");
            Object object = fromServer.readObject();
            ArrayList<UserDto> users = (ArrayList<UserDto>) object;
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(users.size(), 1));
            for (UserDto userDto : users) {
                JButton btAddRole = new JButton("Add role for " + userDto.login());
                infoPanel.add(btAddRole);
                btAddRole.addActionListener(new AddRoleButtonListner(databaseClient, toServer, fromServer, btAddRole.getText().split(" ")[3]));
            }
            EnableUserListener.addScroller(users, mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(java.util.logging.Level.WARNING, "Error while getting users", ex);
        }
    }
}
