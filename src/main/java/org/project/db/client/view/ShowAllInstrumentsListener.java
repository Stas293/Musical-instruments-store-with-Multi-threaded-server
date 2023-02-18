package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.model.Instrument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowAllInstrumentsListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(ShowAllInstrumentsListener.class.getName());
    private final DatabaseClient databaseClient;
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;

    public ShowAllInstrumentsListener(DatabaseClient databaseClient, ObjectInputStream fromServer, ObjectOutputStream toServer) {
        this.databaseClient = databaseClient;
        this.fromServer = fromServer;
        this.toServer = toServer;
    }

    private static void addInfoInstrumentToPanel(JPanel infoPanel, Instrument instrument) {
        ShowHistoryOrdersListener.idTitle(infoPanel, instrument.getId(), instrument.getTitle());
        JLabel lbDescription1 = new JLabel(instrument.getDescription());
        infoPanel.add(lbDescription1);
        lbDescription1.setMaximumSize(new Dimension(1, 1));
        ShowInstrumentByTitleListener.addStatusPriceDateUpdatedToInfoPanel(instrument, infoPanel, lbDescription1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            java.util.List<Instrument> instruments = getInstruments();
            infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6));
            CreateOrderListener.addTableLabels(infoPanel);
            for (Instrument instrument : instruments) addInfoInstrumentToPanel(infoPanel, instrument);
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(600, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.SEVERE, e1.getMessage(), e1);
        }
    }

    private java.util.List<Instrument> getInstruments() throws IOException, ClassNotFoundException {
        toServer.writeObject("getAllInstruments");
        return (java.util.List<Instrument>) fromServer.readObject();
    }
}
