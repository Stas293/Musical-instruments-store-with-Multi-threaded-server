package org.project.db.client.view_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.db.client.DatabaseClient;
import org.project.db.model.Instrument;
import org.project.db.model.Status;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;


public class MakeInstrumentAndStatus implements ActionListener {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final JTextField tfTitle;
    private final JTextField tfDescription;
    private final JComboBox<String> cbStatus;
    private final JTextField tfPrice;
    private final Logger logger = LogManager.getLogger(MakeInstrumentAndStatus.class.getName());

    public MakeInstrumentAndStatus(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer, JTextField tfTitle, JTextField tfDescription, JComboBox<String> cbStatus, JTextField tfPrice, JButton btAdd) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.tfTitle = tfTitle;
        this.tfDescription = tfDescription;
        this.cbStatus = cbStatus;
        this.tfPrice = tfPrice;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (tfDescription.getText().trim().isEmpty()
                    || tfTitle.getText().trim().isEmpty()
                    || Double.parseDouble(tfPrice.getText().trim()) < 0) {
                return;
            }
            toServer.writeObject("getStatuses");
            java.util.List<Status> statuses = (java.util.List<Status>) fromServer.readObject();
            String statusName = Objects.requireNonNull(cbStatus.getSelectedItem()).toString();
            Status status = statuses.stream().filter(s -> s.getName().equals(statusName)).findFirst().orElse(null);
            toServer.writeObject("addInstrument");
            toServer.writeObject(Instrument.builder()
                    .setDescription(tfDescription.getText().trim())
                    .setTitle(tfTitle.getText().trim())
                    .setStatus(status)
                    .setPrice(Double.parseDouble(tfPrice.getText().trim()))
                    .createInstrument());
            Object object = fromServer.readObject();
            System.out.println(object);
            databaseClient.loggedInUser();
        } catch (IOException | ClassNotFoundException e1) {
            logger.error(e1);
        } catch (NumberFormatException ex) {
            System.out.println("Price must be a number");
        }
    }
}
