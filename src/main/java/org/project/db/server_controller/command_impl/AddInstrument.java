package org.project.db.server_controller.command_impl;

import org.project.db.model.Instrument;
import org.project.db.server_controller.Command;
import org.project.db.service.InstrumentService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AddInstrument implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final InstrumentService instrumentService = new InstrumentService();
    private static final Logger logger = Logger.getLogger(AddInstrument.class.getName());

    public AddInstrument(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("addInstrument");
        Instrument instrument = (Instrument) inputObjectFromClient.readObject();
        try {
            outputObjectToClient.writeObject(instrumentService.create(instrument));
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            outputObjectToClient.writeObject("Error: " + e.getMessage());
        }
    }
}
