package org.project.db.Client;

import org.project.db.Dto.UserDto;
import org.project.db.Model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class ShowAllOrdersListener implements ActionListener {
    private final DatabaseClient databaseClient;

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
            int size = 0;
            for (Order value : orders) {
                for (int j = 0; j < value.getInstruments().size(); j++) {
                    size++;
                }
            }
            infoPanel.setLayout(new GridLayout(size + 1, 6));
            JLabel orderTitle = new JLabel("Order title");
            infoPanel.add(orderTitle);
            JLabel orderStatus = new JLabel("Order status");
            infoPanel.add(orderStatus);
            JLabel isClosed = new JLabel("Is closed");
            infoPanel.add(isClosed);
            JLabel instrumentTitle = new JLabel("Title");
            infoPanel.add(instrumentTitle);
            JLabel instrumentPrice = new JLabel("Price");
            infoPanel.add(instrumentPrice);
            JLabel instrumentQuantity = new JLabel("Quantity");
            infoPanel.add(instrumentQuantity);
            for (Order order : orders) {
                for (int j = 0; j < order.getInstruments().size(); j++) {
                    JLabel lbOrderTitle = new JLabel(order.getTitle());
                    infoPanel.add(lbOrderTitle);
                    lbOrderTitle.setPreferredSize(new Dimension(1, 1));
                    JLabel lbOrderStatus = new JLabel(order.getStatus().getName());
                    infoPanel.add(lbOrderStatus);
                    lbOrderStatus.setPreferredSize(new Dimension(1, 1));
                    JLabel lbIsClosed = new JLabel(order.isClosed() + "");
                    infoPanel.add(lbIsClosed);
                    lbIsClosed.setPreferredSize(new Dimension(1, 1));
                    JLabel lbInstrumentTitle = new JLabel(order.getInstruments().get(j).getInstrument().getTitle());
                    infoPanel.add(lbInstrumentTitle);
                    lbInstrumentTitle.setPreferredSize(new Dimension(1, 1));
                    JLabel lbInstrumentPrice = new JLabel(order.getInstruments().get(j).getPrice() + "");
                    infoPanel.add(lbInstrumentPrice);
                    lbInstrumentPrice.setPreferredSize(new Dimension(1, 1));
                    JLabel lbInstrumentQuantity = new JLabel(order.getInstruments().get(j).getQuantity() + "");
                    infoPanel.add(lbInstrumentQuantity);
                    lbInstrumentQuantity.setPreferredSize(new Dimension(1, 1));
                }
            }
            JScrollPane scrollPanel = new JScrollPane(infoPanel);
            mainPanel.add(scrollPanel, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> databaseClient.loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            databaseClient.add(mainPanel);
            databaseClient.setSize(700, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}
