package org.project.db.Client;

import org.project.db.Model.Instrument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class ShowAllInstrumentsListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public ShowAllInstrumentsListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            databaseClient.toServer.writeObject("getAllInstruments");
            ArrayList<Instrument> instruments = (ArrayList<Instrument>) databaseClient.fromServer.readObject();
            infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6));
            JLabel lbId = new JLabel("id");
            infoPanel.add(lbId);
            lbId.setPreferredSize(new Dimension(1, 1));
            JLabel lbTitle = new JLabel("title");
            infoPanel.add(lbTitle);
            lbTitle.setPreferredSize(new Dimension(1, 1));
            JLabel lbDescription = new JLabel("description");
            infoPanel.add(lbDescription);
            lbDescription.setPreferredSize(new Dimension(1, 1));
            lbDescription.setMaximumSize(new Dimension(1, 1));
            lbDescription.setPreferredSize(new Dimension(1, 1));
            JLabel lbStatus = new JLabel("status");
            infoPanel.add(lbStatus);
            lbStatus.setPreferredSize(new Dimension(1, 1));
            JLabel lbPrice = new JLabel("price");
            infoPanel.add(lbPrice);
            lbPrice.setPreferredSize(new Dimension(1, 1));
            JLabel lbDateUpdated = new JLabel("dateUpdated");
            infoPanel.add(lbDateUpdated);
            for (Instrument instrument : instruments) {
                JLabel lbId1 = new JLabel(instrument.getId() + "");
                infoPanel.add(lbId1);
                lbId1.setPreferredSize(new Dimension(1, 1));
                JLabel lbTitle1 = new JLabel(instrument.getTitle());
                infoPanel.add(lbTitle1);
                lbTitle1.setPreferredSize(new Dimension(1, 1));
                JLabel lbDescription1 = new JLabel(instrument.getDescription());
                infoPanel.add(lbDescription1);
                lbDescription1.setMaximumSize(new Dimension(1, 1));
                lbDescription1.setPreferredSize(new Dimension(1, 1));
                JLabel lbStatus1 = new JLabel(instrument.getStatus().getName());
                infoPanel.add(lbStatus1);
                lbStatus1.setPreferredSize(new Dimension(1, 1));
                JLabel lbPrice1 = new JLabel(instrument.getPrice() + "");
                infoPanel.add(lbPrice1);
                lbPrice1.setPreferredSize(new Dimension(1, 1));
                JLabel lbDateUpdated1 = new JLabel(instrument.getDateUpdated() + "");
                infoPanel.add(lbDateUpdated1);
                lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
            }
            JScrollPane scrollPanel = new JScrollPane(infoPanel);
            mainPanel.add(scrollPanel, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(600, 300);
            databaseClient.repaint();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
