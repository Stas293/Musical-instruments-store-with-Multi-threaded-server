package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.dao.InstrumentDao;
import org.project.db.dao.mapper.InstrumentMapper;
import org.project.db.model.Instrument;
import org.project.db.model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InstrumentDaoImpl implements InstrumentDao {
    private static final Logger logger = Logger.getLogger(InstrumentDaoImpl.class.getName());
    private final Connection connection;

    public InstrumentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int numberOfInstruments() {
        @Language("MySQL") String queryString = "SELECT COUNT(*) FROM instrument_list";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return 0;
    }

    @Override
    public List<Instrument> getAllInstruments() {
        @Language("MySQL") String queryString = "SELECT * FROM instrument_list";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Instrument> instruments = new ArrayList<>();
            InstrumentMapper instrumentMapper = new InstrumentMapper();
            while (resultSet.next()) {
                Instrument instrument = instrumentMapper.extractFromResultSet(resultSet);
                instruments.add(instrument);
            }
            return instruments;
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Instrument> getInstrumentsFromBy(int begin, int end) {
        @Language("MySQL") String queryString = "SELECT * FROM instrument_list ORDER BY title limit ? offset ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setInt(2, begin);
            preparedStatement.setInt(1, end - begin);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Instrument> instruments = new ArrayList<>();
            InstrumentMapper instrumentMapper = new InstrumentMapper();
            while (resultSet.next()) {
                Instrument instrument = instrumentMapper.extractFromResultSet(resultSet);
                instruments.add(instrument);
            }
            return instruments;
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return Collections.emptyList();
    }

    public Optional<Instrument> findByTitle(@NotNull String title) {
        @Language("MySQL") String queryString = "SELECT * FROM instrument_list WHERE title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new InstrumentMapper().extractFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Instrument> changeStatusOfInstrument(@NotNull Instrument instrument, @NotNull Status status) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE instrument_list SET status_id = ? WHERE instrument_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, status.getId());
            preparedStatement.setLong(2, instrument.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            return findByTitle(instrument.getTitle());
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage());
            connection.rollback();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Instrument> create(Instrument entity) throws SQLException {
        @Language("MySQL") String queryString = "INSERT INTO instrument_list (description, title, status_id, price) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setLong(3, entity.getStatus().getId());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.executeUpdate();
            connection.commit();
            return Optional.of(entity);
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Instrument> findById(Long id) {
        @Language("MySQL") String queryString = "SELECT * FROM instrument_list WHERE instrument_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                InstrumentMapper instrumentMapper = new InstrumentMapper();
                Instrument instrument = instrumentMapper.extractFromResultSet(resultSet);
                return Optional.of(instrument);
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Instrument entity) throws SQLException {
        @Language("MySQL") String queryString = "UPDATE instrument_list SET description = ?, title = ?, status_id = ?, price = ? WHERE instrument_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setLong(3, entity.getStatus().getId());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        @Language("MySQL") String queryString = "DELETE FROM instrument_list WHERE instrument_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warning(e.getMessage());
        }
    }
}

