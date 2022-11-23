package org.project.db.dao;

public abstract class DaoFactory {
    private static volatile DaoFactory daoFactory;

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    DaoFactory temp = new JDBCDaoFactory();
                    daoFactory = temp;
                }
            }
        }
        return daoFactory;
    }

    public abstract UserDao createUserDao();

    public abstract RoleDao createRoleDao();

    public abstract StatusDao createStatusDao();

    public abstract RequestDao createRequestDao();

    public abstract HistoryRequestDao createHistoryRequestDao();
}
