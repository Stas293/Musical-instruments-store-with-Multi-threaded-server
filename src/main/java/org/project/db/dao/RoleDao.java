package org.project.db.dao;

import org.project.db.dto.UserDto;
import org.project.db.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleDao extends GenericDao<Role> {
    Optional<Role> findByCode(String code);

    Optional<List<Role>> getRolesForUser(UserDto userDto);

    List<Role> insertRoleForUser(UserDto userDto, String roleName);

    Optional<List<Role>> findAll();
}
