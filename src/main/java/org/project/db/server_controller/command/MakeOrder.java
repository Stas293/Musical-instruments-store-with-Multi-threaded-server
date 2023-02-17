package org.project.db.server_controller.command;

import org.project.db.dao.DaoFactory;
import org.project.db.dao.InstrumentOrderDao;
import org.project.db.dao.OrderDao;
import org.project.db.dao.StatusDao;
import org.project.db.dto.OrderDto;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;
import org.project.db.model.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

public class MakeOrder implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public MakeOrder(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        try (StatusDao statusDao = DaoFactory.getInstance().createStatusDao();
             OrderDao orderDao = DaoFactory.getInstance().createOrderDao();
             InstrumentOrderDao instrumentOrderDao = DaoFactory.getInstance().createInstrumentOrderDao()) {
            Status nextStatus = statusDao.findNextStatus(statusDao.getStatusByName("Available").orElseThrow());
            outputObjectToClient.writeObject(nextStatus);
            OrderDto orderDto = (OrderDto) inputObjectFromClient.readObject();
            List<InstrumentOrder> instrumentOrders = (List<InstrumentOrder>) inputObjectFromClient.readObject();
            Order order = orderDao.insertOrder(orderDto);
            for (InstrumentOrder instrumentOrder : instrumentOrders) {
                instrumentOrderDao.insertInstrumentOrder(instrumentOrder, order);
            }
            outputObjectToClient.writeObject(order);
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
