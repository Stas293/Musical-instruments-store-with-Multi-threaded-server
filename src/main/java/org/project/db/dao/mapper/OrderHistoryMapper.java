package org.project.db.dao.mapper;

import org.project.db.model.OrderHistory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderHistoryMapper implements ObjectMapper<OrderHistory>{
    public OrderHistory extractFromResultSet(ResultSet resultSet) throws SQLException {
        return OrderHistory.builder()
                .setId(Long.parseLong(resultSet.getString("history_id")))
                .setDateCreated(resultSet.getTimestamp("date_created"))
                .setTotalSum(resultSet.getDouble("total_sum"))
                .setTitle(resultSet.getString("title"))
                .createOrderHistory();
    }
}
