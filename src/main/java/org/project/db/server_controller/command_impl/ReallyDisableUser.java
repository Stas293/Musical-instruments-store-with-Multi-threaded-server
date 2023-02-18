package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class ReallyDisableUser implements Command {
    private static final Logger logger = Logger.getLogger(ReallyDisableUser.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final UserService userService = new UserService();

    public ReallyDisableUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("reallyDisableUser");
        Object object = inputObjectFromClient.readObject();
        if (object instanceof UserDto object1) {
            userService.disableUser(object1);
            outputObjectToClient.writeObject("Successfully disabled user");
        } else {
            outputObjectToClient.writeObject("Wrong object");
        }
    }
}
