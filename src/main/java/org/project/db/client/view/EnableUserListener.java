package org.project.db.client.view;

import org.project.db.client.constants.MainConstants;
import org.project.db.client.controller.EnableUserSendServer;
import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class EnableUserListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    private static final Logger logger = Logger.getLogger(EnableUserListener.class.getName());

    public EnableUserListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    static void addScroller(ArrayList<UserDto> users, JPanel mainPanel, JPanel infoPanel, DatabaseClient databaseClient) {
        changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
        databaseClient.setSize(400, 75 * users.size());
        databaseClient.repaint();
    }

    static void changeMainPanelScroller(JPanel mainPanel, JPanel infoPanel, DatabaseClient databaseClient) {
        JScrollPane scroller = new JScrollPane(infoPanel);
        mainPanel.add(scroller, BorderLayout.CENTER);
        JButton btBack = new JButton(MainConstants.BACK);
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
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
                JButton btEnable = new JButton("Enable " + userDto.getLogin());
                infoPanel.add(btEnable);
                btEnable.addActionListener(new EnableUserSendServer(btEnable, toServer, fromServer));
            }
            addScroller(users, mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while getting users", e1);
        }
    }

}
