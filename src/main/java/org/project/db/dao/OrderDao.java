package org.project.db.dao;

import org.project.db.dto.OrderDto;
import org.project.db.dto.UserDto;
import org.project.db.model.Order;
import org.project.db.model.OrderHistory;
import org.project.db.model.Status;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OrderDao extends GenericDao<Order> {
    Order insertOrder(OrderDto orderDto) throws SQLException;

    List<Order> getAllOrders(UserDto userDto) throws SQLException;

    Status getOrderStatus(Long id) throws SQLException;

    Status updateOrderStatus(Long id, Status nextStatus) throws SQLException;

    Order getOrderById(Long id) throws SQLException;

    Double getTotalSum(Long id) throws SQLException;

    OrderHistory insertOrderHistory(OrderHistory orderHistory) throws SQLException;

    Order deleteOrder(Long id) throws SQLException;

    ArrayList<OrderHistory> getUserOrderHistory(UserDto userDto) throws SQLException;
}
