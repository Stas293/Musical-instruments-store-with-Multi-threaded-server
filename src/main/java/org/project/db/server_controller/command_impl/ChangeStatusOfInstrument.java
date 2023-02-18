package org.project.db.server_controller.command_impl;

import org.project.db.model.Instrument;
import org.project.db.model.Status;
import org.project.db.server_controller.Command;
import org.project.db.service.InstrumentService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ChangeStatusOfInstrument implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private static final Logger logger = Logger.getLogger(ChangeStatusOfInstrument.class.getName());
    private final InstrumentService instrumentService = new InstrumentService();

    public ChangeStatusOfInstrument(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changeStatusOfInstrument");
        Instrument instrumentToChangeStatus = (Instrument) inputObjectFromClient.readObject();
        Status statusToUse = (Status) inputObjectFromClient.readObject();
        try {
            outputObjectToClient.writeObject(instrumentService.changeStatusOfInstrument(instrumentToChangeStatus, statusToUse));
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            outputObjectToClient.writeObject("Error: " + e.getMessage());
        }
    }
}
