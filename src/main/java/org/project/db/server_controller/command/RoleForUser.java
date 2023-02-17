package org.project.db.server_controller.command;

import org.project.db.dao.RoleDao;
import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;
import org.project.db.dto.UserRoleDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RoleForUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public RoleForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Object object3 = inputObjectFromClient.readObject();
        UserRoleDto userRoleDto = (UserRoleDto) object3;
        try (RoleDao roleDao = JDBCDaoFactory.getInstance().createRoleDao();
             UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            roleDao.insertRoleForUser(new UserDto(userRoleDto.login()), userRoleDto.roleName());
            outputObjectToClient.writeObject(userDao.findByLogin(userRoleDto.login()));
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
