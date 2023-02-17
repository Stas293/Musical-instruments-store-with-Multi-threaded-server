package org.project.db.client.view_service;

import org.project.db.model.Instrument;
import org.project.db.model.Status;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeInstrumentGetByTitle {
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private static final Logger logger = Logger.getLogger(ChangeInstrumentGetByTitle.class.getName());

    public ChangeInstrumentGetByTitle(ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    public void addActionToSendNewStatus(Instrument instrument, JComboBox<Status> cbNewStatus, JButton btChangeStatus) {
        btChangeStatus.addActionListener(event1 -> {
            try {
                toServer.writeObject("changeStatusOfInstrument");
                toServer.writeObject(instrument);
                toServer.writeObject(cbNewStatus.getSelectedItem());
                System.out.println(fromServer.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                logger.log(Level.WARNING, "Error while changing status of instrument", ex);
            }
        });
    }

    public Instrument getInstrumentsByTitle(JTextField tfTitle) throws IOException, ClassNotFoundException {
        String title;
        toServer.writeObject("findByTitle");
        if (!tfTitle.getText().trim().isEmpty()) {
            title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
        } else {
            title = "";
        }
        toServer.writeObject(title);
        return (Instrument) fromServer.readObject();
    }
}
