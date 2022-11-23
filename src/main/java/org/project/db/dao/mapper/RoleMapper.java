package org.project.db.dao.mapper;

import org.project.db.model.Role;
import org.project.db.model.builder.RoleBuilderImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements ObjectMapper<Role> {
    @Override
    public Role extractFromResultSet(ResultSet rs) throws SQLException {
        return new RoleBuilderImpl()
                .setId(rs.getLong("id"))
                .setCode(rs.getString("code"))
                .setName(rs.getString("name"))
                .createRole();
    }
}
