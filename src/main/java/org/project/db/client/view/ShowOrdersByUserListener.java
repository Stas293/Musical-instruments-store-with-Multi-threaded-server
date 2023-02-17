package org.project.db.client.view;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ShowOrdersByUserListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    private static final Logger logger = Logger.getLogger(ShowOrdersByUserListener.class.getName());

    public ShowOrdersByUserListener(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
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
        JButton btBack = new JButton(MainConstants.BACK);
        btSearch.addActionListener(new ActionSearch(mainPanel, controlPanel, tfTitle, btBack).invoke());
        btBack.addActionListener(event -> databaseClient.loggedInUser());
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(btBack, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }

    private class ActionSearch {
        private final JPanel mainPanel;
        private final JPanel controlPanel;
        private final JTextField tfTitle;
        private final JButton btBack;

        public ActionSearch(JPanel mainPanel, JPanel controlPanel, JTextField tfTitle, JButton btBack) {
            this.mainPanel = mainPanel;
            this.controlPanel = controlPanel;
            this.tfTitle = tfTitle;
            this.btBack = btBack;
        }

        public ActionListener invoke() {
            return (event) -> {
                try {
                    String userLogin = tfTitle.getText().trim();
                    ArrayList<Order> orders = getOrders(userLogin);
                    mainPanel.removeAll();
                    mainPanel.add(controlPanel, BorderLayout.NORTH);
                    mainPanel.add(btBack, BorderLayout.SOUTH);
                    databaseClient.setSize(400, 200);
                    databaseClient.add(mainPanel);
                    if (!orders.isEmpty()) {
                        JPanel infoPanel = new JPanel();
                        int size = orders.stream().mapToInt(value -> (int) IntStream.range(0, value.getInstruments().size()).count()).sum();
                        JLabel instrumentQuantity = ShowOrdersListener.addJlabels(infoPanel, size);
                        ShowAllOrdersListener.addArrayToPanel(orders, mainPanel, infoPanel, instrumentQuantity);
                        databaseClient.add(mainPanel);
                        databaseClient.setSize(800, 300);
                    } else {
                        mainPanel.removeAll();
                        mainPanel.add(controlPanel, BorderLayout.NORTH);
                        mainPanel.add(btBack, BorderLayout.SOUTH);
                        databaseClient.setSize(400, 200);
                        databaseClient.add(mainPanel);
                    }
                    databaseClient.repaint();
                } catch (IOException | ClassNotFoundException e1) {
                    logger.log(Level.SEVERE, e1.getMessage(), e1);
                }
            };
        }

        private ArrayList<Order> getOrders(String userLogin) throws IOException, ClassNotFoundException {
            ArrayList<Order> orders = new ArrayList<>();
            if (!userLogin.isEmpty()) {
                toServer.writeObject("getOrderByUser");
                toServer.writeObject(new UserDto(userLogin));
                orders = (ArrayList<Order>) fromServer.readObject();
            }
            return orders;
        }
    }
}
