package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class ChangePassword implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private static final Logger logger = Logger.getLogger(ChangePassword.class.getName());
    private final UserService userService = new UserService();

    public ChangePassword(ObjectInputStream inputObjectFromClient) {
        this.inputObjectFromClient = inputObjectFromClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changePassword");
        UserDto userDto4 = (UserDto) inputObjectFromClient.readObject();
        String password = (String) inputObjectFromClient.readObject();
        userService.changePassword(userDto4, password);
    }
}
