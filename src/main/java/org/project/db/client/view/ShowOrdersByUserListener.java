package org.project.db.client.view;

import org.project.db.client.constants.MainConstants;
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

class ShowOrdersByUserListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(ShowOrdersByUserListener.class.getName());

    public ShowOrdersByUserListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
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
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
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
                    ArrayList<Order> orders = new ArrayList<>();
                    if (!userLogin.isEmpty()) {
                        databaseClient.toServer.writeObject("getOrderByUser");
                        databaseClient.toServer.writeObject(new UserDto(userLogin));
                        orders = (ArrayList<Order>) databaseClient.fromServer.readObject();
                    }
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
    }
}
