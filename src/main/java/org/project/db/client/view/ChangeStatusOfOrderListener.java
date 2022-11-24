package org.project.db.client.view;

import org.project.db.client.constants.InstrumentConstants;
import org.project.db.client.controller.GetChangeStatusOrder;
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
import java.util.logging.Logger;
import java.util.stream.IntStream;

class ChangeStatusOfOrderListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    private static final Logger logger = Logger.getLogger(ChangeStatusOfOrderListener.class.getName());

    public ChangeStatusOfOrderListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
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
            List<Order> orders = getOrders();
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

    private void addButtonForEachOfStatus(List<Order> orders, JPanel infoPanel) {
        long i;
        for (Order order : orders) {
            i = order.getId();
            for (int j = 0; j < order.getInstruments().size(); j++) {
                addInstrumentsToPanel(infoPanel, order, j);
                JButton btChangeStatus = new JButton("Change status " + i);
                btChangeStatus.addActionListener(new GetChangeStatusOrder(btChangeStatus, fromServer, toServer));
                infoPanel.add(btChangeStatus);
            }
        }
    }

    private List<Order> getOrders() throws IOException, ClassNotFoundException {
        toServer.writeObject("allUserDtos");
        List<UserDto> users = (List<UserDto>) fromServer.readObject();
        ArrayList<Order> orders = new ArrayList<>();
        for (UserDto user : users) {
            toServer.writeObject("getOrderByUser");
            toServer.writeObject(user);
            orders.addAll((ArrayList<Order>) fromServer.readObject());
        }
        return orders;
    }

}
