package org.project.db.dao.mapper;

import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.model.InstrumentOrder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InstrumentOrderMapper implements ObjectMapper<InstrumentOrder> {
    @Override
    public InstrumentOrder extractFromResultSet(ResultSet rs) throws SQLException {
        return InstrumentOrder.builder()
                .setInstrument(JDBCDaoFactory.getInstance().createInstrumentDao().findById(rs.getLong("instrument_id")).get())
                .setPrice(rs.getDouble("price"))
                .setQuantity(rs.getInt("quantity"))
                .createInstrumentOrder();
    }
}
