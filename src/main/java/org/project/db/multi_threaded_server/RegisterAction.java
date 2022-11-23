package org.project.db.multi_threaded_server;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dao.impl.UserDaoImpl;
import org.project.db.dto.RegistrationDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

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
        try (UserDao user = JDBCDaoFactory.getInstance().createUserDao()) {
            if (user.create(s).isPresent()) {
                outputObjectToClient.writeObject("Registration successful");
            } else {
                outputObjectToClient.writeObject("Registration failed");
            }
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Registration failed");
        }
    }
}
