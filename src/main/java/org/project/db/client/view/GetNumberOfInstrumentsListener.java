package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetNumberOfInstrumentsListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(GetNumberOfInstrumentsListener.class.getName());
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public GetNumberOfInstrumentsListener(DatabaseClient databaseClient, ObjectOutputStream toServer,
                                          ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("getNumberOfInstruments");
            int numberOfInstruments = (int) fromServer.readObject();
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(1, 1));
            JLabel lbNumberOfInstruments = new JLabel("Number of instruments: " + numberOfInstruments);
            addButtonRepaint(mainPanel, infoPanel, lbNumberOfInstruments);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while getting number of instruments", e1);
        }
    }

    private void addButtonRepaint(JPanel mainPanel, JPanel infoPanel, JLabel lbNumberOfInstruments) {
        infoPanel.add(lbNumberOfInstruments);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JButton btBack = new JButton(MainConstants.BACK);
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 150);
        databaseClient.repaint();
    }
}
