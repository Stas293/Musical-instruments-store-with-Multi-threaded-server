package org.project.db.server_controller.command;

import org.project.db.dao.OrderDao;
import org.project.db.dao.impl.JDBCDaoFactory;
import org.project.db.dto.UserDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetAllOrdersForUser implements Command {
    private final ObjectInputStream inputObjectFromClient;
    private final ObjectOutputStream outputObjectToClient;
    private static final Logger logger = Logger.getLogger(GetAllOrdersForUser.class.getName());
    public GetAllOrdersForUser(ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        this.inputObjectFromClient = inputObjectFromClient;
        this.outputObjectToClient = outputObjectToClient;
    }

    @Override
    public void execute() throws IOException, ClassNotFoundException {
        Object object8 = inputObjectFromClient.readObject();
        logger.log(Level.INFO, object8.toString());
        UserDto userDto5 = (UserDto) object8;
        try (OrderDao orderDao = JDBCDaoFactory.getInstance().createOrderDao()) {
            outputObjectToClient.writeObject(orderDao.getAllOrders(userDto5));
        } catch (SQLException e) {
            outputObjectToClient.writeObject("Error");
        }
    }
}
