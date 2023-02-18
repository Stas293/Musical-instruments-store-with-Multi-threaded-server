package org.project.db.server_controller.command_impl;

import org.project.db.server_controller.Command;
import org.project.db.service.StatusService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class GetStatuses implements Command {
    private static final Logger logger = Logger.getLogger(GetStatuses.class.getName());
    private final StatusService statusService = new StatusService();
    private final ObjectOutputStream outputObjectToClient;
    public GetStatuses(ObjectOutputStream outputObjectToClient) {
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("getStatuses");
        outputObjectToClient.writeObject(statusService.getAllStatuses());
    }
}
