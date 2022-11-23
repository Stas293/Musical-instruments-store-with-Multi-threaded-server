package org.project.db.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class GetNumberOfInstrumentsListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(GetNumberOfInstrumentsListener.class.getName());

    public GetNumberOfInstrumentsListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.toServer.writeObject("getNumberOfInstruments");
            int numberOfInstruments = (int) databaseClient.fromServer.readObject();
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
        JButton btBack = new JButton(Constants.BACK);
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 150);
        databaseClient.repaint();
    }
}
