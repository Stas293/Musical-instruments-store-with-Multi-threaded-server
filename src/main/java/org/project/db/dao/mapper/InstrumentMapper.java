package org.project.db.dao.mapper;

import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dao.impl.RoleDaoImpl;
import org.project.db.model.Instrument;
import org.project.db.model.builder.InstrumentBuilderImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InstrumentMapper implements ObjectMapper<Instrument> {
    @Override
    public Instrument extractFromResultSet(ResultSet rs) throws SQLException {
        return new InstrumentBuilderImpl()
                .setId(rs.getLong("id"))
                .setDateCreated(rs.getTimestamp("date_created"))
                .setDateUpdated(rs.getTimestamp("date_modified"))
                .setDescription(rs.getString("description"))
                .setStatus(JDBCDaoFactory.getInstance().createStatusDao().findById(rs.getLong("status_id")).get())
                .setTitle(rs.getString("title"))
                .setPrice(rs.getDouble("price"))
                .createInstrument();
    }
}
}
