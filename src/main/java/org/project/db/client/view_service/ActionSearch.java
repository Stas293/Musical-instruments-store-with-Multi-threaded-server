package org.project.db.client.view_service;

import org.project.db.client.DatabaseClient;
import org.project.db.client.view.ShowInstrumentByTitleListener;
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

public class ActionSearch implements ActionListener {
    private static final Logger logger = Logger.getLogger(ActionSearch.class.getName());
    private final JPanel mainPanel;
    private final JPanel controlPanel;
    private final JTextField tfTitle;
    private final JButton btBack;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final DatabaseClient databaseClient;

    public ActionSearch(JPanel mainPanel, JPanel controlPanel, JTextField tfTitle, JButton btBack, ObjectOutputStream toServer, ObjectInputStream fromServer, DatabaseClient databaseClient) {
        this.mainPanel = mainPanel;
        this.controlPanel = controlPanel;
        this.tfTitle = tfTitle;
        this.btBack = btBack;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String title;
            toServer.writeObject("findByTitle");
            if (!tfTitle.getText().trim().isEmpty()) {
                title = tfTitle.getText()
                        .trim()
                        .substring(0, 1)
                        .toUpperCase()
                        + tfTitle.getText()
                        .trim().substring(1)
                        .toLowerCase();
            } else {
                title = "";
            }
            toServer.writeObject(title);
            Instrument instrument = (Instrument) fromServer.readObject();
            mainPanel.removeAll();
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.setSize(400, 200);
            databaseClient.add(mainPanel);
            if (instrument != null) {
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(2, 6));
                ShowInstrumentByTitleListener.addLabelConstants(infoPanel);
                ShowInstrumentByTitleListener.printInstrument(instrument, infoPanel);
                mainPanel.add(infoPanel, BorderLayout.CENTER);
                databaseClient.add(mainPanel);
                databaseClient.setSize(600, 300);
                databaseClient.repaint();
            } else {
                mainPanel.removeAll();
                mainPanel.add(controlPanel, BorderLayout.NORTH);
                mainPanel.add(btBack, BorderLayout.SOUTH);
                databaseClient.setSize(400, 200);
                databaseClient.add(mainPanel);
                databaseClient.repaint();
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, ex.getMessage(), ex);
        }
    }
}
