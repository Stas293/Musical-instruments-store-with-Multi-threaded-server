package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.view_service.DisableUserSendServer;
import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisableUserListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(DisableUserListener.class.getName());
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public DisableUserListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            java.util.List<UserDto> users = getUserDtos();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(users.size(), 1));
            for (UserDto userDto : users) {
                JButton btDisable = new JButton("Disable " + userDto.login());
                infoPanel.add(btDisable);
                btDisable.addActionListener(new DisableUserSendServer(btDisable, fromServer, toServer));
            }
            EnableUserListener.addScroller(users, mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while getting users", e1);
        }
    }

    private java.util.List<UserDto> getUserDtos() throws IOException, ClassNotFoundException {
        toServer.writeObject("allUserDtos");
        Object object = fromServer.readObject();
        return (java.util.List<UserDto>) object;
    }

}
