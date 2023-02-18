package org.project.db.service;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.InstrumentDao;
import org.project.db.dao.StatusDao;
import org.project.db.dao.impl.DataSource;
import org.project.db.model.Instrument;
import org.project.db.model.Status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class InstrumentService {
    private static final DaoFactory daoFactory = DaoFactory.getInstance();
    private static final Logger logger = Logger.getLogger(InstrumentService.class.getName());

    public InstrumentService() {
    }

    public int getNumberOfInstruments() {
        try (Connection connection = DataSource.getConnection()) {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            return instrumentDao.numberOfInstruments();
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return 0;
    }

    public List<Instrument> getAllInstruments() {
        try (Connection connection = DataSource.getConnection()) {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            List<Instrument> instruments = instrumentDao.getAllInstruments();
            instruments.forEach(
                    instrument -> instrument.setStatus(
                            statusDao.getStatusOfInstrument(instrument.getId()).orElseThrow()));
            return instruments;
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Instrument> getInstrumentsFromBy(int begin, int end) {
        try (Connection connection = DataSource.getConnection()) {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            List<Instrument> instruments = instrumentDao.getInstrumentsFromBy(begin, end);
            instruments.forEach(
                    instrument -> instrument.setStatus(
                            statusDao.getStatusOfInstrument(instrument.getId()).orElseThrow()));
            return instruments;
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    public Instrument getInstrumentById(long id) {
        try (Connection connection = DataSource.getConnection()) {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            Instrument instrument = instrumentDao.findById(id).orElseThrow();
            instrument.setStatus(statusDao.getStatusOfInstrument(id).orElseThrow());
            return instrument;
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public Instrument changeStatusOfInstrument(Instrument instrumentToChangeStatus, Status status) throws SQLException {
        Connection connection = DataSource.getConnection();
        try {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            connection.setAutoCommit(false);
            Instrument instrument = instrumentDao.changeStatusOfInstrument(
                    instrumentToChangeStatus,
                    status).orElseThrow();
            connection.commit();
            return instrument;
        } catch (Exception e) {
            logger.warning(e.getMessage());
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return null;
    }

    public Instrument findByTitle(String title) {
        try (Connection connection = DataSource.getConnection()) {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            Instrument instrument = instrumentDao.findByTitle(title).orElseThrow();
            instrument.setStatus(statusDao.getStatusOfInstrument(instrument.getId()).orElseThrow());
            return instrument;
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public Instrument create(Instrument instrument) throws SQLException {
        Connection connection = DataSource.getConnection();
        try {
            InstrumentDao instrumentDao = daoFactory.createInstrumentDao(connection);
            connection.setAutoCommit(false);
            Instrument instrumentOptional = instrumentDao.create(instrument).orElseThrow();
            connection.commit();
            return instrumentOptional;
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return null;
    }
}
