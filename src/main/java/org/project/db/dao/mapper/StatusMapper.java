package org.project.db.dao.mapper;

import org.project.db.model.Status;
import org.project.db.model.builder.StatusBuilderImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMapper implements ObjectMapper<Status> {
    @Override
    public Status extractFromResultSet(ResultSet rs) throws SQLException {
        return Status.builder()
                .setId(rs.getLong("id"))
                .setCode(rs.getString("code"))
                .setName(rs.getString("name"))
                .createStatus();
    }
}
