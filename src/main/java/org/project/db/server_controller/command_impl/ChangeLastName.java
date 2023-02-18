package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class ChangeLastName implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private static final Logger logger = Logger.getLogger(ChangeLastName.class.getName());
    private final UserService userService = new UserService();

    public ChangeLastName(ObjectInputStream inputObjectFromClient) {
        this.inputObjectFromClient = inputObjectFromClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changeLastName");
        UserDto userDto = (UserDto) inputObjectFromClient.readObject();
        String lastName = (String) inputObjectFromClient.readObject();
        userService.changeLastName(userDto, lastName);
    }
}
