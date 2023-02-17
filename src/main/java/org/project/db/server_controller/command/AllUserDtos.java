package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class AllUserDtos implements Command {
    private final ObjectOutputStream outputObjectToClient;
    public AllUserDtos(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        try (UserDao user = DaoFactory.getInstance().createUserDao()) {
            outputObjectToClient.writeObject(user.getAllUsers());
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Registration failed");
        }
    }
}
