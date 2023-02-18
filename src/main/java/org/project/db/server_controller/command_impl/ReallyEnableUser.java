package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class ReallyEnableUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final Logger logger = Logger.getLogger(ReallyEnableUser.class.getName());
    private final UserService userService = new UserService();
    public ReallyEnableUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("reallyEnableUser");
        Object object = inputObjectFromClient.readObject();
        if (object instanceof UserDto object1) {
            userService.enableUser(object1);
            outputObjectToClient.writeObject("Successfully enabled user");
        } else {
            outputObjectToClient.writeObject("Wrong object");
        }
    }
}
