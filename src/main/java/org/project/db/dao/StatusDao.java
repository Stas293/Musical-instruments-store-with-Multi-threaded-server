package org.project.db.dao;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.model.Status;
import org.project.db.model.builder.StatusBuilderImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface StatusDao extends GenericDao<Status> {
    Optional<Status> getStatusByName(String name) throws SQLException;

    List<Status> getAllStatuses() throws SQLException;

    Status findNextStatus(Status status) throws SQLException;
}
