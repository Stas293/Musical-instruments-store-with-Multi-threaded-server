package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.project.db.dao.InstrumentOrderDao;
import org.project.db.dao.mapper.InstrumentMapper;
import org.project.db.model.Instrument;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstrumentOrderDaoImpl implements InstrumentOrderDao {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(InstrumentOrderDaoImpl.class.getName());

    public InstrumentOrderDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Contract("_, _, _ -> param2")
    public Optional<InstrumentOrder> insertInstrumentOrder(@NotNull InstrumentOrder instrumentOrder, @NotNull Order order) throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO instrument_order (instrument_id, order_id, price, quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(instrumentOrder.getInstrument().getId()));
            preparedStatement.setString(2, String.valueOf(order.getId()));
            preparedStatement.setString(3, String.valueOf(instrumentOrder.getPrice()));
            preparedStatement.setString(4, String.valueOf(instrumentOrder.getQuantity()));
            preparedStatement.executeUpdate();
            connection.commit();
            return Optional.of(instrumentOrder);
        } catch (SQLException e) {
            connection.rollback();
            logger.log(java.util.logging.Level.WARNING, e.getMessage());
        } finally {
            close();
        }
        return Optional.empty();
    }

    public void deleteAllInstrumentOrdersByOrderId(@NotNull Long orderId) {
        @Language("MySQL") String queryString = "DELETE FROM instrument_order WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(orderId));
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        } finally {
            close();
        }
    }

    @Override
    public List<InstrumentOrder> getInstrumentsForOrder(long id) {
        @Language("MySQL") String queryString =
                "SELECT * FROM instrument_order " +
                        "INNER JOIN instrument_list " +
                        "ON instrument_order.instrument_id = instrument_list.instrument_id " +
                        "WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<InstrumentOrder> instrumentOrders = new ArrayList<>();
            InstrumentMapper instrumentMapper = new InstrumentMapper();
            while (resultSet.next()) {
                Instrument instrument = instrumentMapper.extractFromResultSet(resultSet);
                instrumentOrders.add(new InstrumentOrder(instrument, resultSet.getInt("price"), resultSet.getInt("quantity")));
            }
            return instrumentOrders;
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<InstrumentOrder> create(InstrumentOrder entity) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<InstrumentOrder> findById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(InstrumentOrder entity) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Long id) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
