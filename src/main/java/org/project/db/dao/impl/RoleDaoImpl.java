package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.project.db.dao.RoleDao;
import org.project.db.dao.mapper.RoleMapper;
import org.project.db.dto.UserDto;
import org.project.db.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoleDaoImpl implements RoleDao {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(RoleDaoImpl.class.getName());

    public RoleDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Role> create(Role entity) throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO role (code, name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.executeUpdate();
            connection.commit();
            return Optional.of(entity);
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection.rollback();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findById(Long id) {
        @Language("MySQL") String queryString = "SELECT * FROM role WHERE role_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exist = resultSet.next();
            if (exist) {
                return Optional.of(new RoleMapper().extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void update(Role entity) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE role SET code = ?, name = ? WHERE role_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, entity.getCode());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, String.valueOf(entity.getId()));
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection.rollback();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        @Language("MySQL") String queryString = "DELETE FROM role WHERE role_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(id));
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection.rollback();
        }
    }

    @Override
    public Optional<Role> findByCode(String code) {
        @Language("MySQL") String queryString = "SELECT * FROM role WHERE code = ?";
        return getRole(code, queryString);
    }

    private Optional<Role> getRole(String code, String queryString) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exist = resultSet.next();
            if (exist) {
                return Optional.of(new RoleMapper().extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByName(String name) {
        @Language("MySQL") String queryString = "SELECT * FROM role WHERE name = ?";
        return getRole(name, queryString);
    }

    @Override
    public List<Role> getRolesForUser(UserDto userDto) {
        @Language("MySQL") String queryString =
                "select r.* from user_list u " +
                        "join user_role ur on (u.user_id=ur.user_id) " +
                        "left join role r on (ur.role_id=r.role_id) " +
                        "where u.login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, userDto.login());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(new RoleMapper().extractFromResultSet(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Role> insertRoleForUser(UserDto userDto, String roleName) {
        @Language("MySQL") String queryString = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            @Language("MySQL") String idUser = "SELECT user_id FROM user_list WHERE login = ?";
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(idUser)) {
                preparedStatement1.setString(1, userDto.login());
                ResultSet resultSet = preparedStatement1.executeQuery();
                resultSet.next();
                preparedStatement.setString(1, resultSet.getString("user_id"));
            }
            @Language("MySQL") String idRole = "SELECT role_id FROM role WHERE name = ?";
            try (PreparedStatement preparedStatement2 = connection.prepareStatement(idRole)) {
                preparedStatement2.setString(1, roleName);
                ResultSet resultSet = preparedStatement2.executeQuery();
                resultSet.next();
                preparedStatement.setString(2, resultSet.getString("role_id"));
            }
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return getRolesForUser(userDto);
    }

    @Override
    public List<Role> findAll() {
    @Language("MySQL") String queryString = "SELECT * FROM role";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                roles.add(new RoleMapper().extractFromResultSet(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
