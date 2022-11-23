package org.project.db.client;

import org.project.db.model.Instrument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Logger;

class ShowInstrumentByTitleListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ShowInstrumentByTitleListener.class.getName());

    public ShowInstrumentByTitleListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
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
        JButton btBack = new JButton(Constants.BACK);
        btSearch.addActionListener(new ActionSearch(mainPanel, controlPanel, tfTitle, btBack).invoke());
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }

    static void addLabelConstants(JPanel infoPanel) {
        MakeOrderListener.addIdTitleDescription(infoPanel);
        JLabel lbStatus = new JLabel(LabelConstants.STATUS);
        infoPanel.add(lbStatus);
        lbStatus.setPreferredSize(new Dimension(1, 1));
        JLabel lbPrice = new JLabel(LabelConstants.PRICE);
        infoPanel.add(lbPrice);
        lbPrice.setPreferredSize(new Dimension(1, 1));
        JLabel lbDateUpdated = new JLabel(LabelConstants.DATE_UPDATED);
        infoPanel.add(lbDateUpdated);
    }

    static void printInstrument(Instrument instrument, JPanel infoPanel) {
        ShowHistoryOrdersListener.idTitle(infoPanel, instrument.getId(), instrument.getTitle(), null);
        JLabel lbDescription1 = new JLabel(instrument.getDescription());
        infoPanel.add(lbDescription1);
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

    private class ActionSearch {
        private final JPanel mainPanel;
        private final JPanel controlPanel;
        private final JTextField tfTitle;
        private final JButton btBack;

        public ActionSearch(JPanel mainPanel, JPanel controlPanel, JTextField tfTitle, JButton btBack) {
            this.mainPanel = mainPanel;
            this.controlPanel = controlPanel;
            this.tfTitle = tfTitle;
            this.btBack = btBack;
        }

        public ActionListener invoke() {
            return (event) -> {
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
                    if (instrument != null) {
                        JPanel infoPanel = new JPanel();
                        infoPanel.setLayout(new GridLayout(2, 6));
                        addLabelConstants(infoPanel);
                        printInstrument(instrument, infoPanel);
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
                } catch (IOException | ClassNotFoundException e) {
                    logger.info(e.getMessage());
                }
            };
        }
    }
}
