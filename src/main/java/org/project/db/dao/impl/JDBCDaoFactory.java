package org.project.db.dao.impl;

import org.project.db.dao.*;

import java.sql.Connection;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public UserDao createUserDao(Connection connection) {
        return new UserDaoImpl(connection);
    }

    @Override
    public RoleDao createRoleDao(Connection connection) {
        return new RoleDaoImpl(connection);
    }

    @Override
    public StatusDao createStatusDao(Connection connection) {
        return new StatusDaoImpl(connection);
    }

    @Override
    public OrderDao createOrderDao(Connection connection) {
        return new OrderDaoImpl(connection);
    }

    @Override
    public InstrumentDao createInstrumentDao(Connection connection) {
        return new InstrumentDaoImpl(connection);
    }

    @Override
    public InstrumentOrderDao createInstrumentOrderDao(Connection connection) {
        return new InstrumentOrderDaoImpl(connection);
    }

}
