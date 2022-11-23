package org.project.db.client;

import org.project.db.dto.UserDto;
import org.project.db.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.IntStream;

class ChangeStatusOfOrderListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ChangeStatusOfOrderListener.class.getName());

    public ChangeStatusOfOrderListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    static void addInstrumentsToPanel(JPanel infoPanel, Order order, int j) {
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
            infoPanel.setLayout(new GridLayout(size + 1, 7));
            JLabel orderTitle = new JLabel("Order title");
            infoPanel.add(orderTitle);
            JLabel orderStatus = new JLabel("Order status");
            infoPanel.add(orderStatus);
            JLabel isClosed = new JLabel("Is closed");
            infoPanel.add(isClosed);
            JLabel instrumentTitle = new JLabel(InstrumentConstants.INSTRUMENT_TITLE);
            infoPanel.add(instrumentTitle);
            JLabel instrumentPrice = new JLabel(InstrumentConstants.INSTRUMENT_PRICE);
            infoPanel.add(instrumentPrice);
            JLabel instrumentQuantity = new JLabel(InstrumentConstants.INSTRUMENT_QUANTITY);
            infoPanel.add(instrumentQuantity);
            JLabel changeStatus = new JLabel("Change status");
            infoPanel.add(changeStatus);
            addButtonForEachOfStatus(orders, infoPanel);
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(1100, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void addButtonForEachOfStatus(ArrayList<Order> orders, JPanel infoPanel) {
        long i;
        for (Order order : orders) {
            i = order.getId();
            for (int j = 0; j < order.getInstruments().size(); j++) {
                addInstrumentsToPanel(infoPanel, order, j);
                JButton btChangeStatus = new JButton("Change status " + i);
                btChangeStatus.addActionListener(event -> {
                    try {
                        databaseClient.toServer.writeObject("changeStatusOfOrder");
                        databaseClient.toServer.writeObject(btChangeStatus.getText().split(" ")[2]);
                    } catch (IOException ex) {
                        logger.warning(ex.getMessage());
                    }
                });
                infoPanel.add(btChangeStatus);
            }
        }
    }
}
