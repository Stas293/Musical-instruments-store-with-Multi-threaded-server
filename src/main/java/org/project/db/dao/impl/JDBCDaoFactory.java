package org.project.db.dao.impl;

import org.project.db.dao.*;

public class JDBCDaoFactory extends DaoFactory {
    @Override
    public UserDao createUserDao() {
        return new UserDaoImpl(DataSource.getConnection());
    }

    @Override
    public RoleDao createRoleDao() {
        return new RoleDaoImpl(DataSource.getConnection());
    }

    @Override
    public StatusDao createStatusDao() {
        return new StatusDaoImpl(DataSource.getConnection());
    }

    @Override
    public OrderDao createOrderDao() {
        return new OrderDaoImpl(DataSource.getConnection());
    }

    @Override
    public InstrumentDao createInstrumentDao() {
        return new InstrumentDaoImpl(DataSource.getConnection());
    }

    @Override
    public InstrumentOrderDao createInstrumentOrderDao() {
        return new InstrumentOrderDaoImpl(DataSource.getConnection());
    }

}
