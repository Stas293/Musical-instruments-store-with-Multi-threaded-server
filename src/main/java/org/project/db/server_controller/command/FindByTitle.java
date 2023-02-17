package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.InstrumentDao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FindByTitle implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public FindByTitle(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        String title = (String) inputObjectFromClient.readObject();
        try (InstrumentDao instrumentDao = DaoFactory.getInstance().createInstrumentDao()) {
            outputObjectToClient.writeObject(instrumentDao.findByTitle(title));
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
