package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.StatusDao;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class GetStatuses implements Command {

    private final ObjectOutputStream outputObjectToClient;
    public GetStatuses(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        try (StatusDao statusDao = DaoFactory.getInstance().createStatusDao()) {
            outputObjectToClient.writeObject(statusDao.getAllStatuses());
        } catch (Exception e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
