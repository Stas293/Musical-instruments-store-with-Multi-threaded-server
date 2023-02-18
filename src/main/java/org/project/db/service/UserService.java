package org.project.db.service;

import org.jetbrains.annotations.NotNull;
import org.project.db.dao.DaoFactory;
import org.project.db.dao.RoleDao;
import org.project.db.dao.UserDao;
import org.project.db.dao.impl.DataSource;
import org.project.db.dto.RegistrationDto;
import org.project.db.dto.UserDto;
import org.project.db.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserService {
    private static final DaoFactory daoFactory = DaoFactory.getInstance();
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserService() {
    }

    @NotNull
    private static String getPasswordHash(UserDto userDto, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(userDto.login().getBytes());
        password = password.trim();
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();

        for (var aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        password = sb.toString();
        return password;
    }

    public List<UserDto> getAllUsers() {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.getAllUsers();
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return List.of();
    }

    public void changeEmail(UserDto userDto, String email) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.changeEmail(userDto, email);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void changeFirstName(UserDto userDto, String firstName) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.changeFirstName(userDto, firstName);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void changeLastName(UserDto userDto, String lastName) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.changeLastName(userDto, lastName);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void changePassword(UserDto userDto, String password) {
        try (Connection connection = DataSource.getConnection()) {
            password = getPasswordHash(userDto, password);
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.changePassword(userDto, password);
        } catch (NoSuchAlgorithmException | SQLException e) {
            logger.warning(e.getMessage());
        }
    }

    public void changePhone(UserDto userDto, String phone) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.changePhone(userDto, phone);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public Optional<User> findByLogin(String login) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            return userDao.findByLogin(login);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return Optional.empty();
    }

    public void disableUser(UserDto userDto) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.disableUser(userDto);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void enableUser(UserDto userDto) {
        try (Connection connection = DataSource.getConnection()) {
            UserDao userDao = daoFactory.createUserDao(connection);
            userDao.enableUser(userDto);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public Optional<User> create(RegistrationDto registrationDto) throws SQLException {
        Connection connection = DataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            UserDao userDao = daoFactory.createUserDao(connection);
            RoleDao roleDao = daoFactory.createRoleDao(connection);
            Optional<User> user = userDao.create(registrationDto);
            if (user.isPresent()) {
                roleDao.insertRoleForUser(
                        new UserDto(user.get().getLogin()),
                        "User");
                connection.commit();
                return user;
            }
        } catch (Exception e) {
            connection.rollback();
            logger.warning(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return Optional.empty();
    }
}
