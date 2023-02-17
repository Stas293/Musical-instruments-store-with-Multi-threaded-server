package org.project.db.server_controller.command;

import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.LoginDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class LoginAction implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public LoginAction(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws ClassNotFoundException, IOException {
        LoginDto s = (LoginDto) inputObjectFromClient.readObject();
        try (UserDao user = JDBCDaoFactory.getInstance().createUserDao()) {
            Optional<User> userFromDB = user.findByLogin(s.login());
            if (userFromDB.isPresent()) {
                if (userFromDB.get().getPassword().equals(s.password())) {
                    outputObjectToClient.writeObject(new UserDto(userFromDB.get().getLogin()));
                } else {
                    outputObjectToClient.writeObject("Wrong password");
                }
            } else {
                outputObjectToClient.writeObject("Wrong login");
            }
        }
    }
}
