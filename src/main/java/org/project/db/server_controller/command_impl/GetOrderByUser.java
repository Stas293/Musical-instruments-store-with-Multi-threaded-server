package org.project.db.server_controller.command_impl;

import org.project.db.dto.UserDto;
import org.project.db.model.Order;
import org.project.db.server_controller.Command;
import org.project.db.service.OrderService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Logger;

public class GetOrderByUser implements Command {
    private static final Logger logger = Logger.getLogger(GetOrderByUser.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final OrderService orderService = new OrderService();

    public GetOrderByUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("getOrderByUser");
        UserDto userDto7 = (UserDto) inputObjectFromClient.readObject();
        List<Order> orders = orderService.getAllOrders(userDto7);
        outputObjectToClient.writeObject(orders);
    }
}
