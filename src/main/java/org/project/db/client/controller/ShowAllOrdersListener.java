package org.project.db.client.controller;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;
import org.project.db.dto.UserDto;
import org.project.db.model.Order;

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
import java.util.stream.IntStream;

public class ShowAllOrdersListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    private static final Logger logger = Logger.getLogger(ShowAllOrdersListener.class.getName());

    public ShowAllOrdersListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("allUserDtos");
            List<UserDto> users = (List<UserDto>) fromServer.readObject();
            List<Order> orders = new ArrayList<>();
            for (UserDto user : users) {
                toServer.writeObject("getOrderByUser");
                toServer.writeObject(user);
                orders.addAll((List<Order>) fromServer.readObject());
            }
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            int size = (int) orders.stream().flatMapToInt(value -> IntStream.range(0, value.getInstruments().size())).count();
            orderLayout(infoPanel, size);
            JLabel instrumentTitle = new JLabel("Title");
            infoPanel.add(instrumentTitle);
            JLabel instrumentPrice = new JLabel("Price");
            infoPanel.add(instrumentPrice);
            JLabel instrumentQuantity = new JLabel("Quantity");
            addArrayToPanel(orders, mainPanel, infoPanel, instrumentQuantity);
            JButton btBack = new JButton(MainConstants.BACK);
            btBack.addActionListener(event -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(700, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    static void addArrayToPanel(List<Order> orders, JPanel mainPanel, JPanel infoPanel, JLabel instrumentQuantity) {
        infoPanel.add(instrumentQuantity);
        for (Order order : orders) {
            for (int j = 0; j < order.getInstruments().size(); j++) {
                ChangeStatusOfOrderListener.addInstrumentsToPanel(infoPanel, order, j);
            }
        }
        JScrollPane scrollPanel = new JScrollPane(infoPanel);
        mainPanel.add(scrollPanel, BorderLayout.CENTER);
    }

    static void orderLayout(JPanel infoPanel, int size) {
        infoPanel.setLayout(new GridLayout(size + 1, 6));
        JLabel orderTitle = new JLabel("Order title");
        infoPanel.add(orderTitle);
        JLabel orderStatus = new JLabel("Order status");
        infoPanel.add(orderStatus);
        JLabel isClosed = new JLabel("Is closed");
        infoPanel.add(isClosed);
    }
}
