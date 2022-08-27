package org.project.db.Client;

import org.project.db.Dto.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class AddRoleForUserListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
                btAddRole.addActionListener(new addRoleButtonListner(databaseClient, btAddRole.getText().split(" ")[3]));
            }
            JScrollPane scroller = new JScrollPane(infoPanel);
            mainPanel.add(scroller, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(400, 75 * users.size());
            databaseClient.repaint();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
