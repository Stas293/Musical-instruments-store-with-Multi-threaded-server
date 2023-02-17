package org.project.db.server_controller.command;

import org.project.db.dao.InstrumentOrderDao;
import org.project.db.dao.OrderDao;
import org.project.db.dao.StatusDao;
import org.project.db.dao.UserDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.model.Order;
import org.project.db.model.OrderHistory;
import org.project.db.model.Status;
import org.project.db.model.User;
import org.project.db.utility.Mapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeStatusOfOrder implements Command {
    private static final Logger logger = Logger.getLogger(ChangeStatusOfOrder.class.getName());
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public ChangeStatusOfOrder(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Long id = Long.parseLong(String.valueOf(inputObjectFromClient.readObject()));
        try (OrderDao orderDao = JDBCDaoFactory.getInstance().createOrderDao();
             StatusDao statusDao = JDBCDaoFactory.getInstance().createStatusDao();
             InstrumentOrderDao instrumentOrderDao = JDBCDaoFactory.getInstance().createInstrumentOrderDao();
             UserDao userDao = JDBCDaoFactory.getInstance().createUserDao()) {
            Status nextStatus = orderDao.updateOrderStatus(id,
                    statusDao.findNextStatus(orderDao.getOrderStatus(id)));
            logger.log(Level.INFO, nextStatus.getName());
            if (Objects.equals(nextStatus.getName(), "Arrived")) {
                double totalSum = orderDao.getTotalSum(id);
                Order orderToHistory = orderDao.getOrderById(id);
                User user = userDao.findByLogin(orderToHistory.getLogin()).orElseThrow();
                instrumentOrderDao.deleteAllInstrumentOrdersByOrderId(id);
                orderDao.deleteOrder(id);
                OrderHistory orderHistory = new Mapper().mapOrderHistory(orderToHistory, totalSum, user);
                orderDao.insertOrderHistory(orderHistory);
                outputObjectToClient.writeObject(orderHistory);
            }
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
