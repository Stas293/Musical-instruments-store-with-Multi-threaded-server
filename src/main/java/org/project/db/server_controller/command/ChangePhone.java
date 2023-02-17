package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChangePhone implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public ChangePhone(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto3 = (UserDto) inputObjectFromClient.readObject();
        String phone = (String) inputObjectFromClient.readObject();
        try (UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            userDao.changePhone(userDto3, phone);
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
