package org.project.db.Client;

import org.project.db.Model.Instrument;
import org.project.db.Model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

class AddInstrumentListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public AddInstrumentListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
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
        btAdd.addActionListener((event) -> {
            try {
                if (tfDescription.getText().trim().isEmpty() || tfTitle.getText().trim().isEmpty() || Double.parseDouble(tfPrice.getText().trim()) < 0) {
                    return;
                }
                databaseClient.toServer.writeObject("getStatuses");
                ArrayList<Status> statuses = (ArrayList<Status>) databaseClient.fromServer.readObject();
                String statusName = cbStatus.getSelectedItem().toString();
                Status status = null;
                for (Status s : statuses) {
                    if (s.getName().equals(statusName)) {
                        status = s;
                        break;
                    }
                }
                databaseClient.toServer.writeObject("addInstrument");
                databaseClient.toServer.writeObject(new Instrument(tfDescription.getText().trim(), tfTitle.getText().trim(), status, Double.parseDouble(tfPrice.getText().trim())));
                Object object = databaseClient.fromServer.readObject();
                System.out.println(object);
                databaseClient.loggedInUser();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (NumberFormatException ex) {
                System.out.println("Price must be a number");
            }
        });
        JButton btBack = new JButton("Back");
        btBack.addActionListener((event) -> databaseClient.loggedInUser());
        JPanel btPanel = new JPanel();
        btPanel.add(btBack);
        btPanel.add(btAdd);
        mainPanel.add(btPanel, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }
}
