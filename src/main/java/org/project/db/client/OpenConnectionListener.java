package org.project.db.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

class OpenConnectionListener implements ActionListener {
    private final DatabaseClient databaseClient;

    private static final Logger logger = Logger.getLogger(OpenConnectionListener.class.getName());

    public OpenConnectionListener(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            databaseClient.socket = SocketChannel.open(new InetSocketAddress("localhost", 8000));
            databaseClient.fromServer = new ObjectInputStream(Channels.newInputStream(databaseClient.socket));
            databaseClient.toServer = new ObjectOutputStream(Channels.newOutputStream(databaseClient.socket));
        } catch (IOException e1) {
            logger.log(Level.WARNING, "Error while opening connection", e1);
        }
    }
}
