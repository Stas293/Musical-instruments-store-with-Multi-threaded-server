package org.project.db.server_controller.command;

import org.project.db.dao.OrderDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.logging.Logger;

public class GetAllHistoryOrdersForUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;

    public GetAllHistoryOrdersForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        UserDto userDto6 = (UserDto) inputObjectFromClient.readObject();
        try (OrderDao orderDao = JDBCDaoFactory.getInstance().createOrderDao()) {
            outputObjectToClient.writeObject(orderDao.getUserOrderHistory(userDto6));
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
