package org.project.db.client.controller;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.InstrumentConstants;
import org.project.db.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class ShowOrdersListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public ShowOrdersListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("getAllOrdersForUser");
            toServer.writeObject(databaseClient.getUserDto());
            ArrayList<Order> orders = (ArrayList<Order>) fromServer.readObject();
            System.out.println(orders);
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            int size = orders.stream().mapToInt(value -> (int) IntStream.range(0, value.getInstruments().size()).count()).sum();
            JLabel instrumentQuantity = addJlabels(infoPanel, size);
            infoPanel.add(instrumentQuantity);
            orders.forEach(order -> IntStream.range(0, order.getInstruments().size()).forEachOrdered(j ->
                    ChangeStatusOfOrderListener.addInstrumentsToPanel(infoPanel, order, j)));
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(600, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    static JLabel addJlabels(JPanel infoPanel, int size) {
        ShowAllOrdersListener.orderLayout(infoPanel, size);
        JLabel instrumentTitle = new JLabel(InstrumentConstants.INSTRUMENT_TITLE);
        infoPanel.add(instrumentTitle);
        JLabel instrumentPrice = new JLabel(InstrumentConstants.INSTRUMENT_PRICE);
        infoPanel.add(instrumentPrice);
        return new JLabel(InstrumentConstants.INSTRUMENT_QUANTITY);
    }
}
