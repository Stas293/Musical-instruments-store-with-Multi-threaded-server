package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.model.Role;
import org.project.db.server_controller.Command;
import org.project.db.service.RoleService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;

public class AddRoleForUser implements Command {
    private static final Logger logger = Logger.getLogger(AddRoleForUser.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final RoleService roleService = new RoleService();

    public AddRoleForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("addRoleForUser");
        UserDto userDto = (UserDto) inputObjectFromClient.readObject();
        List<Role> allRoles = roleService.getAllRoles();
        List<Role> haveRoles = roleService.getRolesForUser(userDto);
        logger.info("allRoles: " + allRoles);
        logger.info("haveRoles: " + haveRoles);
        allRoles.removeAll(haveRoles);
        outputObjectToClient.writeObject(allRoles);
    }
}
