package org.project.db.client.view_service;

import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnableUserSendServer implements ActionListener {
    private static final Logger logger = Logger.getLogger(EnableUserSendServer.class.getName());
    private final JButton btEnable;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;

    public EnableUserSendServer(JButton btEnable, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.btEnable = btEnable;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(btEnable.getText().split(" ")[1]);
            toServer.writeObject("reallyEnableUser");
            toServer.writeObject(new UserDto(btEnable.getText().split(" ")[1]));
            Object object1 = fromServer.readObject();
            System.out.println((String) object1);
        } catch (IOException | ClassNotFoundException e1) {
            logger.log(Level.WARNING, "Error while enabling user", e1);
        }
    }
}
