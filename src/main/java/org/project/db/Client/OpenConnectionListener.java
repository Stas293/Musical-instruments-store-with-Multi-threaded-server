package org.project.db.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class OpenConnectionListener implements ActionListener {
    private final DatabaseClient databaseClient;

    public OpenConnectionListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.socket = new Socket("localhost", 8000);
            databaseClient.fromServer = new ObjectInputStream(databaseClient.socket.getInputStream());
            databaseClient.toServer = new ObjectOutputStream(databaseClient.socket.getOutputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
