package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class ReallyEnableUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public ReallyEnableUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Object object1 = inputObjectFromClient.readObject();
        if (object1 instanceof UserDto) {
            try (UserDao userDao = DaoFactory.getInstance().createUserDao()) {
                userDao.enableUser((UserDto) object1);
            } catch (SQLException e) {
                outputObjectToClient.writeObject("Error");
            }
            outputObjectToClient.writeObject("Successfully enabled user");
        } else {
            outputObjectToClient.writeObject("Wrong object");
        }
    }
}
