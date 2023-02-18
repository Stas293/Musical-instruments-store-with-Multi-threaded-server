package org.project.db.server_controller.command_impl;

import org.project.db.model.OrderHistory;
import org.project.db.server_controller.Command;
import org.project.db.service.OrderService;
import org.project.db.service.StatusService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ChangeStatusOfOrder implements Command {
    private static final Logger logger = Logger.getLogger(ChangeStatusOfOrder.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private final StatusService statusService = new StatusService();
    private final OrderService orderService = new OrderService();

    public ChangeStatusOfOrder(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        logger.info("changeStatusOfOrder");
        Long id = Long.parseLong(String.valueOf(inputObjectFromClient.readObject()));
        OrderHistory orderHistory = null;
        try {
            orderHistory = orderService.updateOrderStatus(id,
                    statusService.findNextStatus(statusService.getOrderStatus(id)));
        } catch (SQLException e) {
            logger.warning(e.getMessage());
            outputObjectToClient.writeObject("Error " + e.getMessage());
        }
        outputObjectToClient.writeObject(orderHistory);
    }
}
