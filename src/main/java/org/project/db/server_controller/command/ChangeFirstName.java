package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChangeFirstName implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public ChangeFirstName(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto1 = (UserDto) inputObjectFromClient.readObject();
        String firstName = (String) inputObjectFromClient.readObject();
        try (UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            userDao.changeFirstName(userDto1, firstName);
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
