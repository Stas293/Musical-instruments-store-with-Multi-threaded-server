package org.project.db.dao;

import org.project.db.model.Status;

import java.util.List;
import java.util.Optional;

public interface StatusDao extends GenericDao<Status> {
    Optional<Status> findByCode(String code);

    Optional<Status> findByRequestId(Long requestId);

    Optional<Status> findByHistoryRequestId(Long historyRequestId);

    Optional<List<Status>> findNextStatusesForCurrentStatusById(Long id);
}
