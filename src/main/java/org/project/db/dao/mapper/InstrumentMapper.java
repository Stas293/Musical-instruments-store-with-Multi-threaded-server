package org.project.db.dao.mapper;

import org.project.db.model.Instrument;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InstrumentMapper implements ObjectMapper<Instrument> {
    @Override
    public Instrument extractFromResultSet(ResultSet rs) throws SQLException {
        return Instrument.builder()
                .setId(rs.getLong("id"))
                .setDateCreated(rs.getTimestamp("date_created"))
                .setDateUpdated(rs.getTimestamp("date_modified"))
                .setDescription(rs.getString("description"))
                .setTitle(rs.getString("title"))
                .setPrice(rs.getDouble("price"))
                .createInstrument();
    }
}
