package org.project.db.dao.mapper;

import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements ObjectMapper<Order> {
    @Override
    public Order extractFromResultSet(ResultSet rs) throws SQLException {
        return Order.builder()
                .setId(rs.getLong("id"))
                .setDateCreated(rs.getTimestamp("date_created"))
                .setStatus(JDBCDaoFactory.getInstance().createStatusDao().findById(rs.getLong("status_id")).get())
                .setTitle(rs.getString("title"))
                .setLogin(rs.getString("login"))
                .setClosed(rs.getBoolean("closed"))
                .setInstruments(JDBCDaoFactory.getInstance().createInstrumentOrderDao().)
    }
}
