package org.project.db;

import javax.swing.*;
import java.sql.SQLException;

public class MultiThreadServer {
    public static void main(String[] args) throws SQLException {
        org.project.db.multi_threaded_server.MultiThreadServer mts = new org.project.db.multi_threaded_server.MultiThreadServer();
        mts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mts.setVisible(true);
    }
}
