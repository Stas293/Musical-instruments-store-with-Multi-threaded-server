package org.project.db.dao.mapper;

import org.project.db.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements ObjectMapper<Order> {
    @Override
    public Order extractFromResultSet(ResultSet rs) throws SQLException {
        return Order.builder()
                .setId(rs.getLong("id"))
                .setDateCreated(rs.getTimestamp("date_created"))
                .setTitle(rs.getString("title"))
                .setClosed(rs.getBoolean("closed"))
                .createOrder();
    }
}
