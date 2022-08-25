package org.project.db.Repository;

import org.jetbrains.annotations.NotNull;
import org.project.db.Dto.UserDto;
import org.project.db.Model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoleRepository {
    public Role getRoleById(@NotNull Connection connection, Long id) throws SQLException {
        String queryString = "SELECT * FROM role WHERE role_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new Role(Long.parseLong(resultSet.getString("role_id")),
                resultSet.getString("code"), resultSet.getString("name"));
    }

    public Role getRoleByName(@NotNull Connection connection, String name) throws SQLException {
        String queryString = "SELECT * FROM role WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new Role(Long.parseLong(resultSet.getString("role_id")),
                resultSet.getString("code"), resultSet.getString("name"));
    }

    public ArrayList<Role> insertRoleForUser(@NotNull Connection connection, UserDto userDto, String roleName)
            throws SQLException {
        String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long userId = resultSet.getLong("user_id");
        Role role = getRoleByName(connection, roleName);
        String queryString1 = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        preparedStatement = connection.prepareStatement(queryString1);
        preparedStatement.setString(1, String.valueOf(userId));
        preparedStatement.setString(2, String.valueOf(role.getId()));
        preparedStatement.executeUpdate();
        connection.commit();
        return getRolesForUser(connection, userDto);
    }

    public ArrayList<Role> getRolesForUser(@NotNull Connection connection, UserDto userDto) throws SQLException {
        String queryString = "select r.* from user_list u join user_role ur on (u.user_id=ur.user_id) left join role r on (ur.role_id=r.role_id) where u.login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Role> roles = new ArrayList<>();
        while (resultSet.next()) {
            roles.add(new Role(Long.parseLong(resultSet.getString("role_id")),
                    resultSet.getString("code"), resultSet.getString("name")));
        }
        return roles;
    }

    public ArrayList<Role> getAllRoles(@NotNull Connection connection) throws SQLException {
        String queryString = "SELECT * FROM role";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Role> roles = new ArrayList<>();
        while (resultSet.next()) {
            roles.add(new Role(Long.parseLong(resultSet.getString("role_id")),
                    resultSet.getString("code"), resultSet.getString("name")));
        }
        return roles;
    }
}
