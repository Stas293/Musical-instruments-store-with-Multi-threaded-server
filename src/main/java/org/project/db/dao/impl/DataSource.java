package org.project.db.dao.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl( "jdbc:mysql://localhost:3306/" + System.getenv("DB_SCHEMA"));
        config.setUsername( System.getenv("MYSQL_USERNAME") );
        config.setPassword( System.getenv("MYSQL_PASSWORD") );
        ds = new HikariDataSource( config );
    }

    private DataSource() {}

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
