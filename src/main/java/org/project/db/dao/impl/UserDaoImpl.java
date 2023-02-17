package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.dao.UserDao;
import org.project.db.dao.mapper.UserMapper;
import org.project.db.dto.RegistrationDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;
import org.project.db.model.builder.UserBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    public Optional<User> enableUser(UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET enabled = 1 WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, userDto.login());
            preparedStatement.executeUpdate();
            connection.commit();
            return findByLogin(userDto.login());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while enabling user", e);
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        return Optional.empty();
    }

    public List<UserDto> getAllUsers() throws SQLException {
        @Language("MySQL") String queryString = "SELECT login FROM user_list";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserDto> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new UserDto(resultSet.getString("login")));
            }
            return users;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while getting all users", e);
        } finally {
            connection.setAutoCommit(true);
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<User> create(RegistrationDto registrationDto) throws SQLException {
        return create(new UserBuilderImpl()
                .setLogin(registrationDto.login())
                .setPassword(registrationDto.password())
                .setFirstName(registrationDto.firstName())
                .setLastName(registrationDto.lastName())
                .setEmail(registrationDto.email())
                .setPhone(registrationDto.phone())
                .createUser());
    }

    @Override
    public Optional<User> create(User user) throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO user_list " +
                "(login, first_name, last_name, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhone());
            preparedStatement.setString(6, user.getPassword());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            new RoleDaoImpl(connection).insertRoleForUser(new UserDto(user.getLogin()), "user");
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while creating user", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
        return findByLogin(user.getLogin());
    }

    @Override
    public Optional<User> findById(Long id) {
        UserMapper userMapper = new UserMapper();
        @Language("MySQL") String queryString = "SELECT * FROM user_list WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = userMapper.extractFromResultSet(resultSet);
                user.setRoles(new RoleDaoImpl(connection).getRolesForUser(new UserDto(user.getLogin())));
                return Optional.of(userMapper.extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Can't find user by id", e);
        } finally {
            close();
        }
        return Optional.empty();
    }

    @Override
    public void update(User entity) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET first_name = ?, last_name = ?, email = ?, phone = ?, password = ? WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPhone());
            preparedStatement.setString(5, entity.getPassword());
            preparedStatement.setString(6, entity.getLogin());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while updating user", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        @Language("MySQL") String queryString = "DELETE FROM user_list WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, String.valueOf(id));
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while deleting user", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Can't close connection", e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        UserMapper userMapper = new UserMapper();
        @Language("MySQL") String queryString = "SELECT * FROM user_list WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = userMapper.extractFromResultSet(resultSet);
                user.setRoles(new RoleDaoImpl(connection).getRolesForUser(new UserDto(user.getLogin())));
                return Optional.of(userMapper.extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Can't find user by login", e);
        } finally {
            close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> disableUser(UserDto userDto) {
        @Language("MySQL") String queryString = "UPDATE user_list SET enabled = 0 WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, userDto.login());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while disabling user", e);
        } finally {
            close();
        }
        return findByLogin(userDto.login());
    }

    public Optional<User> changeEmail(@NotNull UserDto userDto, String email)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET email = ? WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, userDto.login());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while changing email", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
        return findByLogin(userDto.login());
    }

    public Optional<User> changeFirstName(@NotNull UserDto userDto, String firstName)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET first_name = ? WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, userDto.login());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while changing first name", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
        return findByLogin(userDto.login());
    }

    public Optional<User> changeLastName(@NotNull UserDto userDto, String lastName)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET last_name = ? WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, userDto.login());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while changing last name", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
        return findByLogin(userDto.login());
    }

    public Optional<User> changePhone(@NotNull UserDto userDto, String phone)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET phone = ? WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, userDto.login());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while changing phone", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
        return findByLogin(userDto.login());
    }

    public Optional<User> changePassword(@NotNull UserDto userDto, String password)
            throws SQLException {
        @Language("MySQL") String queryString = "UPDATE user_list SET password = ? WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);){
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, userDto.login());
            connection.setAutoCommit(false);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.log(Level.SEVERE, "Error while changing password", e);
        } finally {
            connection.setAutoCommit(true);
            close();
        }
        return findByLogin(userDto.login());
    }
}
