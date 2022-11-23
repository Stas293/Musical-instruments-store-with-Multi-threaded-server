package org.project.db.client;

import org.project.db.model.Instrument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class ShowAllInstrumentsListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ShowAllInstrumentsListener.class.getName());

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
            MakeOrderListener.addTableLabels(infoPanel);
            for (Instrument instrument : instruments) addInfoInstrumentToPanel(infoPanel, instrument);
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(600, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.SEVERE, e1.getMessage(), e1);
        }
    }

    private static void addInfoInstrumentToPanel(JPanel infoPanel, Instrument instrument) {
        ShowHistoryOrdersListener.idTitle(infoPanel, instrument.getId(), instrument.getTitle(), null);
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
}
