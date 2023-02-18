package org.project.db.dao;

import org.project.db.model.Status;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StatusDao extends GenericDao<Status> {
    Optional<Status> getStatusByName(String name) throws SQLException;

    List<Status> getAllStatuses() throws SQLException;

    Optional<Status> findNextStatus(Status status) throws SQLException;

    Optional<Status> getStatusOfInstrument(long instrumentId);

    Optional<Status> getOrderStatus(Long id);

    Optional<Status> getHistoryStatus(Long id);
}
