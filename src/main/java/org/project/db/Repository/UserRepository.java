package org.project.db.Repository;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.Dto.RegistrationDto;
import org.project.db.Dto.UserDto;
import org.project.db.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository {
    public synchronized User findUserById(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM user_list WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new User(Long.parseLong(resultSet.getString("user_id")),
                resultSet.getString("login"), resultSet.getString("first_name"),
                resultSet.getString("last_name"), resultSet.getString("email"),
                resultSet.getString("phone"), resultSet.getString("password"),
                Integer.parseInt(resultSet.getString("enabled")) == 1, resultSet.getTimestamp("date_created"),
                resultSet.getTimestamp("date_modified"), new RoleRepository().getRolesForUser(connection, new UserDto(resultSet.getString("login"))));
    }

    public synchronized User findUserByLogin(@NotNull Connection connection, @NotNull UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new User(Long.parseLong(resultSet.getString("user_id")),
                resultSet.getString("login"), resultSet.getString("first_name"),
                resultSet.getString("last_name"), resultSet.getString("email"),
                resultSet.getString("phone"), resultSet.getString("password"),
                Integer.parseInt(resultSet.getString("enabled")) == 1, resultSet.getTimestamp("date_created"),
                resultSet.getTimestamp("date_modified"), new RoleRepository().getRolesForUser(connection, userDto));
    }

    public synchronized User insertUser(@NotNull Connection connection, @NotNull RegistrationDto registrationDto)
            throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO user_list (login, first_name, last_name, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, registrationDto.getLogin());
        preparedStatement.setString(2, registrationDto.getFirstName());
        preparedStatement.setString(3, registrationDto.getLastName());
        preparedStatement.setString(4, registrationDto.getEmail());
        preparedStatement.setString(5, registrationDto.getPhone());
        preparedStatement.setString(6, registrationDto.getPassword());
        preparedStatement.executeUpdate();
        connection.commit();
        new RoleRepository().insertRoleForUser(connection, new UserDto(registrationDto.getLogin()), "user");
        return findUserByLogin(connection, new UserDto(registrationDto.getLogin()));
    }

    public synchronized User changeEmail(@NotNull Connection connection, @NotNull UserDto userDto, String email)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET email = ? WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized User changeFirstName(@NotNull Connection connection, @NotNull UserDto userDto, String firstName)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET first_name = ? WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized User changeLastName(@NotNull Connection connection, @NotNull UserDto userDto, String lastName)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET last_name = ? WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, lastName);
        preparedStatement.setString(2, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized User changePhone(@NotNull Connection connection, @NotNull UserDto userDto, String phone)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET phone = ? WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, phone);
        preparedStatement.setString(2, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized User changePassword(@NotNull Connection connection, @NotNull UserDto userDto, String password)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET password = ? WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, password);
        preparedStatement.setString(2, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized User disableUser(@NotNull Connection connection, @NotNull UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET enabled = 0 WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized User enableUser(@NotNull Connection connection, @NotNull UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET enabled = 1 WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        preparedStatement.executeUpdate();
        connection.commit();
        return findUserByLogin(connection, userDto);
    }

    public synchronized ArrayList<UserDto> getAllUsers(@NotNull Connection connection) throws SQLException {
        @Language("MySQL") String queryString = "SELECT login FROM user_list";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<UserDto> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(new UserDto(resultSet.getString("login")));
        }
        return users;
    }
}
