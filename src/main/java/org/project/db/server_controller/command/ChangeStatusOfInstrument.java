package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.InstrumentDao;
import org.project.db.model.Instrument;
import org.project.db.model.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public class ChangeStatusOfInstrument implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public ChangeStatusOfInstrument(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Instrument instrumentToChangeStatus = (Instrument) inputObjectFromClient.readObject();
        Status statusToUse = (Status) inputObjectFromClient.readObject();
        try (InstrumentDao instrumentDao = DaoFactory.getInstance().createInstrumentDao()) {
            outputObjectToClient.writeObject(instrumentDao.changeStatusOfInstrument(instrumentToChangeStatus, statusToUse).orElseThrow());
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
