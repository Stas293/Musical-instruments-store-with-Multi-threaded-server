package org.project.db.client;

import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class DisableUserListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(DisableUserListener.class.getName());

    public DisableUserListener(DatabaseClient databaseClient) {
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
                JButton btDisable = new JButton("Disable " + userDto.getLogin());
                infoPanel.add(btDisable);
                btDisable.addActionListener((event) -> {
                    try {
                        System.out.println(btDisable.getText().split(" ")[1]);
                        databaseClient.toServer.writeObject("reallyDisableUser");
                        databaseClient.toServer.writeObject(new UserDto(btDisable.getText().split(" ")[1]));
                        Object object1 = databaseClient.fromServer.readObject();
                        System.out.println((String) object1);
                    } catch (IOException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                });
            }
            EnableUserListener.addScroller(users, mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while getting users", e1);
        }
    }
}
