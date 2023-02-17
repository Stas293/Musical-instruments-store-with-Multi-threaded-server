package org.project.db.dao.mapper;

import org.project.db.dao.InstrumentOrderDao;
import org.project.db.dao.StatusDao;
import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.model.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements ObjectMapper<Order> {
    @Override
    public Order extractFromResultSet(ResultSet rs) throws SQLException {
        try (StatusDao statusDao = JDBCDaoFactory.getInstance().createStatusDao();
             InstrumentOrderDao instrumentOrderDao = JDBCDaoFactory.getInstance().createInstrumentOrderDao();
             UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            return Order.builder()
                    .setId(rs.getLong("id"))
                    .setDateCreated(rs.getTimestamp("date_created"))
                    .setStatus(statusDao.findById(rs.getLong("status_id")).orElseThrow())
                    .setTitle(rs.getString("title"))
                    .setLogin(userDao.findById(rs.getLong("user_id")).orElseThrow().getLogin())
                    .setClosed(rs.getBoolean("closed"))
                    .setInstruments(instrumentOrderDao.getInstrumentsForOrder(rs.getLong("id")))
                    .createOrder();
        }
    }
}
