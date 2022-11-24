package org.project.db;

import javax.swing.*;

public class DatabaseClient {
    public static void main(String[] args) {
        org.project.db.client.view.DatabaseClient sc = new org.project.db.client.view.DatabaseClient();
        sc.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sc.setVisible(true);
    }
}