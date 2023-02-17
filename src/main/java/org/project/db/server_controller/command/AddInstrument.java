package org.project.db.server_controller.command;

import org.project.db.dao.InstrumentDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.model.Instrument;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddInstrument implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public AddInstrument(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Instrument instrument = (Instrument) inputObjectFromClient.readObject();
        try (InstrumentDao instrumentDao = JDBCDaoFactory.getInstance().createInstrumentDao()) {
            outputObjectToClient.writeObject(instrumentDao.create(instrument).orElse(null));
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
