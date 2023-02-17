package org.project.db;

import javax.swing.*;

public class MultiThreadServer {
    public static void main(String[] args) {
        org.project.db.server_controller.MultiThreadServer mts = new org.project.db.server_controller.MultiThreadServer();
        mts.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mts.setVisible(true);
    }
}
