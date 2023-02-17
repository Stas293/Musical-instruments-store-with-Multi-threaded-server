package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class ChangeEmail implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public ChangeEmail(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto = (UserDto) inputObjectFromClient.readObject();
        String email = (String) inputObjectFromClient.readObject();
        try (UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            userDao.changeEmail(userDto, email);
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
