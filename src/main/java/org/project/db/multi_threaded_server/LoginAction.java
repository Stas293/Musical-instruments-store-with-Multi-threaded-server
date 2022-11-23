package org.project.db.multi_threaded_server;

import org.project.db.dao.impl.UserDaoImpl;
import org.project.db.dto.LoginDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginAction implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public LoginAction(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws ClassNotFoundException, IOException {
        LoginDto s = (LoginDto) inputObjectFromClient.readObject();
        User user = new UserDaoImpl().findUserByLogin(multiThreadServer.connection, new UserDto(s.getLogin()));
        if (user != null) {
            if (!user.isEnabled()) {
                outputObjectToClient.writeObject("User is disabled");
            } else if (user.getPassword().equals(s.getPassword())) {
                outputObjectToClient.writeObject(user);
            } else {
                outputObjectToClient.writeObject("Wrong password");
            }
        } else {
            outputObjectToClient.writeObject("User not found");
        }
    }
}
