package org.project.db.server_controller.command;

import org.project.db.dao.InstrumentDao;
import org.project.db.dao.impl.JDBCDaoFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class GetAllInstruments implements Command {
    private final ObjectOutputStream outputObjectToClient;
    public GetAllInstruments(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        try (InstrumentDao instrumentDao = JDBCDaoFactory.getInstance().createInstrumentDao()) {
            outputObjectToClient.writeObject(instrumentDao.getAllInstruments());
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
