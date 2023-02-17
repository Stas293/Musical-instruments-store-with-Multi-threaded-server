package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.InstrumentDao;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class GetNumberOfInstruments implements Command {
    private final ObjectOutputStream outputObjectToClient;
    public GetNumberOfInstruments(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        try (InstrumentDao instrumentDao = DaoFactory.getInstance().createInstrumentDao()) {
            outputObjectToClient.writeObject(instrumentDao.numberOfInstruments());
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
