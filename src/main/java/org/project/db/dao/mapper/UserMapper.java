package org.project.db.dao.mapper;

import org.project.db.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ObjectMapper<User> {
    @Override
    public User extractFromResultSet(ResultSet resultSet) throws SQLException {
        return User.builder()
                .setId(Long.parseLong(resultSet.getString("user_id")))
                .setLogin(resultSet.getString("login"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setEmail(resultSet.getString("email"))
                .setPhone(resultSet.getString("phone"))
                .setPassword(resultSet.getString("password"))
                .setEnabled(Integer.parseInt(resultSet.getString("enabled")) == 1)
                .setDateCreated(resultSet.getTimestamp("date_created"))
                .setDateUpdated(resultSet.getTimestamp("date_modified"))
                .createUser();
    }
}
