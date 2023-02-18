package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;

public class ChangePhone implements Command {
    private static final Logger logger = Logger.getLogger(ChangePhone.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final UserService userService = new UserService();

    public ChangePhone(ObjectInputStream inputObjectFromClient) {
        this.inputObjectFromClient = inputObjectFromClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changePhone");
        UserDto userDto3 = (UserDto) inputObjectFromClient.readObject();
        String phone = (String) inputObjectFromClient.readObject();
        userService.changePhone(userDto3, phone);
    }
}
