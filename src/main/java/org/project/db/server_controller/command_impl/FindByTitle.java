package org.project.db.server_controller.command_impl;

import org.project.db.server_controller.Command;
import org.project.db.service.InstrumentService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class FindByTitle implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final InstrumentService instrumentService = new InstrumentService();
    private static final Logger logger = Logger.getLogger(FindByTitle.class.getName());

    public FindByTitle(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("findByTitle");
        String title = (String) inputObjectFromClient.readObject();
        outputObjectToClient.writeObject(instrumentService.findByTitle(title));
    }
}
