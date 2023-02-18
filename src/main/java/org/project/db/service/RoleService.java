package org.project.db.service;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.RoleDao;
import org.project.db.dao.impl.DataSource;
import org.project.db.dto.UserDto;
import org.project.db.dto.UserRoleDto;
import org.project.db.model.Role;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

public class RoleService {
    private static final DaoFactory daoFactory = DaoFactory.getInstance();
    private static final Logger logger = Logger.getLogger(RoleService.class.getName());

    public RoleService() {
    }

    public List<Role> getAllRoles() {
        try (Connection connection = DataSource.getConnection()) {
            RoleDao roleDao = daoFactory.createRoleDao(connection);
            return roleDao.findAll();
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return List.of();
    }

    public List<Role> getRolesForUser(UserDto userDto) {
        try (Connection connection = DataSource.getConnection()) {
            RoleDao roleDao = daoFactory.createRoleDao(connection);
            return roleDao.getRolesForUser(userDto);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return List.of();
    }

    public void insertRoleForUser(UserRoleDto userRoleDto) {
        try (Connection connection = DataSource.getConnection()) {
            RoleDao roleDao = daoFactory.createRoleDao(connection);
            roleDao.insertRoleForUser(
                    new UserDto(userRoleDto.login()),
                    userRoleDto.roleName());
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }
}
