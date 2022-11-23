package org.project.db.client;

import org.project.db.model.Instrument;
import org.project.db.model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ChangeStatusOfInstrumentListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private static final Logger logger = Logger.getLogger(ChangeStatusOfInstrumentListener.class.getName());

    public ChangeStatusOfInstrumentListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.toServer.writeObject("getStatuses");
            ArrayList<Status> statuses = (ArrayList<Status>) databaseClient.fromServer.readObject();
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel controlPanel = new JPanel();
            JTextField tfTitle = new JTextField();
            controlPanel.add(tfTitle);
            tfTitle.setPreferredSize(new Dimension(100, 30));
            JButton btSearch = new JButton("Search");
            controlPanel.add(btSearch);
            JButton btBack = new JButton(Constants.BACK);
            JPanel actionPanel = new JPanel();
            actionPanel.add(btBack);
            btSearch.addActionListener(event -> new GetInstrumentAndChangeStatus(statuses, mainPanel, controlPanel, tfTitle, btBack, actionPanel).invoke());
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

    private class GetInstrumentAndChangeStatus {
        private final List<Status> statuses;
        private final JPanel mainPanel;
        private final JPanel controlPanel;
        private final JTextField tfTitle;
        private final JButton btBack;
        private final JPanel actionPanel;

        public GetInstrumentAndChangeStatus(List<Status> statuses, JPanel mainPanel, JPanel controlPanel, JTextField tfTitle, JButton btBack, JPanel actionPanel) {
            this.statuses = statuses;
            this.mainPanel = mainPanel;
            this.controlPanel = controlPanel;
            this.tfTitle = tfTitle;
            this.btBack = btBack;
            this.actionPanel = actionPanel;
        }

        public void invoke() {
            try {
                String title;
                databaseClient.toServer.writeObject("getInstrumentByTitle");
                if (!tfTitle.getText().trim().isEmpty()) {
                    title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
                } else {
                    title = "";
                }
                databaseClient.toServer.writeObject(title);
                Instrument instrument = (Instrument) databaseClient.fromServer.readObject();
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
                addActionToSendNewStatus(instrument, cbNewStatus, btChangeStatus);
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

        private void addActionToSendNewStatus(Instrument instrument, JComboBox<Status> cbNewStatus, JButton btChangeStatus) {
            btChangeStatus.addActionListener(event1 -> {
                try {
                    databaseClient.toServer.writeObject("changeStatusOfInstrument");
                    databaseClient.toServer.writeObject(instrument);
                    databaseClient.toServer.writeObject(cbNewStatus.getSelectedItem());
                    System.out.println(databaseClient.fromServer.readObject());
                } catch (IOException | ClassNotFoundException ex) {
                    logger.log(Level.WARNING, "Error while changing status of instrument", ex);
                }
            });
        }
    }
}
