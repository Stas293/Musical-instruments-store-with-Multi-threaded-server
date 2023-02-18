package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.server_controller.Command;
import org.project.db.service.OrderService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetAllOrdersForUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private static final Logger logger = Logger.getLogger(GetAllOrdersForUser.class.getName());
    private static final OrderService orderService = new OrderService();
    public GetAllOrdersForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("getAllOrdersForUser");
        Object object8 = inputObjectFromClient.readObject();
        logger.log(Level.INFO, object8.toString());
        UserDto userDto = (UserDto) object8;
        outputObjectToClient.writeObject(orderService.getAllOrders(userDto));
    }
}
