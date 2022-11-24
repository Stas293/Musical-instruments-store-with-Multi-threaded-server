package org.project.db.client.view;

import org.project.db.client.constants.MainConstants;
import org.project.db.client.controller.ChangeInstrumentGetByTitle;
import org.project.db.model.Instrument;
import org.project.db.model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ChangeStatusOfInstrumentListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private static final Logger logger = Logger.getLogger(ChangeStatusOfInstrumentListener.class.getName());

    public ChangeStatusOfInstrumentListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("getStatuses");
            ArrayList<Status> statuses = (ArrayList<Status>) fromServer.readObject();
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
            JPanel actionPanel = new JPanel();
            actionPanel.add(btBack);
            btSearch.addActionListener(event -> new GetInstrumentAndChangeStatus(statuses, mainPanel, controlPanel, tfTitle, btBack, actionPanel));
            btBack.addActionListener(event -> databaseClient.loggedInUser());
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            mainPanel.add(actionPanel, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(400, 200);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Error while getting statuses", ex);
        }
    }

    private class GetInstrumentAndChangeStatus implements ActionListener {
        private final List<Status> statuses;
        private final JPanel mainPanel;
        private final JPanel controlPanel;
        private final JTextField tfTitle;
        private final JButton btBack;
        private final JPanel actionPanel;
        private final ChangeInstrumentGetByTitle changeInstrumentGetByTitle = new ChangeInstrumentGetByTitle(toServer, fromServer);

        public GetInstrumentAndChangeStatus(List<Status> statuses, JPanel mainPanel, JPanel controlPanel, JTextField tfTitle, JButton btBack, JPanel actionPanel) {
            this.statuses = statuses;
            this.mainPanel = mainPanel;
            this.controlPanel = controlPanel;
            this.tfTitle = tfTitle;
            this.btBack = btBack;
            this.actionPanel = actionPanel;
        }

        private void changeInstrumentIfExist(Instrument instrument) {
            if (instrument != null) {
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(2, 7));
                ShowInstrumentByTitleListener.addLabelConstants(infoPanel);
                JLabel lbNewStatus = new JLabel("newStatus");
                infoPanel.add(lbNewStatus);
                ShowInstrumentByTitleListener.printInstrument(instrument, infoPanel);
                JComboBox<Status> cbNewStatus = new JComboBox<>(statuses.toArray(new Status[0]));
                infoPanel.add(cbNewStatus);
                JButton btChangeStatus = new JButton("Change status");
                changeInstrumentGetByTitle.addActionToSendNewStatus(instrument, cbNewStatus, btChangeStatus);
                actionPanel.removeAll();
                actionPanel.add(btBack);
                actionPanel.add(btChangeStatus);
                mainPanel.add(actionPanel, BorderLayout.SOUTH);
                mainPanel.add(infoPanel, BorderLayout.CENTER);
                databaseClient.add(mainPanel);
                databaseClient.setSize(600, 300);
                databaseClient.repaint();
            } else {
                mainPanel.removeAll();
                actionPanel.removeAll();
                actionPanel.add(btBack);
                mainPanel.add(controlPanel, BorderLayout.NORTH);
                mainPanel.add(actionPanel, BorderLayout.SOUTH);
                databaseClient.setSize(400, 200);
                databaseClient.add(mainPanel);
                databaseClient.repaint();
            }
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Instrument instrument = changeInstrumentGetByTitle.getInstrumentsByTitle(tfTitle);
                mainPanel.removeAll();
                mainPanel.add(controlPanel, BorderLayout.NORTH);
                mainPanel.add(btBack, BorderLayout.SOUTH);
                databaseClient.setSize(400, 200);
                databaseClient.add(mainPanel);
                changeInstrumentIfExist(instrument);
            } catch (IOException | ClassNotFoundException e1) {
                logger.log(Level.WARNING, "Error while searching instrument by title", e1);
            }
        }
    }
}

