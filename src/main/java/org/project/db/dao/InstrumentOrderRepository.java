package org.project.db.dao;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InstrumentOrderRepository {
    private static final Object mutex = new Object();
    @Contract("_, _, _ -> param2")
    public static @NotNull InstrumentOrder insertInstrumentOrder(@NotNull Connection connection, @NotNull InstrumentOrder instrumentOrder, @NotNull Order order) throws SQLException {
        synchronized (mutex) {
            @Language("MySQL") String queryString = "INSERT INTO instrument_order (instrument_id, order_id, price, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, String.valueOf(instrumentOrder.getInstrument().getId()));
            preparedStatement.setString(2, String.valueOf(order.getId()));
            preparedStatement.setString(3, String.valueOf(instrumentOrder.getPrice()));
            preparedStatement.setString(4, String.valueOf(instrumentOrder.getQuantity()));
            preparedStatement.executeUpdate();
            connection.commit();
            return instrumentOrder;
        }
    }

    public static void deleteAllInstrumentOrdersByOrderId(@NotNull Connection connection, @NotNull Long orderId) throws SQLException {
        synchronized (mutex) {
            @Language("MySQL") String queryString = "DELETE FROM instrument_order WHERE order_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, String.valueOf(orderId));
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }
}
