package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.project.db.dao.OrderDao;
import org.project.db.dao.mapper.OrderHistoryMapper;
import org.project.db.dao.mapper.OrderMapper;
import org.project.db.dto.UserDto;
import org.project.db.model.Order;
import org.project.db.model.OrderHistory;
import org.project.db.model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class OrderDaoImpl implements OrderDao {
    private static final Logger logger = Logger.getLogger(OrderDaoImpl.class.getName());
    private final Connection connection;

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Order> getAllOrders(UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString =
                "SELECT * FROM order_list " +
                        "INNER JOIN user_list " +
                        "ON order_list.user_id = user_list.user_id " +
                        "WHERE user_list.login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, userDto.login());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                orders.add(new OrderMapper().extractFromResultSet(resultSet));
            }
            return orders;
        }
    }

    @Override
    public Status updateOrderStatus(Long id, Status nextStatus) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE order_list SET status_id = ? WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(nextStatus.getId()));
            preparedStatement.setString(2, String.valueOf(id));
            preparedStatement.executeUpdate();
            connection.commit();
            return nextStatus;
        }
    }

    @Override
    public synchronized Double getTotalSum(Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM instrument_order WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            double sum = 0.0;
            while (resultSet.next()) {
                sum += resultSet.getDouble("price") * resultSet.getInt("quantity");
            }
            return sum;
        }
    }

    @Override
    public OrderHistory insertOrderHistory(OrderHistory orderHistory) throws SQLException {
        @Language("MySQL") String queryString =
                "INSERT INTO order_history (user_id, total_sum, title, status_id) " +
                        "VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(orderHistory.getUser().getId()));
            preparedStatement.setString(2, String.valueOf(orderHistory.getTotalSum()));
            preparedStatement.setString(3, orderHistory.getTitle());
            preparedStatement.setString(4, String.valueOf(orderHistory.getStatus().getId()));
            preparedStatement.executeUpdate();
            connection.commit();
            return orderHistory;
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public List<OrderHistory> getUserOrderHistory(UserDto userDto) {
        @Language("MySQL") String queryString1 =
                "SELECT * FROM order_history " +
                        "INNER JOIN user_list " +
                        "ON order_history.user_id = user_list.user_id " +
                        "WHERE user_list.login = ?";
        try (PreparedStatement preparedStatement1 = connection.prepareStatement(queryString1)) {
            preparedStatement1.setString(1, userDto.login());
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            ArrayList<OrderHistory> orderHistories = new ArrayList<>();
            OrderHistoryMapper orderHistoryMapper = new OrderHistoryMapper();
            while (resultSet1.next()) {
                orderHistories.add(orderHistoryMapper.extractFromResultSet(resultSet1));
            }
            return orderHistories;
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Order> create(Order entity) {
        @Language("MySQL") String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, entity.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Long userId = resultSet.getLong("user_id");
            @Language("MySQL") String queryString1 = "SELECT status_id FROM status WHERE name = ?";
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(queryString1)) {
                preparedStatement1.setString(1, entity.getStatus().getName());
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                resultSet1.next();
                Long statusId = resultSet1.getLong("status_id");
                @Language("MySQL") String queryString2 =
                        "INSERT INTO order_list (user_id, status_id, title) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement2 = connection.prepareStatement(queryString2)) {
                    preparedStatement2.setString(1, String.valueOf(userId));
                    preparedStatement2.setString(2, String.valueOf(statusId));
                    preparedStatement2.setString(3, entity.getTitle());
                    preparedStatement2.executeUpdate();
                    @Language("MySQL") String queryString3 =
                            "SELECT * FROM order_list WHERE user_id = ? order by date_created desc limit 1";
                    try (PreparedStatement preparedStatement3 = connection.prepareStatement(queryString3)) {
                        preparedStatement3.setString(1, String.valueOf(userId));
                        ResultSet resultSet2 = preparedStatement3.executeQuery();
                        resultSet2.next();
                        connection.commit();
                        return Optional.of(new OrderMapper().extractFromResultSet(resultSet2));
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> findById(Long id) {
        @Language("MySQL") String queryString = "SELECT * FROM order_list WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new OrderMapper().extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Order entity) throws SQLException {
        @Language("MySQL") String queryString =
                "UPDATE order_list SET title = ?, status_id = ?, closed = ? WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setLong(2, entity.getStatus().getId());
            preparedStatement.setBoolean(3, entity.isClosed());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection.rollback();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        @Language("MySQL") String queryString = "DELETE FROM order_list WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, id);
            @Language("MySQL") String queryString1 = "DELETE FROM instrument_order WHERE order_id = ?";
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(queryString1)) {
                preparedStatement1.setLong(1, id);
                preparedStatement1.executeUpdate();
                connection.commit();
            }
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection.rollback();
        }
    }
}
