package org.project.db.client.service;

import org.apache.logging.log4j.Logger;
import org.project.db.client.DatabaseClient;
import org.project.db.client.AddRoleButtonListner;
import org.project.db.dto.UserDto;
import org.project.db.dto.UserRoleDto;
import org.project.db.model.Role;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SendUserAndUpdateRole {
    private final DatabaseClient databaseClient;
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private final String login;
    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(SendUserAndUpdateRole.class);


    public SendUserAndUpdateRole(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer, String login) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
        this.login = login;
    }

    public List<Role> getRoles() throws IOException, ClassNotFoundException {
        toServer.writeObject("addRoleForUser");
        toServer.writeObject(new UserDto(login));
        Object object = fromServer.readObject();
        ArrayList<Role> roles = (ArrayList<Role>) object;
        return roles;
    }

    public ActionListener invoke(JButton btAddRole) {
        return event -> {
            try {
                toServer.writeObject("RoleForUser");
                toServer.writeObject(new UserRoleDto(login, btAddRole.getText().split(" ")[2]));
                Object object1 = fromServer.readObject();
                System.out.println(object1);
                databaseClient.loggedInUser();
            } catch (IOException | ClassNotFoundException e1) {
                logger.error(e1);
            }
        };
    }
}
