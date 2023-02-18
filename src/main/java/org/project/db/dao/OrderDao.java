package org.project.db.dao;

import org.project.db.dto.UserDto;
import org.project.db.model.Order;
import org.project.db.model.OrderHistory;
import org.project.db.model.Status;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    List<Order> getAllOrders(UserDto userDto) throws SQLException;

    Status updateOrderStatus(Long id, Status nextStatus) throws SQLException;

    Double getTotalSum(Long id) throws SQLException;

    OrderHistory insertOrderHistory(OrderHistory orderHistory) throws SQLException;

    List<OrderHistory> getUserOrderHistory(UserDto userDto) throws SQLException;
}
