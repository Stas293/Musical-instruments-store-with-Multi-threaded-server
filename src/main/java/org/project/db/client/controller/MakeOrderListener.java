package org.project.db.client.controller;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.LabelConstants;
import org.project.db.client.constants.MainConstants;
import org.project.db.client.service.AddInstrumentsToOrder;
import org.project.db.model.Instrument;
import org.project.db.model.InstrumentOrder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MakeOrderListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    private static final Logger logger = Logger.getLogger(MakeOrderListener.class.getName());

    public MakeOrderListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            toServer.writeObject("getAllInstruments");
            ArrayList<Instrument> originalInstruments = (ArrayList<Instrument>) fromServer.readObject();
            ArrayList<Instrument> instruments = originalInstruments.stream().filter(originalInstrument ->
                    originalInstrument.getStatus().getName().equals("Available")).collect(Collectors.toCollection(ArrayList::new));
            infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6 + 1));
            addTableLabels(infoPanel);
            JLabel lbQuantity = new JLabel("quantity");
            infoPanel.add(lbQuantity);
            JComboBox<Integer> cbQuantity = new JComboBox<>();
            IntStream.rangeClosed(0, 10).forEachOrdered(cbQuantity::addItem);
            JComboBox[] cbQuantitys = new JComboBox[instruments.size()];
            addInstrumentsToTable(infoPanel, instruments, cbQuantitys);
            JScrollPane scrollPanel = new JScrollPane(infoPanel);
            ArrayList<InstrumentOrder> instrumentOrders = new ArrayList<>();
            JPanel controlPanel = new JPanel();
            JButton btMakeOrder = new JButton("Make order");
            btMakeOrder.addActionListener(new AddInstrumentsToOrder(instruments, cbQuantitys, instrumentOrders, fromServer, toServer, databaseClient));
            mainPanel.add(scrollPanel, BorderLayout.CENTER);
            JButton btBack = new JButton(MainConstants.BACK);
            btBack.addActionListener(event -> databaseClient.loggedInUser());
            controlPanel.add(btBack);
            controlPanel.add(btMakeOrder);
            mainPanel.add(controlPanel, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(600, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Exception", e1);
        }
    }

    private static void addInstrumentsToTable(JPanel infoPanel, ArrayList<Instrument> instruments, JComboBox[] cbQuantitys) {
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
    }

    static void addTableLabels(JPanel infoPanel) {
        addIdTitleDescription(infoPanel);
        addIdPriceDateUpdatedToInfoPanel(infoPanel);
    }

    static void addIdPriceDateUpdatedToInfoPanel(JPanel infoPanel) {
        JLabel lbStatus = new JLabel(LabelConstants.STATUS);
        infoPanel.add(lbStatus);
        lbStatus.setPreferredSize(new Dimension(1, 1));
        JLabel lbPrice = new JLabel(LabelConstants.PRICE);
        infoPanel.add(lbPrice);
        lbPrice.setPreferredSize(new Dimension(1, 1));
        JLabel lbDateUpdated = new JLabel(LabelConstants.DATE_UPDATED);
        infoPanel.add(lbDateUpdated);
    }

    static void addIdTitleDescription(JPanel infoPanel) {
        JLabel lbId = new JLabel(LabelConstants.ID);
        infoPanel.add(lbId);
        lbId.setPreferredSize(new Dimension(1, 1));
        JLabel lbTitle = new JLabel(LabelConstants.TITLE);
        infoPanel.add(lbTitle);
        lbTitle.setPreferredSize(new Dimension(1, 1));
        JLabel lbDescription = new JLabel(LabelConstants.DESCRIPTION);
        infoPanel.add(lbDescription);
        lbDescription.setPreferredSize(new Dimension(1, 1));
    }

}
