package org.project.db.dao;

import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InstrumentOrderDao extends GenericDao<InstrumentOrder> {
    Optional<InstrumentOrder> insertInstrumentOrder(InstrumentOrder instrumentOrder, Order order) throws SQLException;

    void deleteAllInstrumentOrdersByOrderId(Long id);

    List<InstrumentOrder> getInstrumentsForOrder(long id);
}
