package org.project.db.dao.mapper;

import org.project.db.model.InstrumentOrder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InstrumentOrderMapper implements ObjectMapper<InstrumentOrder> {
    @Override
    public InstrumentOrder extractFromResultSet(ResultSet rs) throws SQLException {
        return InstrumentOrder.builder()
                .setPrice(rs.getDouble("price"))
                .setQuantity(rs.getInt("quantity"))
                .createInstrumentOrder();
    }
}
