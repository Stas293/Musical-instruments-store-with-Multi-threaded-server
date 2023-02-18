package org.project.db.server_controller.command_impl;

import org.project.db.dto.RegistrationDto;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

public class RegisterAction implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final Logger logger = Logger.getLogger(RegisterAction.class.getName());
    private final UserService userService = new UserService();

    public RegisterAction(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        RegistrationDto registrationDto = (RegistrationDto) inputObjectFromClient.readObject();
        try {
            if (userService.create(registrationDto).isPresent()) {
                outputObjectToClient.writeObject("Registration successful");
            } else {
                outputObjectToClient.writeObject("Registration failed");
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            outputObjectToClient.writeObject("Registration failed");
        }
    }
}
