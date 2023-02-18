package org.project.db.dao;

import org.jetbrains.annotations.NotNull;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;

import java.sql.SQLException;
import java.util.List;

public interface InstrumentOrderDao extends GenericDao<InstrumentOrder> {

    void insertInstrumentOrder(@NotNull List<InstrumentOrder> instrumentOrder, @NotNull Order order) throws SQLException;

    void deleteAllInstrumentOrdersByOrderId(Long id);

    List<InstrumentOrder> getInstrumentsForOrder(long id);
}
