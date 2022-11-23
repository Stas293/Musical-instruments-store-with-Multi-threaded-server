package org.project.db.client;

import org.project.db.dto.UserDto;
import org.project.db.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class ShowAllOrdersListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ShowAllOrdersListener.class.getName());

    public ShowAllOrdersListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.toServer.writeObject("allUserDtos");
            ArrayList<UserDto> users = (ArrayList<UserDto>) databaseClient.fromServer.readObject();
            ArrayList<Order> orders = new ArrayList<>();
            for (UserDto user : users) {
                databaseClient.toServer.writeObject("getOrderByUser");
                databaseClient.toServer.writeObject(user);
                orders.addAll((ArrayList<Order>) databaseClient.fromServer.readObject());
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
            JButton btBack = new JButton(Constants.BACK);
            btBack.addActionListener(event -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(700, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    static void addArrayToPanel(ArrayList<Order> orders, JPanel mainPanel, JPanel infoPanel, JLabel instrumentQuantity) {
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
