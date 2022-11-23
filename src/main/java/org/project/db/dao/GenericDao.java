package org.project.db.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {
    Optional<T> create(T entity) throws SQLException;

    Optional<T> findById(Long id);

    void update(T entity) throws SQLException;

    void delete(Long id) throws SQLException;

    void close();
}
