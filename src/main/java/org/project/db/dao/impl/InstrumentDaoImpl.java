package org.project.db.dao.impl;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.project.db.model.Instrument;
import org.project.db.model.builder.InstrumentBuilderImpl;
import org.project.db.model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InstrumentDaoImpl {

    public static int numberOfInstruments(@NotNull Connection connection)
            throws SQLException {
            @Language("MySQL") String queryString = "SELECT COUNT(*) FROM instrument_list";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Integer.parseInt(resultSet.getString(1));
    }

    public static ArrayList<Instrument> getAllInstruments(@NotNull Connection connection)
            throws SQLException {
            @Language("MySQL") String queryString = "SELECT * FROM instrument_list";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Instrument> instruments = new ArrayList<>();
            Status status;
            StatusDaoImpl statusRepository = new StatusDaoImpl();
            while (resultSet.next()) {
                status = statusRepository.getStatusById(connection, Long.parseLong(resultSet.getString("status_id")));
                instruments.add(new InstrumentBuilderImpl().setId(Long.parseLong(resultSet.getString("instrument_id"))).setDateCreated(resultSet.getTimestamp("date_created")).setDateUpdated(resultSet.getTimestamp("date_updated")).setDescription(resultSet.getString("description")).setTitle(resultSet.getString("title")).setStatus(status).setPrice(Double.parseDouble(resultSet.getString("price"))).createInstrument());
            }
            return instruments;
    }

    public static ArrayList<Instrument> getInstrumentsFromBy(@NotNull Connection connection, int begin, int end)
            throws SQLException {
            @Language("MySQL") String queryString = "SELECT * FROM instrument_list limit ? offset ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(2, begin);
            preparedStatement.setInt(1, end - begin);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Instrument> instruments = new ArrayList<>();
            Status status;
            while (resultSet.next()) {
                status = new StatusDaoImpl().getStatusById(connection, Long.parseLong(resultSet.getString("status_id")));
                instruments.add(new InstrumentBuilderImpl().setId(Long.parseLong(resultSet.getString("instrument_id"))).setDateCreated(resultSet.getTimestamp("date_created")).setDateUpdated(resultSet.getTimestamp("date_updated")).setDescription(resultSet.getString("description")).setTitle(resultSet.getString("title")).setStatus(status).setPrice(Double.parseDouble(resultSet.getString("price"))).createInstrument());
            }
            return instruments;
    }

    public static Instrument getInstrumentByTitle(@NotNull Connection connection, @NotNull String title)
            throws SQLException {
            @Language("MySQL") String queryString = "SELECT * FROM instrument_list WHERE title = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean found = resultSet.next();
            if (!found) {
                return null;
            }
            Status status = new StatusDaoImpl().getStatusById(connection, Long.parseLong(resultSet.getString("status_id")));
            return new InstrumentBuilderImpl().setId(Long.parseLong(resultSet.getString("instrument_id"))).setDateCreated(resultSet.getTimestamp("date_created")).setDateUpdated(resultSet.getTimestamp("date_updated")).setDescription(resultSet.getString("description")).setTitle(resultSet.getString("title")).setStatus(status).setPrice(Double.parseDouble(resultSet.getString("price"))).createInstrument();
    }

    public static Instrument insertInstrument(@NotNull Connection connection, @NotNull Instrument instrument) throws SQLException {
            try {
                @Language("MySQL") String queryString = "INSERT INTO instrument_list (description, title, status_id, price) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setString(1, instrument.getDescription());
                preparedStatement.setString(2, instrument.getTitle());
                preparedStatement.setLong(3, instrument.getStatus().getId());
                preparedStatement.setDouble(4, instrument.getPrice());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
            return getInstrumentByTitle(connection, instrument.getTitle());
    }

    public static Instrument changeStatusOfInstrument(@NotNull Connection connection, @NotNull Instrument instrument, @NotNull Status status)
            throws SQLException {
            @Language("MySQL") String queryString = "UPDATE instrument_list SET status_id = ? WHERE instrument_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setLong(1, status.getId());
            preparedStatement.setLong(2, instrument.getId());
            preparedStatement.executeUpdate();
            connection.commit();
            return getInstrumentByTitle(connection, instrument.getTitle());
    }
}

