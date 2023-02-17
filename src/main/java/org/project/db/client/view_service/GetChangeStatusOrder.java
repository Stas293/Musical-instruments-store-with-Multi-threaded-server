package org.project.db.client.view_service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class GetChangeStatusOrder implements ActionListener {
    private final JButton btChangeStatus;
    private final ObjectOutputStream toServer;
    private static final Logger logger = Logger.getLogger(GetChangeStatusOrder.class.getName());

    public GetChangeStatusOrder(JButton btChangeStatus, ObjectInputStream fromServer, ObjectOutputStream toServer) {
        this.btChangeStatus = btChangeStatus;
        this.toServer = toServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            toServer.writeObject("changeStatusOfOrder");
            toServer.writeObject(btChangeStatus.getText().split(" ")[2]);
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
        }
    }
}
