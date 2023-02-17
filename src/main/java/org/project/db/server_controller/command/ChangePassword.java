package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChangePassword implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public ChangePassword(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto4 = (UserDto) inputObjectFromClient.readObject();
        String password = (String) inputObjectFromClient.readObject();
        try (UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            userDao.changePassword(userDto4, password);
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
