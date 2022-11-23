package org.project.db.client.service;

import org.project.db.client.DatabaseClient;
import org.project.db.model.Status;
import org.project.db.model.builder.InstrumentBuilderImpl;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

public class MakeInstrumentAndStatus {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final JTextField tfTitle;
    private final JTextField tfDescription;
    private final JComboBox<String> cbStatus;
    private final JTextField tfPrice;
    private final JButton btAdd;
    private final Logger logger = Logger.getLogger(MakeInstrumentAndStatus.class.getName());

    public MakeInstrumentAndStatus(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer, JTextField tfTitle, JTextField tfDescription, JComboBox<String> cbStatus, JTextField tfPrice, JButton btAdd) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
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
                toServer.writeObject("getStatuses");
                ArrayList<Status> statuses = (ArrayList<Status>) fromServer.readObject();
                String statusName = Objects.requireNonNull(cbStatus.getSelectedItem()).toString();
                Status status = statuses.stream().filter(s -> s.getName().equals(statusName)).findFirst().orElse(null);
                toServer.writeObject("addInstrument");
                toServer.writeObject(new InstrumentBuilderImpl().setDescription(tfDescription.getText().trim()).setTitle(tfTitle.getText().trim()).setStatus(status).setPrice(Double.parseDouble(tfPrice.getText().trim())).createInstrument());
                Object object = fromServer.readObject();
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
