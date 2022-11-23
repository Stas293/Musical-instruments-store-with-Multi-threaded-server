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

class EnableUserListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(EnableUserListener.class.getName());

    public EnableUserListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    static void addScroller(ArrayList<UserDto> users, JPanel mainPanel, JPanel infoPanel, DatabaseClient databaseClient) {
        changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
        databaseClient.setSize(400, 75 * users.size());
        databaseClient.repaint();
    }

    static void changeMainPanelScroller(JPanel mainPanel, JPanel infoPanel, DatabaseClient databaseClient) {
        JScrollPane scroller = new JScrollPane(infoPanel);
        mainPanel.add(scroller, BorderLayout.CENTER);
        JButton btBack = new JButton(Constants.BACK);
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
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
                JButton btEnable = new JButton("Enable " + userDto.getLogin());
                infoPanel.add(btEnable);
                btEnable.addActionListener((event) -> {
                    try {
                        System.out.println(btEnable.getText().split(" ")[1]);
                        databaseClient.toServer.writeObject("reallyEnableUser");
                        databaseClient.toServer.writeObject(new UserDto(btEnable.getText().split(" ")[1]));
                        Object object1 = databaseClient.fromServer.readObject();
                        System.out.println((String) object1);
                    } catch (IOException | ClassNotFoundException e1) {
                        logger.log(Level.WARNING, "Error while enabling user", e1);
                    }
                });
            }
            addScroller(users, mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while getting users", e1);
        }
    }
}
