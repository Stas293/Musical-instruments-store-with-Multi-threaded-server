package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.LabelConstants;
import org.project.db.client.constants.MainConstants;
import org.project.db.model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetStatusesListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(GetStatusesListener.class.getName());
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public GetStatusesListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    static void addBackRepaint(JPanel mainPanel, JPanel infoPanel, DatabaseClient databaseClient) {
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JButton btBack = new JButton(MainConstants.BACK);
        btBack.addActionListener(event -> databaseClient.loggedInUser());
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 400);
        databaseClient.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("getStatuses");
            java.util.List<Status> statuses = (java.util.List<Status>) fromServer.readObject();
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(6, 4));
            JLabel lbId = new JLabel(LabelConstants.ID);
            infoPanel.add(lbId);
            JLabel lbCode = new JLabel("code");
            infoPanel.add(lbCode);
            JLabel lbName = new JLabel(LabelConstants.DESCRIPTION);
            infoPanel.add(lbName);
            JLabel lbClosed = new JLabel("closed");
            infoPanel.add(lbClosed);
            statuses.forEach(status -> {
                JLabel lbId1 = new JLabel(String.valueOf(status.getId()));
                infoPanel.add(lbId1);
                JLabel lbCode1 = new JLabel(status.getCode());
                infoPanel.add(lbCode1);
                JLabel lbName1 = new JLabel(status.getName());
                infoPanel.add(lbName1);
                JLabel lbClosed1 = new JLabel(status.isClosed() ? "true" : "false");
                infoPanel.add(lbClosed1);
            });
            addBackRepaint(mainPanel, infoPanel, databaseClient);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while getting statuses", e1);
        }
    }
}
