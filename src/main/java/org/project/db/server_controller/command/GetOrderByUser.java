package org.project.db.server_controller.command;

import org.project.db.dao.OrderDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;
import org.project.db.model.Order;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.List;

public class GetOrderByUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    public GetOrderByUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto7 = (UserDto) inputObjectFromClient.readObject();
        try (OrderDao orderDao = JDBCDaoFactory.getInstance().createOrderDao()) {
            List<Order> orders = orderDao.getAllOrders(userDto7);
            outputObjectToClient.writeObject(orders);
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
