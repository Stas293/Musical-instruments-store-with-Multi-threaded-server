package org.project.db.server_controller.command;

import org.project.db.dao.RoleDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;
import org.project.db.model.Role;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class AddRoleForUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public AddRoleForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        System.out.println("addRoleForUser");
        Object object2 = inputObjectFromClient.readObject();
        try (RoleDao roleDao = JDBCDaoFactory.getInstance().createRoleDao()) {
            List<Role> allRoles = roleDao.findAll();
            List<Role> haveRoles = roleDao.getRolesForUser((UserDto) object2);
            System.out.println(allRoles);
            System.out.println(haveRoles);
            allRoles.removeAll(haveRoles);
            outputObjectToClient.writeObject(allRoles);
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
