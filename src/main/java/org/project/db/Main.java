package org.project.db;

import org.project.db.Dto.UserDto;
import org.project.db.Repository.OrderRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
                        System.getenv("DB_SCHEMA"), System.getenv("MYSQL_USERNAME"),
                System.getenv("MYSQL_PASSWORD"));
        try {
            connection.setAutoCommit(false);
            System.out.println("Connected to database");
            System.out.println(new OrderRepository().getAllOrders(connection, new UserDto("Stas230")));
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}