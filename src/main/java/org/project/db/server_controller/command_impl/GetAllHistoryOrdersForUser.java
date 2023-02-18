package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.OrderService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

public class GetAllHistoryOrdersForUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final OrderService orderService = new OrderService();
    private static final Logger logger = Logger.getLogger(GetAllHistoryOrdersForUser.class.getName());

    public GetAllHistoryOrdersForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("getAllHistoryOrdersForUser");
        UserDto userDto = (UserDto) inputObjectFromClient.readObject();
        outputObjectToClient.writeObject(orderService.getUserOrderHistory(userDto));
    }
}
