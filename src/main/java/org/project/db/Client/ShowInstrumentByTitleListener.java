package org.project.db.Client;

import org.project.db.Model.Instrument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class ShowInstrumentByTitleListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
        JButton btBack = new JButton("Back");
        btSearch.addActionListener((event) -> {
                    try {
                        String title;
                        databaseClient.toServer.writeObject("getInstrumentByTitle");
                        if (!tfTitle.getText().trim().isEmpty()) {
                            title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
                        } else {
                            title = "";
                        }
                        //title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
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
                            JLabel lbId = new JLabel("id");
                            infoPanel.add(lbId);
                            lbId.setPreferredSize(new Dimension(1, 1));
                            JLabel lbTitle = new JLabel("title");
                            infoPanel.add(lbTitle);
                            lbTitle.setPreferredSize(new Dimension(1, 1));
                            JLabel lbDescription = new JLabel("description");
                            infoPanel.add(lbDescription);
                            lbDescription.setPreferredSize(new Dimension(1, 1));
                            JLabel lbStatus = new JLabel("status");
                            infoPanel.add(lbStatus);
                            lbStatus.setPreferredSize(new Dimension(1, 1));
                            JLabel lbPrice = new JLabel("price");
                            infoPanel.add(lbPrice);
                            lbPrice.setPreferredSize(new Dimension(1, 1));
                            JLabel lbDateUpdated = new JLabel("dateUpdated");
                            infoPanel.add(lbDateUpdated);
                            JLabel lbId1 = new JLabel(instrument.getId() + "");
                            infoPanel.add(lbId1);
                            lbId1.setPreferredSize(new Dimension(1, 1));
                            JLabel lbTitle1 = new JLabel(instrument.getTitle());
                            infoPanel.add(lbTitle1);
                            lbTitle1.setPreferredSize(new Dimension(1, 1));
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
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }
}
