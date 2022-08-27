package org.project.db.Client;

import org.project.db.Dto.UserDto;
import org.project.db.Model.OrderHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class ShowHistoryOrdersListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public ShowHistoryOrdersListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.toServer.writeObject("getAllHistoryOrdersForUser");
            databaseClient.toServer.writeObject(new UserDto(databaseClient.user.getLogin()));
            ArrayList<OrderHistory> ordersHistory = (ArrayList<OrderHistory>) databaseClient.fromServer.readObject();
            databaseClient.getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(ordersHistory.size() + 1, 6));
            JLabel orderHistoryId = new JLabel("Order history id");
            infoPanel.add(orderHistoryId);
            JLabel orderHistoryTitle = new JLabel("Order history title");
            infoPanel.add(orderHistoryTitle);
            JLabel userLogin = new JLabel("User login");
            infoPanel.add(userLogin);
            JLabel dateCreated = new JLabel("Date created");
            infoPanel.add(dateCreated);
            JLabel totalSum = new JLabel("Total sum");
            infoPanel.add(totalSum);
            JLabel status = new JLabel("Status");
            infoPanel.add(status);
            for (OrderHistory orderHistory : ordersHistory) {
                JLabel lbOrderHistoryId = new JLabel(orderHistory.getId() + "");
                infoPanel.add(lbOrderHistoryId);
                lbOrderHistoryId.setPreferredSize(new Dimension(1, 1));
                JLabel lbOrderHistoryTitle = new JLabel(orderHistory.getTitle());
                infoPanel.add(lbOrderHistoryTitle);
                lbOrderHistoryTitle.setPreferredSize(new Dimension(1, 1));
                JLabel lbUserLogin = new JLabel(orderHistory.getUser().getLogin());
                infoPanel.add(lbUserLogin);
                lbUserLogin.setPreferredSize(new Dimension(1, 1));
                JLabel lbDateCreated = new JLabel(orderHistory.getDateCreated() + "");
                infoPanel.add(lbDateCreated);
                lbDateCreated.setPreferredSize(new Dimension(1, 1));
                JLabel lbTotalSum = new JLabel(orderHistory.getTotalSum() + "");
                infoPanel.add(lbTotalSum);
                lbTotalSum.setPreferredSize(new Dimension(1, 1));
                JLabel lbStatus = new JLabel(orderHistory.getStatus().getName());
                infoPanel.add(lbStatus);
                lbStatus.setPreferredSize(new Dimension(1, 1));
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
