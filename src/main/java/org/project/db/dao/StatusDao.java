package org.project.db.dao;

import org.project.db.model.Status;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StatusDao extends GenericDao<Status> {
    Optional<Status> getStatusByName(String name) throws SQLException;

    List<Status> getAllStatuses() throws SQLException;

    Status findNextStatus(Status status) throws SQLException;
}
