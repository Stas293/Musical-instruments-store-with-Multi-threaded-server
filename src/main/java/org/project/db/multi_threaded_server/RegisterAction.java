package org.project.db.multi_threaded_server;

import org.project.db.dao.UserRepository;
import org.project.db.dto.RegistrationDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegisterAction implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public RegisterAction(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        RegistrationDto s = (RegistrationDto) inputObjectFromClient.readObject();
        User user = new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(s.getLogin()));
        if (user == null) {
            new UserRepository().insertUser(multiThreadServer.connection, s);
            multiThreadServer.connection.commit();
            outputObjectToClient.writeObject(new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(s.getLogin())));
        } else {
            outputObjectToClient.writeObject("User already exists");
        }
    }
}
