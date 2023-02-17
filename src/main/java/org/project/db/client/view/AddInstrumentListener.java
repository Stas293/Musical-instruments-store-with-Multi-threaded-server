package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;
import org.project.db.client.view_service.MakeInstrumentAndStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddInstrumentListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;


    public AddInstrumentListener(DatabaseClient databaseClient, ObjectInputStream fromServer, ObjectOutputStream toServer) {
        this.databaseClient = databaseClient;
        this.fromServer = fromServer;
        this.toServer = toServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        databaseClient.getContentPane().removeAll();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(2, 4));
        JLabel lbTitle = new JLabel("Title");
        infoPanel.add(lbTitle);
        JLabel lbDescription = new JLabel("Description");
        infoPanel.add(lbDescription);
        JLabel lbStatus = new JLabel("Status");
        infoPanel.add(lbStatus);
        JLabel lbPrice = new JLabel("Price");
        infoPanel.add(lbPrice);
        JTextField tfTitle = new JTextField();
        infoPanel.add(tfTitle);
        JTextField tfDescription = new JTextField();
        infoPanel.add(tfDescription);
        JComboBox<String> cbStatus = new JComboBox<>();
        cbStatus.addItem("Not Available");
        cbStatus.addItem("Not Processed");
        cbStatus.addItem("Available");
        cbStatus.addItem("Order Processing");
        cbStatus.addItem("Arrived");
        cbStatus.setSelectedIndex(2);
        infoPanel.add(cbStatus);
        JTextField tfPrice = new JTextField();
        infoPanel.add(tfPrice);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JButton btAdd = new JButton("Add");
        btAdd.addActionListener(new MakeInstrumentAndStatus(databaseClient, toServer, fromServer, tfTitle, tfDescription, cbStatus, tfPrice, btAdd));
        JButton btBack = new JButton(MainConstants.BACK);
        btBack.addActionListener(event -> databaseClient.loggedInUser());
        JPanel btPanel = new JPanel();
        btPanel.add(btBack);
        btPanel.add(btAdd);
        mainPanel.add(btPanel, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }

}
