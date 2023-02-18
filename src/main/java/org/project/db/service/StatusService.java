package org.project.db.service;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.StatusDao;
import org.project.db.dao.impl.DataSource;
import org.project.db.model.Status;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class StatusService {
    private static final DaoFactory daoFactory = DaoFactory.getInstance();
    private static final Logger logger = Logger.getLogger(StatusService.class.getName());
    public Status getOrderStatus(Long id) {
        try (Connection connection = DataSource.getConnection()) {
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            return statusDao.getOrderStatus(id).orElseThrow();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public Status findNextStatus(Status orderStatus) {
        try (Connection connection = DataSource.getConnection()) {
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            return statusDao.findNextStatus(orderStatus).orElseThrow();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    public List<Status> getAllStatuses() {
        try (Connection connection = DataSource.getConnection()) {
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            return statusDao.getAllStatuses();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return Collections.emptyList();
    }

    public Status getStatusByName(String available) {
        try (Connection connection = DataSource.getConnection()) {
            StatusDao statusDao = daoFactory.createStatusDao(connection);
            return statusDao.getStatusByName(available).orElseThrow();
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }
}
