package org.project.db.Repository;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.Dto.OrderDto;
import org.project.db.Dto.UserDto;
import org.project.db.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderRepository {
    public synchronized Order insertOrder(@NotNull Connection connection, @NotNull OrderDto orderDto) throws SQLException {
        String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, orderDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long userId = resultSet.getLong("user_id");
        String queryString1 = "SELECT status_id FROM status WHERE name = ?";
        preparedStatement = connection.prepareStatement(queryString1);
        preparedStatement.setString(1, orderDto.getStatus().getName());
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long statusId = resultSet.getLong("status_id");
        String queryString2 = "INSERT INTO order_list (user_id, status_id, title) VALUES (?, ?, ?)";
        preparedStatement = connection.prepareStatement(queryString2);
        preparedStatement.setString(1, String.valueOf(userId));
        preparedStatement.setString(2, String.valueOf(statusId));
        preparedStatement.setString(3, orderDto.getTitle());
        preparedStatement.executeUpdate();
        String queryString3 = "SELECT * FROM order_list WHERE user_id = ? order by date_created desc limit 1";
        preparedStatement = connection.prepareStatement(queryString3);
        preparedStatement.setString(1, String.valueOf(userId));
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        connection.commit();
        return new Order(Long.parseLong(resultSet.getString("order_id")), resultSet.getTimestamp("date_created"),
                new UserRepository().findUserById(connection, Long.valueOf(resultSet.getString("user_id"))).getLogin(), resultSet.getString("title"),
                new StatusRepository().getStatusById(connection, statusId), Integer.parseInt(resultSet.getString("closed")) == 1);
    }

    public synchronized ArrayList<Order> getAllOrders(@NotNull Connection connection, @NotNull UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        boolean isResult = resultSet.next();
        if (!isResult) {
            return new ArrayList<>();
        }
        Long userId = Long.valueOf(resultSet.getString("user_id"));
        @Language("MySQL") String queryString1 = "SELECT * FROM order_list WHERE user_id = ? order by date_created desc";
        preparedStatement = connection.prepareStatement(queryString1);
        preparedStatement.setString(1, String.valueOf(userId));
        resultSet = preparedStatement.executeQuery();
        ArrayList<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            @Language("MySQL") String queryString2 = "SELECT * FROM status WHERE status_id = ?";
            PreparedStatement preparedStatement1 = connection.prepareStatement(queryString2);
            preparedStatement1.setString(1, resultSet.getString("status_id"));
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            resultSet1.next();
            Status status = new Status(resultSet1.getLong("status_id"), resultSet1.getString("code"), resultSet1.getString("name"), Integer.parseInt(resultSet1.getString("closed")) == 1);
            @Language("MySQL") String queryString3 = "SELECT * FROM instrument_order WHERE order_id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(queryString3);
            preparedStatement2.setString(1, resultSet.getString("order_id"));
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            ArrayList<InstrumentOrder> instruments = new ArrayList<>();
            while (resultSet2.next()) {
                @Language("MySQL") String queryString4 = "SELECT * FROM instrument_list WHERE instrument_id = ?";
                PreparedStatement preparedStatement3 = connection.prepareStatement(queryString4);
                preparedStatement3.setString(1, resultSet2.getString("instrument_id"));
                ResultSet resultSet3 = preparedStatement3.executeQuery();
                resultSet3.next();
                @Language("MySQL") String queryString5 = "SELECT * FROM status WHERE status_id = ?";
                PreparedStatement preparedStatement4 = connection.prepareStatement(queryString5);
                preparedStatement4.setString(1, resultSet3.getString("status_id"));
                ResultSet resultSet4 = preparedStatement4.executeQuery();
                resultSet4.next();
                Status status1 = new Status(resultSet4.getLong("status_id"),
                        resultSet4.getString("code"), resultSet4.getString("name"),
                        Integer.parseInt(resultSet4.getString("closed")) == 1);
                instruments.add(new InstrumentOrder(new Instrument(resultSet3.getLong("instrument_id"), resultSet3.getTimestamp("date_created"), resultSet3.getTimestamp("date_updated"), resultSet3.getString("description"), resultSet3.getString("title"), status1, Double.parseDouble(resultSet3.getString("price"))), resultSet2.getDouble("price"), resultSet2.getInt("quantity")));
            }
            Order order = new Order(Long.parseLong(resultSet.getString("order_id")), resultSet.getTimestamp("date_created"), new UserRepository().findUserById(connection, Long.valueOf(resultSet.getString("user_id"))).getLogin(), resultSet.getString("title"), status, Integer.parseInt(resultSet.getString("closed")) == 1);
            order.setInstruments(instruments);
            orders.add(order);
        }
        return orders;
    }

    public synchronized Status getOrderStatus(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT status_id FROM order_list WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long statusId = resultSet.getLong("status_id");
        return new StatusRepository().getStatusById(connection, statusId);
    }

    public synchronized Status updateOrderStatus(@NotNull Connection connection, @NotNull Long id, @NotNull Status nextStatus) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE order_list SET status_id = ? WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(nextStatus.getId()));
        preparedStatement.setString(2, String.valueOf(id));
        preparedStatement.executeUpdate();
        connection.commit();
        return nextStatus;
    }

    public synchronized Order getOrderById(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM order_list WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        @Language("MySQL") String queryString1 = "SELECT * FROM status WHERE status_id = ?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(queryString1);
        preparedStatement1.setString(1, resultSet.getString("status_id"));
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        Status status = new Status(resultSet1.getLong("status_id"), resultSet1.getString("code"), resultSet1.getString("name"), Integer.parseInt(resultSet1.getString("closed")) == 1);
        @Language("MySQL") String queryString2 = "SELECT * FROM instrument_order WHERE order_id = ?";
        PreparedStatement preparedStatement2 = connection.prepareStatement(queryString2);
        preparedStatement2.setString(1, resultSet.getString("order_id"));
        ResultSet resultSet2 = preparedStatement2.executeQuery();
        ArrayList<InstrumentOrder> instruments = new ArrayList<>();
        while (resultSet2.next()) {
            @Language("MySQL") String queryString3 = "SELECT * FROM instrument_list WHERE instrument_id = ?";
            PreparedStatement preparedStatement3 = connection.prepareStatement(queryString3);
            preparedStatement3.setString(1, resultSet2.getString("instrument_id"));
            ResultSet resultSet3 = preparedStatement3.executeQuery();
            resultSet3.next();
            @Language("MySQL") String queryString4 = "SELECT * FROM status WHERE status_id = ?";
            PreparedStatement preparedStatement4 = connection.prepareStatement(queryString4);
            preparedStatement4.setString(1, resultSet3.getString("status_id"));
            ResultSet resultSet4 = preparedStatement4.executeQuery();
            resultSet4.next();
            Status status1 = new Status(resultSet4.getLong("status_id"),
                    resultSet4.getString("code"), resultSet4.getString("name"),
                    Integer.parseInt(resultSet4.getString("closed")) == 1);
            instruments.add(new InstrumentOrder(new Instrument(resultSet3.getLong("instrument_id"), resultSet3.getTimestamp("date_created"), resultSet3.getTimestamp("date_updated"), resultSet3.getString("description"), resultSet3.getString("title"), status1, Double.parseDouble(resultSet3.getString("price"))), resultSet2.getDouble("price"), resultSet2.getInt("quantity")));
        }
        Order order = new Order(Long.parseLong(resultSet.getString("order_id")), resultSet.getTimestamp("date_created"), new UserRepository().findUserById(connection, Long.valueOf(resultSet.getString("user_id"))).getLogin(), resultSet.getString("title"), status, Integer.parseInt(resultSet.getString("closed")) == 1);
        order.setInstruments(instruments);
        return order;
    }

    public synchronized Double getTotalSum(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM instrument_order WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        double sum = 0.0;
        while (resultSet.next()) {
            sum += resultSet.getDouble("price") * resultSet.getInt("quantity");
        }
        return sum;
    }

    public synchronized OrderHistory insertOrderHistory(@NotNull Connection connection, @NotNull OrderHistory orderHistory) throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO order_history (user_id, total_sum, title, status_id) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(orderHistory.getUser().getId()));
        preparedStatement.setString(2, String.valueOf(orderHistory.getTotalSum()));
        preparedStatement.setString(3, orderHistory.getTitle());
        preparedStatement.setString(4, String.valueOf(orderHistory.getStatus().getId()));
        preparedStatement.executeUpdate();
        connection.commit();
        return orderHistory;
    }

    public synchronized Order deleteOrder(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        @Language("MySQL") String queryString1 = "SELECT * FROM order_list WHERE order_id = ?";
        PreparedStatement preparedStatement1 = connection.prepareStatement(queryString1);
        preparedStatement1.setString(1, String.valueOf(id));
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        resultSet1.next();
        @Language("MySQL") String queryString = "DELETE FROM order_list WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        preparedStatement.executeUpdate();
        connection.commit();
        return new Order(Long.parseLong(resultSet1.getString("order_id")), resultSet1.getTimestamp("date_created"), new UserRepository().findUserById(connection, Long.valueOf(resultSet1.getString("user_id"))).getLogin(), resultSet1.getString("title"), new StatusRepository().getStatusById(connection, Long.parseLong(resultSet1.getString("status_id"))), Integer.parseInt(resultSet1.getString("closed")) == 1);
    }

    public synchronized ArrayList<OrderHistory> getUserOrderHistory(@NotNull Connection connection, @NotNull UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.getLogin());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        @Language("MySQL") String queryString1 = "SELECT * FROM order_history WHERE user_id = ? order by date_created";
        PreparedStatement preparedStatement1 = connection.prepareStatement(queryString1);
        preparedStatement1.setString(1, resultSet.getString("user_id"));
        ResultSet resultSet1 = preparedStatement1.executeQuery();
        ArrayList<OrderHistory> orderHistories = new ArrayList<>();
        while (resultSet1.next()) {
            @Language("MySQL") String queryString2 = "SELECT * FROM status WHERE status_id = ?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(queryString2);
            preparedStatement2.setString(1, resultSet1.getString("status_id"));
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            resultSet2.next();
            Status status = new Status(resultSet2.getLong("status_id"), resultSet2.getString("code"), resultSet2.getString("name"), Integer.parseInt(resultSet2.getString("closed")) == 1);
            orderHistories.add(new OrderHistory(Long.parseLong(resultSet1.getString("history_id")),
                    resultSet1.getTimestamp("date_created"), new User(Long.parseLong(resultSet1.getString("user_id")),
                    resultSet.getString("login"), resultSet.getString("first_name"),
                    resultSet.getString("last_name"), resultSet.getString("email"),
                    resultSet.getString("phone"), resultSet.getString("password"),
                    Integer.parseInt(resultSet.getString("enabled")) == 1, resultSet.getTimestamp("date_created"),
                    resultSet.getTimestamp("date_modified"), new RoleRepository().getRolesForUser(connection, new UserDto(resultSet.getString("login")))), resultSet1.getDouble("total_sum"), resultSet1.getString("title"), status));
        }
        return orderHistories;
    }
}
