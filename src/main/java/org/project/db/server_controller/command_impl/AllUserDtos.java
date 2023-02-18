package org.project.db.server_controller.command_impl;

import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class AllUserDtos implements Command {
    private static final Logger logger = Logger.getLogger(AllUserDtos.class.getName());
    private final ObjectOutputStream outputObjectToClient;
    private final UserService userService = new UserService();

    public AllUserDtos(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("allUserDtos");
        outputObjectToClient.writeObject(userService.getAllUsers());
    }
}
