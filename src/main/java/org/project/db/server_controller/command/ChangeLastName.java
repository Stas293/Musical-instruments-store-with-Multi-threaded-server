package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChangeLastName implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public ChangeLastName(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto2 = (UserDto) inputObjectFromClient.readObject();
        String lastName = (String) inputObjectFromClient.readObject();
        try (UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            userDao.changeLastName(userDto2, lastName);
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
