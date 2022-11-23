package org.project.db.client;

import org.project.db.model.builder.InstrumentBuilderImpl;
import org.project.db.model.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

class AddInstrumentListener implements ActionListener {
    private final DatabaseClient databaseClient;
    private static final Logger logger = Logger.getLogger(AddInstrumentListener.class.getName());

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
        new MakeInstrumentAndStatus(tfTitle, tfDescription, cbStatus, tfPrice, btAdd).invoke();
        JButton btBack = new JButton(Constants.BACK);
        btBack.addActionListener(event -> databaseClient.loggedInUser());
        JPanel btPanel = new JPanel();
        btPanel.add(btBack);
        btPanel.add(btAdd);
        mainPanel.add(btPanel, BorderLayout.SOUTH);
        databaseClient.add(mainPanel);
        databaseClient.setSize(400, 200);
        databaseClient.repaint();
    }

    private class MakeInstrumentAndStatus {
        private final JTextField tfTitle;
        private final JTextField tfDescription;
        private final JComboBox<String> cbStatus;
        private final JTextField tfPrice;
        private final JButton btAdd;

        public MakeInstrumentAndStatus(JTextField tfTitle, JTextField tfDescription, JComboBox<String> cbStatus, JTextField tfPrice, JButton btAdd) {
            this.tfTitle = tfTitle;
            this.tfDescription = tfDescription;
            this.cbStatus = cbStatus;
            this.tfPrice = tfPrice;
            this.btAdd = btAdd;
        }

        public void invoke() {
            btAdd.addActionListener((event) -> {
                try {
                    if (tfDescription.getText().trim().isEmpty() || tfTitle.getText().trim().isEmpty() || Double.parseDouble(tfPrice.getText().trim()) < 0) {
                        return;
                    }
                    databaseClient.toServer.writeObject("getStatuses");
                    ArrayList<Status> statuses = (ArrayList<Status>) databaseClient.fromServer.readObject();
                    String statusName = Objects.requireNonNull(cbStatus.getSelectedItem()).toString();
                    Status status = statuses.stream().filter(s -> s.getName().equals(statusName)).findFirst().orElse(null);
                    databaseClient.toServer.writeObject("addInstrument");
                    databaseClient.toServer.writeObject(new InstrumentBuilderImpl().setDescription(tfDescription.getText().trim()).setTitle(tfTitle.getText().trim()).setStatus(status).setPrice(Double.parseDouble(tfPrice.getText().trim())).createInstrument());
                    Object object = databaseClient.fromServer.readObject();
                    System.out.println(object);
                    databaseClient.loggedInUser();
                } catch (IOException | ClassNotFoundException e1) {
                    logger.warning(e1.getMessage());
                } catch (NumberFormatException ex) {
                    System.out.println("Price must be a number");
                }
            });
        }
    }
}
