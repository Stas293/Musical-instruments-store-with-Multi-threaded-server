package org.project.db.Repository;

import org.jetbrains.annotations.NotNull;
import org.project.db.Model.InstrumentOrder;
import org.project.db.Model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InstrumentOrderRepository {
    public InstrumentOrder insertInstrumentOrder(@NotNull Connection connection, InstrumentOrder instrumentOrder, Order order) throws SQLException {
        String queryString = "INSERT INTO instrument_order (instrument_id, order_id, price, quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(instrumentOrder.getInstrument().getId()));
        preparedStatement.setString(2, String.valueOf(order.getId()));
        preparedStatement.setString(3, String.valueOf(instrumentOrder.getPrice()));
        preparedStatement.setString(4, String.valueOf(instrumentOrder.getQuantity()));
        preparedStatement.executeUpdate();
        connection.commit();
        return instrumentOrder;
    }

    public void deleteAllInstrumentOrdersByOrderId(@NotNull Connection connection, @NotNull Long orderId) throws SQLException {
        String queryString = "DELETE FROM instrument_order WHERE order_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(queryString);
        preparedStatement.setString(1, String.valueOf(orderId));
        preparedStatement.executeUpdate();
        connection.commit();
    }
}
