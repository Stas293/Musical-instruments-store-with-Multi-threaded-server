package org.project.db.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class GetNumberOfInstrumentsListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
            infoPanel.add(lbNumberOfInstruments);
            mainPanel.add(infoPanel, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(400, 150);
            databaseClient.repaint();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
