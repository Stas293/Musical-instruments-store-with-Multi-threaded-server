package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.project.db.dao.OrderDao;
import org.project.db.dao.UserDao;
import org.project.db.dao.mapper.OrderMapper;
import org.project.db.dao.mapper.StatusMapper;
import org.project.db.dto.OrderDto;
import org.project.db.dto.UserDto;
import org.project.db.model.*;
import org.project.db.model.builder.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class OrderDaoImpl implements OrderDao {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(OrderDaoImpl.class.getName());

    public OrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Order insertOrder(OrderDto orderDto) throws SQLException {
        String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, orderDto.login());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long userId = resultSet.getLong("user_id");
        String queryString1 = "SELECT status_id FROM status WHERE name = ?";
        preparedStatement = connection.prepareStatement(queryString1);
        preparedStatement.setString(1, orderDto.status().getName());
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long statusId = resultSet.getLong("status_id");
        String queryString2 = "INSERT INTO order_list (user_id, status_id, title) VALUES (?, ?, ?)";
        preparedStatement = connection.prepareStatement(queryString2);
        preparedStatement.setString(1, String.valueOf(userId));
        preparedStatement.setString(2, String.valueOf(statusId));
        preparedStatement.setString(3, orderDto.title());
        preparedStatement.executeUpdate();
        String queryString3 = "SELECT * FROM order_list WHERE user_id = ? order by date_created desc limit 1";
        preparedStatement = connection.prepareStatement(queryString3);
        preparedStatement.setString(1, String.valueOf(userId));
        resultSet = preparedStatement.executeQuery();
        resultSet.next();
        connection.commit();
        return new OrderMapper().extractFromResultSet(resultSet);
    }

    @Override
    public List<Order> getAllOrders(UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "SELECT user_id FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.login());
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
            Status status = new StatusMapper().extractFromResultSet(resultSet1);
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
                Status status1 = new StatusMapper().extractFromResultSet(resultSet4);
                instruments.add(new InstrumentOrderBuilderImpl().setInstrument(new InstrumentBuilderImpl().setId(resultSet3.getLong("instrument_id")).setDateCreated(resultSet3.getTimestamp("date_created")).setDateUpdated(resultSet3.getTimestamp("date_updated")).setDescription(resultSet3.getString("description")).setTitle(resultSet3.getString("title")).setStatus(status1).setPrice(Double.parseDouble(resultSet3.getString("price"))).createInstrument()).setPrice(resultSet2.getDouble("price")).setQuantity(resultSet2.getInt("quantity")).createInstrumentOrder());
            }
            Order order = new OrderMapper().extractFromResultSet(resultSet);
            order.setInstruments(instruments);
            orders.add(order);
        }
        return orders;
    }

    @Override
    public Status getOrderStatus(Long id) throws SQLException {
        @Language("MySQL") String queryString = "SELECT status_id FROM order_list WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(id));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Long statusId = resultSet.getLong("status_id");
        try (PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM status WHERE status_id = ?")) {
            preparedStatement1.setString(1, String.valueOf(statusId));
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            resultSet1.next();
            return new StatusMapper().extractFromResultSet(resultSet1);
        }
    }

    @Override
    public Status updateOrderStatus(Long id, Status nextStatus) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE order_list SET status_id = ? WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(nextStatus.getId()));
        preparedStatement.setString(2, String.valueOf(id));
        preparedStatement.executeUpdate();
        connection.commit();
        return nextStatus;
    }

    @Override
    public Order getOrderById(Long id) throws SQLException {
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
        Status status = new StatusBuilderImpl().setId(resultSet1.getLong("status_id")).setCode(resultSet1.getString("code")).setName(resultSet1.getString("name")).setClosed(Integer.parseInt(resultSet1.getString("closed")) == 1).createStatus();
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
            Status status1 = new StatusBuilderImpl().setId(resultSet4.getLong("status_id")).setCode(resultSet4.getString("code")).setName(resultSet4.getString("name")).setClosed(Integer.parseInt(resultSet4.getString("closed")) == 1).createStatus();
            instruments.add(new InstrumentOrderBuilderImpl().setInstrument(new InstrumentBuilderImpl().setId(resultSet3.getLong("instrument_id")).setDateCreated(resultSet3.getTimestamp("date_created")).setDateUpdated(resultSet3.getTimestamp("date_updated")).setDescription(resultSet3.getString("description")).setTitle(resultSet3.getString("title")).setStatus(status1).setPrice(Double.parseDouble(resultSet3.getString("price"))).createInstrument()).setPrice(resultSet2.getDouble("price")).setQuantity(resultSet2.getInt("quantity")).createInstrumentOrder());
        }
        Order order = new OrderMapper().extractFromResultSet(resultSet);
        order.setInstruments(instruments);
        return order;
    }

    @Override
    public synchronized Double getTotalSum(Long id) throws SQLException {
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

    @Override
    public OrderHistory insertOrderHistory(OrderHistory orderHistory) throws SQLException {
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

    @Override
    public Order deleteOrder(Long id) throws SQLException {
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
        return new OrderMapper().extractFromResultSet(resultSet1);
    }

    @Override
    public ArrayList<OrderHistory> getUserOrderHistory(UserDto userDto) throws SQLException {
        @Language("MySQL") String queryString = "SELECT * FROM user_list WHERE login = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, userDto.login());
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
            Status status = new StatusBuilderImpl().setId(resultSet2.getLong("status_id")).setCode(resultSet2.getString("code")).setName(resultSet2.getString("name")).setClosed(Integer.parseInt(resultSet2.getString("closed")) == 1).createStatus();
            orderHistories.add(new OrderHistoryBuilderImpl().setId(Long.parseLong(resultSet1.getString("history_id"))).setDateCreated(resultSet1.getTimestamp("date_created")).setUser(new UserBuilderImpl().setId(Long.parseLong(resultSet1.getString("user_id"))).setLogin(resultSet.getString("login")).setFirstName(resultSet.getString("first_name")).setLastName(resultSet.getString("last_name")).setEmail(resultSet.getString("email")).setPhone(resultSet.getString("phone")).setPassword(resultSet.getString("password")).setEnabled(Integer.parseInt(resultSet.getString("enabled")) == 1).setDateCreated(resultSet.getTimestamp("date_created")).setDateUpdated(resultSet.getTimestamp("date_modified")).setRoles(JDBCDaoFactory.getInstance().createRoleDao().getRolesForUser(new UserDto(resultSet.getString("login")))).createUser()).setTotalSum(resultSet1.getDouble("total_sum")).setTitle(resultSet1.getString("title")).setStatus(status).createOrderHistory());
        }
        return orderHistories;
    }

    @Override
    public Optional<Order> create(Order entity) throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO order_list (user_id, title, status_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString);
             UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            preparedStatement.setString(1, String.valueOf(userDao.findByLogin(entity.getLogin()).orElseThrow().getId()));
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, String.valueOf(entity.getStatus().getId()));
            preparedStatement.executeUpdate();
            connection.commit();
            return Optional.of(entity);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Order entity) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE order_list SET title = ?, status_id = ?, closed = ? WHERE order_id = ?";
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
        } finally {
            close();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        @Language("MySQL") String queryString = "DELETE FROM order_list WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            connection.rollback();
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
