package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserRoleDto;
import org.project.db.server_controller.Command;
import org.project.db.service.RoleService;
import org.project.db.service.UserService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class RoleForUser implements Command {
    private static final Logger logger = Logger.getLogger(RoleForUser.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final RoleService roleService = new RoleService();
    private final UserService userService = new UserService();

    public RoleForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("roleForUser");
        Object object3 = inputObjectFromClient.readObject();
        UserRoleDto userRoleDto = (UserRoleDto) object3;
        roleService.insertRoleForUser(userRoleDto);
        outputObjectToClient.writeObject(userService.findByLogin(userRoleDto.login()));
    }
}
