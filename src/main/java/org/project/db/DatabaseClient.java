package org.project.db;

import javax.swing.*;

public class DatabaseClient {
    public static void main(String[] args) {
        org.project.db.Client.DatabaseClient sc = new org.project.db.Client.DatabaseClient();
        sc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sc.setVisible(true);
    }
}