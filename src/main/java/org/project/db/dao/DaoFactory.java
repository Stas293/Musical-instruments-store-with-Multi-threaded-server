package org.project.db.dao;

import org.project.db.dao.impl.JDBCDaoFactory;

import java.sql.Connection;

public abstract class DaoFactory {
    private static volatile DaoFactory daoFactory;

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    daoFactory = new JDBCDaoFactory();
                }
            }
        }
        return daoFactory;
    }

    public abstract UserDao createUserDao(Connection connection);

    public abstract RoleDao createRoleDao(Connection connection);

    public abstract StatusDao createStatusDao(Connection connection);

    public abstract OrderDao createOrderDao(Connection connection);

    public abstract InstrumentDao createInstrumentDao(Connection connection);

    public abstract InstrumentOrderDao createInstrumentOrderDao(Connection connection);
}
