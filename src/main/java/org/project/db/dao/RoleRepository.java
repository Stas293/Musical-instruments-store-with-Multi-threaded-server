package org.project.db.dao;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.dto.UserDto;
import org.project.db.model.Role;
import org.project.db.model.builder.RoleBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoleRepository {
    public synchronized Role getRoleById(@NotNull Connection connection, Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM role WHERE role_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new RoleBuilderImpl().setId(Long.parseLong(resultSet.getString("role_id"))).setCode(resultSet.getString("code")).setName(resultSet.getString("name")).createRole();
    }

    public synchronized Role getRoleByName(@NotNull Connection connection, String name) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM role WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new RoleBuilderImpl().setId(Long.parseLong(resultSet.getString("role_id"))).setCode(resultSet.getString("code")).setName(resultSet.getString("name")).createRole();
    }

    public synchronized ArrayList<Role> insertRoleForUser(@NotNull Connection connection, @NotNull UserDto userDto, String roleName)
            throws SQLException {
        @Language("MySQL") String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long userId = resultSet.getLong("user_id");
        Role role = getRoleByName(connection, roleName);
        @Language("MySQL") String queryString1 = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        preparedStatement = connection.prepareStatement(queryString1);
        preparedStatement.setString(1, String.valueOf(userId));
        preparedStatement.setString(2, String.valueOf(role.getId()));
        preparedStatement.executeUpdate();
        connection.commit();
        return getRolesForUser(connection, userDto);
    }

    public synchronized ArrayList<Role> getRolesForUser(@NotNull Connection connection, @NotNull UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "select r.* from user_list u join user_role ur on (u.user_id=ur.user_id) left join role r on (ur.role_id=r.role_id) where u.login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Role> roles = new ArrayList<>();
        while (resultSet.next()) {
            roles.add(new RoleBuilderImpl().setId(Long.parseLong(resultSet.getString("role_id"))).setCode(resultSet.getString("code")).setName(resultSet.getString("name")).createRole());
        }
        return roles;
    }

    public synchronized ArrayList<Role> getAllRoles(@NotNull Connection connection) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM role";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Role> roles = new ArrayList<>();
        while (resultSet.next()) {
            roles.add(new RoleBuilderImpl().setId(Long.parseLong(resultSet.getString("role_id"))).setCode(resultSet.getString("code")).setName(resultSet.getString("name")).createRole());
        }
        return roles;
    }
}
