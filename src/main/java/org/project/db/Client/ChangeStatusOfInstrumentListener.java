package org.project.db.Client;

import org.project.db.Model.Instrument;
import org.project.db.Model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class ChangeStatusOfInstrumentListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
            JButton btBack = new JButton("Back");
            JPanel actionPanel = new JPanel();
            actionPanel.add(btBack);
            btSearch.addActionListener((event) -> {
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
                                infoPanel.setLayout(new GridLayout(2, 7));
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
                                JLabel lbNewStatus = new JLabel("newStatus");
                                infoPanel.add(lbNewStatus);
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
                                JComboBox<Status> cbNewStatus = new JComboBox<>(statuses.toArray(new Status[statuses.size()]));
                                infoPanel.add(cbNewStatus);
                                JButton btChangeStatus = new JButton("Change status");
                                btChangeStatus.addActionListener((event1) -> {
                                    try {
                                        databaseClient.toServer.writeObject("changeStatusOfInstrument");
                                        databaseClient.toServer.writeObject(instrument);
                                        databaseClient.toServer.writeObject(cbNewStatus.getSelectedItem());
                                        System.out.println(databaseClient.fromServer.readObject());
                                    } catch (IOException | ClassNotFoundException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                });
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
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
            );
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            mainPanel.add(actionPanel, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(400, 200);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
