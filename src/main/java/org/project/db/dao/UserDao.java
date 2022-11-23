package org.project.db.dao;

import org.project.db.dto.RegistrationDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao extends GenericDao<User> {
    Optional<User> create(RegistrationDto registrationDto) throws SQLException;

    Optional<User> findByLogin(String login);

    Optional<User> disableUser(UserDto userDto) throws SQLException;

    Optional<User> enableUser(UserDto userDto) throws SQLException;

    List<UserDto> getAllUsers() throws SQLException;

    Optional<User> changeEmail(UserDto userDto) throws SQLException;

    Optional<User> changePassword(UserDto userDto) throws SQLException;

    Optional<User> changeFirstName(UserDto userDto) throws SQLException;

    Optional<User> changeLastName(UserDto userDto) throws SQLException;

    Optional<User> changePhone(UserDto userDto) throws SQLException;
}
