package org.project.db.server_controller.command_impl;

import org.project.db.dto.OrderDto;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;
import org.project.db.model.Status;
import org.project.db.server_controller.Command;
import org.project.db.service.OrderService;
import org.project.db.service.StatusService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class MakeOrder implements Command {
    private static final Logger logger = Logger.getLogger(MakeOrder.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final StatusService statusService = new StatusService();
    private final OrderService orderService = new OrderService();

    public MakeOrder(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("makeOrder");
        Status nextStatus = statusService.findNextStatus(statusService.getStatusByName("Available"));
        outputObjectToClient.writeObject(nextStatus);
        OrderDto orderDto = (OrderDto) inputObjectFromClient.readObject();
        List<InstrumentOrder> instrumentOrders = (List<InstrumentOrder>) inputObjectFromClient.readObject();
        Order order = null;
        try {
            order = orderService.makeOrder(orderDto, instrumentOrders);
        } catch (SQLException e) {
            logger.info("SQLException: " + e.getMessage());
            outputObjectToClient.writeObject("Error: " + e.getMessage());
        }
        outputObjectToClient.writeObject(order);
    }
}
