package org.project.db.Repository;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.Model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StatusRepository {
    public synchronized Status getStatusById(@NotNull Connection connection, @NotNull Long id)
            throws SQLException {
        @Language("MySQL") String queryString = "select * from status where status_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new Status(Long.parseLong(resultSet.getString("status_id")),
                resultSet.getString("code"), resultSet.getString("name"),
                Integer.parseInt(resultSet.getString("closed")) == 1);
    }

    public synchronized Status getStatusByName(@NotNull Connection connection, @NotNull String name)
            throws SQLException {
        @Language("MySQL") String queryString = "select * from status where name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new Status(Long.parseLong(resultSet.getString("status_id")),
                resultSet.getString("code"), resultSet.getString("name"),
                Integer.parseInt(resultSet.getString("closed")) == 1);
    }

    public synchronized ArrayList<Status> getAllStatuses(@NotNull Connection connection)
            throws SQLException {
        @Language("MySQL") String queryString = "select * from status";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Status> statuses = new ArrayList<>();
        while (resultSet.next()) {
            statuses.add(new Status(Long.parseLong(resultSet.getString("status_id")),
                    resultSet.getString("code"), resultSet.getString("name"),
                    Integer.parseInt(resultSet.getString("closed")) == 1));
        }
        return statuses;
    }

    public synchronized Status updateStatus(@NotNull Connection connection, @NotNull Status status)
            throws SQLException {
        @Language("MySQL") String queryString = "update status set code = ?, name = ?, closed = ? where status_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, status.getCode());
        preparedStatement.setString(2, status.getName());
        preparedStatement.setInt(3, status.isClosed() ? 1 : 0);
        preparedStatement.setString(4, String.valueOf(status.getId()));
        preparedStatement.executeUpdate();
        return status;
    }

    public synchronized Status findNextStatus(@NotNull Connection connection, @NotNull Status status)
            throws SQLException {
        @Language("MySQL") String queryString = "SELECT status.* FROM next_status join `status` ON next_status.next_status_id = status.status_id where next_status.status_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(status.getId()));
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean exist = resultSet.next();
        if (!exist) {
            return null;
        }
        return new Status(Long.parseLong(resultSet.getString("status_id")),
                resultSet.getString("code"), resultSet.getString("name"),
                Integer.parseInt(resultSet.getString("closed")) == 1);
    }
}
