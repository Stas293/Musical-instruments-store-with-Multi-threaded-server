package org.project.db.Client;

import org.project.db.Model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class GetStatusesListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public GetStatusesListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.toServer.writeObject("getStatuses");
            ArrayList<Status> statuses = (ArrayList<Status>) databaseClient.fromServer.readObject();
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(6, 4));
            JLabel lbId = new JLabel("id");
            infoPanel.add(lbId);
            JLabel lbCode = new JLabel("code");
            infoPanel.add(lbCode);
            JLabel lbName = new JLabel("description");
            infoPanel.add(lbName);
            JLabel lbClosed = new JLabel("closed");
            infoPanel.add(lbClosed);
            for (Status status : statuses) {
                JLabel lbId1 = new JLabel(String.valueOf(status.getId()));
                infoPanel.add(lbId1);
                JLabel lbCode1 = new JLabel(status.getCode());
                infoPanel.add(lbCode1);
                JLabel lbName1 = new JLabel(status.getName());
                infoPanel.add(lbName1);
                JLabel lbClosed1 = new JLabel(status.isClosed() ? "true" : "false");
                infoPanel.add(lbClosed1);
            }
            mainPanel.add(infoPanel, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(400, 400);
            databaseClient.repaint();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
