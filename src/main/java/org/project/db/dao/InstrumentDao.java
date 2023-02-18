package org.project.db.dao;

import org.project.db.model.Instrument;
import org.project.db.model.Status;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InstrumentDao extends GenericDao<Instrument> {
    int numberOfInstruments();

    List<Instrument> getAllInstruments();

    List<Instrument> getInstrumentsFromBy(int begin, int end);

    Optional<Instrument> findByTitle(String title);

    Optional<Instrument> changeStatusOfInstrument(Instrument instrumentToChangeStatus, Status statusToUse) throws SQLException;
}
