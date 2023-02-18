package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class ChangeEmail implements Command {
    private static final Logger logger = Logger.getLogger(ChangeEmail.class.getName());
    private static final UserService userService = new UserService();
    private final ObjectInputStream inputObjectFromClient;


    public ChangeEmail(ObjectInputStream inputObjectFromClient) {
        this.inputObjectFromClient = inputObjectFromClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changeEmail");
        UserDto userDto = (UserDto) inputObjectFromClient.readObject();
        String email = (String) inputObjectFromClient.readObject();
        userService.changeEmail(userDto, email);
    }
}
