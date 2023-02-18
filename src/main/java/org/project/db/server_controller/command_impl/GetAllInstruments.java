package org.project.db.server_controller.command_impl;

import org.project.db.server_controller.Command;
import org.project.db.service.InstrumentService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class GetAllInstruments implements Command {
    private final ObjectOutputStream outputObjectToClient;
    private final InstrumentService instrumentService = new InstrumentService();
    private static final Logger logger = Logger.getLogger(GetAllInstruments.class.getName());

    public GetAllInstruments(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("getAllInstruments");
        outputObjectToClient.writeObject(instrumentService.getAllInstruments());
    }
}
