package org.project.db.server_controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class HandleAClient implements Runnable {
    private static final Logger logger = Logger.getLogger(HandleAClient.class.getName());
    private final Socket socket;

    /**
     * Construct a thread
     */
    public HandleAClient(Socket socket) {
        this.socket = socket;
    }

    /**
     * Run a thread
     */
    public void run() {
        try {
            ObjectOutputStream outputObjectToClient = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputObjectFromClient = new ObjectInputStream(socket.getInputStream());
            while (true) {
                String command = (String) inputObjectFromClient.readObject();
                try {
                    logger.log(Level.INFO, command);
                    Command commandObject =
                            ActionFactory.getCommand(command, inputObjectFromClient, outputObjectToClient);
                    commandObject.execute();
                } catch (IllegalArgumentException e) {
                    outputObjectToClient.writeObject("Error");
                }
                logger.log(Level.INFO, command);
            }
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Error in transaction", ex);
        }
    }
}
