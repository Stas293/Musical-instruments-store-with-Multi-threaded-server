package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.dao.StatusDao;
import org.project.db.dao.mapper.StatusMapper;
import org.project.db.model.Status;
import org.project.db.model.builder.StatusBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class StatusDaoImpl implements StatusDao {
    private final Connection connection;
    private final Logger logger = Logger.getLogger(StatusDaoImpl.class.getName());

    public StatusDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Status> create(Status entity) {
        @Language("MySQL")
        String query = "INSERT INTO status (code, name) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getName());
            statement.executeUpdate();
            return Optional.of(entity);
        } catch (SQLException e) {
            logger.warning("Can't create status: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Status> findById(@NotNull Long id) {
        @Language("MySQL") String queryString = "select * from status where status_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(id));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new StatusMapper().extractFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            close();
        }
        return Optional.empty();
    }

    public Optional<Status> getStatusByName(@NotNull String name)
            throws SQLException {
        @Language("MySQL") String queryString = "select * from status where name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new StatusMapper().extractFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            close();
        }
        return Optional.empty();
    }

    public List<Status> getAllStatuses()
            throws SQLException {
        @Language("MySQL") String queryString = "select * from status";
        List<Status> statuses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    statuses.add(new StatusMapper().extractFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            close();
        }
        return statuses;
    }

    public void update(@NotNull Status status)
            throws SQLException {
        @Language("MySQL") String queryString = "update status set code = ?, name = ?, closed = ? where status_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, status.getCode());
            preparedStatement.setString(2, status.getName());
            preparedStatement.setBoolean(3, status.isClosed());
            preparedStatement.setLong(4, status.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            close();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from status where status_id = ?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
    }

    public Status findNextStatus(@NotNull Status status)
            throws SQLException {
        @Language("MySQL") String queryString = "SELECT status.* FROM next_status join `status` ON " +
                "next_status.next_status_id = status.status_id where next_status.status_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, status.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new StatusMapper().extractFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        } finally {
            close();
        }
        return getStatusByName("Arrived").orElseThrow(() -> new SQLException("Status not found"));
    }
}
