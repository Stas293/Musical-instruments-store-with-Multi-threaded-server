package org.project.db.client.service;

import org.project.db.dto.UserDto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DisableUserSendServer implements ActionListener {
    private final JButton btDisable;
    private final ObjectInputStream fromServer;
    private final ObjectOutputStream toServer;
    public DisableUserSendServer(JButton btDisable, ObjectInputStream fromServer, ObjectOutputStream toServer) {
        this.btDisable = btDisable;
        this.fromServer = fromServer;
        this.toServer = toServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            System.out.println(btDisable.getText().split(" ")[1]);
            toServer.writeObject("reallyDisableUser");
            toServer.writeObject(new UserDto(btDisable.getText().split(" ")[1]));
            Object object1 = fromServer.readObject();
            System.out.println((String) object1);
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
