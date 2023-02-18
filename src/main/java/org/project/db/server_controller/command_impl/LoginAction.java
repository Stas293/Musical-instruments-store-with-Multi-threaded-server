package org.project.db.server_controller.command_impl;

import org.project.db.dto.LoginDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;
import org.project.db.server_controller.Command;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.logging.Logger;

public class LoginAction implements Command {
    private static final Logger logger = Logger.getLogger(LoginAction.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final UserService userService = new UserService();

    public LoginAction(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws ClassNotFoundException, IOException {
        logger.info("login");
        LoginDto s = (LoginDto) inputObjectFromClient.readObject();
        Optional<User> userFromDB = userService.findByLogin(s.login());
        if (userFromDB.isPresent()) {
            if (userFromDB.get().getPassword().equals(s.password())) {
                outputObjectToClient.writeObject(new UserDto(userFromDB.get().getLogin()));
            } else {
                outputObjectToClient.writeObject("Wrong password");
            }
        } else {
            outputObjectToClient.writeObject("Wrong login");
        }
    }
}
