package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.model.OrderHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShowHistoryOrdersListener implements ActionListener {
    private static final Logger logger = Logger.getLogger(ShowHistoryOrdersListener.class.getName());
    private final DatabaseClient databaseClient;
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;

    public ShowHistoryOrdersListener(DatabaseClient databaseClient, ObjectInputStream fromServer, ObjectOutputStream toServer) {
        this.databaseClient = databaseClient;
        this.fromServer = fromServer;
        this.toServer = toServer;
    }

    private static void addOrderHistoryToPanel(JPanel infoPanel, OrderHistory orderHistory) {
        idTitle(infoPanel, orderHistory.getId(), orderHistory.getTitle());
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

    static void idTitle(JPanel infoPanel, Long id, String title) {
        JLabel lbOrderHistoryId = new JLabel(id + "");
        infoPanel.add(lbOrderHistoryId);
        lbOrderHistoryId.setPreferredSize(new Dimension(1, 1));
        JLabel lbOrderHistoryTitle = new JLabel(title);
        infoPanel.add(lbOrderHistoryTitle);
        lbOrderHistoryTitle.setPreferredSize(new Dimension(1, 1));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            java.util.List<OrderHistory> ordersHistory = getOrderHistories();
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
                addOrderHistoryToPanel(infoPanel, orderHistory);
            }
            EnableUserListener.changeMainPanelScroller(mainPanel, infoPanel, databaseClient);
            databaseClient.setSize(700, 300);
            databaseClient.repaint();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Error while getting all history orders for user", ex);
        }
    }

    private java.util.List<OrderHistory> getOrderHistories() throws IOException, ClassNotFoundException {
        toServer.writeObject("getAllHistoryOrdersForUser");
        toServer.writeObject(databaseClient.getUserDto());
        return (java.util.List<OrderHistory>) fromServer.readObject();
    }
}
