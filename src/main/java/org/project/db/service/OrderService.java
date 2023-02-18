package org.project.db.service;

import org.project.db.dao.*;
import org.project.db.dao.impl.DataSource;
import org.project.db.dto.OrderDto;
import org.project.db.dto.UserDto;
import org.project.db.model.*;
import org.project.db.utility.Mapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class OrderService {
    private static final DaoFactory daoFactory = DaoFactory.getInstance();
    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    public OrderHistory updateOrderStatus(Long id, Status nextStatus) throws SQLException {
        Connection connection = DataSource.getConnection();
        try {
            OrderDao orderDao = daoFactory.createOrderDao(connection);
            UserDao userDao = daoFactory.createUserDao(connection);
            connection.setAutoCommit(false);
            InstrumentOrderDao instrumentOrderDao = daoFactory.createInstrumentOrderDao(connection);
            nextStatus = orderDao.updateOrderStatus(id, nextStatus);
            if (Objects.equals(nextStatus.getName(), "Arrived")) {
                double totalSum = orderDao.getTotalSum(id);
                Order orderToHistory = orderDao.findById(id).orElseThrow();
                User user = userDao.findByLogin(orderToHistory.getLogin()).orElseThrow();
                instrumentOrderDao.deleteAllInstrumentOrdersByOrderId(id);
                orderDao.delete(id);
                OrderHistory orderHistory = Mapper.mapOrderHistory(orderToHistory, totalSum, user);
                orderHistory.setStatus(nextStatus);
                orderDao.insertOrderHistory(orderHistory);
                connection.commit();
                return orderHistory;
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return null;
    }

    public List<OrderHistory> getUserOrderHistory(UserDto userDto) {
        try (Connection connection = DataSource.getConnection()) {
            OrderDao orderDao = daoFactory.createOrderDao(connection);
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            UserDao userDao = daoFactory.createUserDao(connection);
            List<OrderHistory> orderHistoryList = orderDao.getUserOrderHistory(userDto);
            orderHistoryList.forEach(orderHistory -> {
                orderHistory.setStatus(
                        statusDao.getHistoryStatus(orderHistory.getId())
                                .orElseThrow());
                orderHistory.setUser(
                        userDao.findByHistoryId(orderHistory.getId())
                                .orElseThrow()
                );
            });
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Order> getAllOrders(UserDto userDto) {
        try (Connection connection = DataSource.getConnection()) {
            OrderDao orderDao = daoFactory.createOrderDao(connection);
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            InstrumentOrderDao instrumentOrderDao = daoFactory.createInstrumentOrderDao(connection);
            UserDao userDao = daoFactory.createUserDao(connection);
            List<Order> orders = orderDao.getAllOrders(userDto);
            orders.forEach(order -> {
                order.setStatus(
                        statusDao.getOrderStatus(order.getId()).orElseThrow());
                order.setInstruments(
                        instrumentOrderDao.getInstrumentsForOrder(order.getId()));
                order.setLogin(
                        userDao.findByOrderId(order.getId()).orElseThrow().getLogin()
                );
            });
            return orders;
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    public Order makeOrder(OrderDto orderDto, List<InstrumentOrder> instrumentOrders) throws SQLException {
        Connection connection = DataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            OrderDao orderDao = daoFactory.createOrderDao(connection);
            InstrumentOrderDao instrumentOrderDao = daoFactory.createInstrumentOrderDao(connection);
            Order order = orderDao.create(
                    Mapper.mapOrder(orderDto, instrumentOrders)).orElseThrow();
            instrumentOrderDao.insertInstrumentOrder(instrumentOrders, order);
            connection.commit();
            return order;
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return null;
    }
}
