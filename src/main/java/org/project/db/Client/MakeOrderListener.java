package org.project.db.Client;

import org.project.db.Dto.OrderDto;
import org.project.db.Model.Instrument;
import org.project.db.Model.InstrumentOrder;
import org.project.db.Model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

class MakeOrderListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public MakeOrderListener(DatabaseClient databaseClient) {
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
            ArrayList<Instrument> originalInstruments = (ArrayList<Instrument>) databaseClient.fromServer.readObject();
            ArrayList<Instrument> instruments = new ArrayList<>();
            for (Instrument originalInstrument : originalInstruments) {
                if (originalInstrument.getStatus().getName().equals("Available")) {
                    instruments.add(originalInstrument);
                }
            }
            infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6 + 1));
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
            JLabel lbQuantity = new JLabel("quantity");
            infoPanel.add(lbQuantity);
            JComboBox<Integer> cbQuantity = new JComboBox<>();
            for (int i = 0; i <= 10; i++) {
                cbQuantity.addItem(i);
            }
            JComboBox[] cbQuantitys = new JComboBox[instruments.size()];
            for (int i = 0; i < instruments.size(); i++) {
                JLabel lbId1 = new JLabel(instruments.get(i).getId() + "");
                infoPanel.add(lbId1);
                lbId1.setPreferredSize(new Dimension(1, 1));
                JLabel lbTitle1 = new JLabel(instruments.get(i).getTitle());
                infoPanel.add(lbTitle1);
                lbTitle1.setPreferredSize(new Dimension(1, 1));
                JLabel lbDescription1 = new JLabel(instruments.get(i).getDescription());
                infoPanel.add(lbDescription1);
                lbDescription1.setPreferredSize(new Dimension(1, 1));
                JLabel lbStatus1 = new JLabel(instruments.get(i).getStatus().getName());
                infoPanel.add(lbStatus1);
                lbStatus1.setPreferredSize(new Dimension(1, 1));
                JLabel lbPrice1 = new JLabel(instruments.get(i).getPrice() + "");
                infoPanel.add(lbPrice1);
                lbPrice1.setPreferredSize(new Dimension(1, 1));
                JLabel lbDateUpdated1 = new JLabel(instruments.get(i).getDateUpdated() + "");
                infoPanel.add(lbDateUpdated1);
                lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
                cbQuantitys[i] = new JComboBox<>();
                for (int j = 0; j <= 10; j++) {
                    cbQuantitys[i].addItem(j);
                }
                infoPanel.add(cbQuantitys[i]);
            }
            JScrollPane scrollPanel = new JScrollPane(infoPanel);
            ArrayList<InstrumentOrder> instrumentOrders = new ArrayList<>();
            JPanel controlPanel = new JPanel();
            JButton btMakeOrder = new JButton("Make order");
            btMakeOrder.addActionListener((event) -> {
                        try {
                            for (int i = 0; i < instruments.size(); i++) {
                                if ((Integer) cbQuantitys[i].getSelectedItem() > 0) {
                                    instrumentOrders.add(new InstrumentOrder(instruments.get(i), instruments.get(i).getPrice(), (Integer) cbQuantitys[i].getSelectedItem()));
                                }
                            }
                            if (instrumentOrders.size() > 0) {
                                databaseClient.toServer.writeObject("makeOrder");
                                Status nextstatus = (Status) databaseClient.fromServer.readObject();
                                databaseClient.toServer.writeObject(new OrderDto(databaseClient.user.getLogin() + " " + new Date(), databaseClient.user.getLogin(), nextstatus));
                                databaseClient.toServer.writeObject(instrumentOrders);
                                System.out.println(databaseClient.fromServer.readObject());
                                databaseClient.loggedInUser();
                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
            );
            mainPanel.add(scrollPanel, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            controlPanel.add(btBack);
            controlPanel.add(btMakeOrder);
            mainPanel.add(controlPanel, BorderLayout.SOUTH);
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
