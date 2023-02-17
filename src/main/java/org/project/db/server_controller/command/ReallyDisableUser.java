package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class ReallyDisableUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public ReallyDisableUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Object object = inputObjectFromClient.readObject();
        if (object instanceof UserDto) {
            try (UserDao user = JDBCDaoFactory.getInstance().createUserDao()) {
                user.disableUser((UserDto) object);
            } catch (SQLException e) {
                outputObjectToClient.writeObject("Error");
            }
            outputObjectToClient.writeObject("Successfully disabled user");
        } else {
            outputObjectToClient.writeObject("Wrong object");
        }
    }
}
