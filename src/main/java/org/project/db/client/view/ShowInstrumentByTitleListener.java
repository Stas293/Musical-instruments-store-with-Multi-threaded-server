package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;
import org.project.db.client.view_service.ActionSearch;
import org.project.db.model.Instrument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class ShowInstrumentByTitleListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ShowInstrumentByTitleListener.class.getName());
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;

    public ShowInstrumentByTitleListener(DatabaseClient databaseClient, ObjectInputStream fromServer, ObjectOutputStream toServer) {
        this.databaseClient = databaseClient;
        this.fromServer = fromServer;
        this.toServer = toServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        JTextField tfTitle = new JTextField();
        controlPanel.add(tfTitle);
        tfTitle.setPreferredSize(new Dimension(100, 30));
        JButton btSearch = new JButton("Search");
        controlPanel.add(btSearch);
        JButton btBack = new JButton(MainConstants.BACK);
        btSearch.addActionListener(new ActionSearch(mainPanel, controlPanel, tfTitle, btBack, toServer, fromServer, databaseClient));
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }

    public static void addLabelConstants(JPanel infoPanel) {
        CreateOrderListener.addIdTitleDescription(infoPanel);
        CreateOrderListener.addIdPriceDateUpdatedToInfoPanel(infoPanel);
    }

    public static void printInstrument(Instrument instrument, JPanel infoPanel) {
        ShowHistoryOrdersListener.idTitle(infoPanel, instrument.getId(), instrument.getTitle());
        JLabel lbDescription1 = new JLabel(instrument.getDescription());
        infoPanel.add(lbDescription1);
        addStatusPriceDateUpdatedToInfoPanel(instrument, infoPanel, lbDescription1);
    }

    static void addStatusPriceDateUpdatedToInfoPanel(Instrument instrument, JPanel infoPanel, JLabel lbDescription1) {
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
