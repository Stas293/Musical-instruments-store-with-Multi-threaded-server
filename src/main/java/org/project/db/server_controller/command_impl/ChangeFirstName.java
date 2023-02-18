package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class ChangeFirstName implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private static final Logger logger = Logger.getLogger(ChangeFirstName.class.getName());
    private final UserService userService = new UserService();

    public ChangeFirstName(ObjectInputStream inputObjectFromClient) {
        this.inputObjectFromClient = inputObjectFromClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changeFirstName");
        UserDto userDto = (UserDto) inputObjectFromClient.readObject();
        String firstName = (String) inputObjectFromClient.readObject();
        userService.changeFirstName(userDto, firstName);
    }
}
