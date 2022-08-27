package org.project.db;

import javax.swing.*;
import java.sql.SQLException;

public class MultiThreadServer {
    public static void main(String[] args) throws SQLException {
        org.project.db.MultiThreadedServer.MultiThreadServer mts = new org.project.db.MultiThreadedServer.MultiThreadServer();
        mts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mts.setVisible(true);
    }
}
