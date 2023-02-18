package org.project.db.server_controller.command_impl;

import org.project.db.server_controller.Command;
import org.project.db.service.InstrumentService;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class GetNumberOfInstruments implements Command {
    private final ObjectOutputStream outputObjectToClient;
    private final InstrumentService instrumentService = new InstrumentService();

    public GetNumberOfInstruments(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        outputObjectToClient.writeObject(instrumentService.getNumberOfInstruments());
    }
}
