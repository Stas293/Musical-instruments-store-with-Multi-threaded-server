package org.project.db.multi_threaded_server;

import org.project.db.dao.*;
import org.project.db.dto.*;
import org.project.db.model.*;
import org.project.db.model.builder.OrderHistoryBuilderImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

class HandleAClient implements Runnable {
    private final Socket socket;
    private static final Logger logger = Logger.getLogger(HandleAClient.class.getName());

    /**
     * Construct a thread
     */
    public HandleAClient(Socket socket) {
        this.socket = socket;
    }

    /**
     * Run a thread
     */
    public void run() {
        try {
            ObjectOutputStream outputObjectToClient = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputObjectFromClient = new ObjectInputStream(socket.getInputStream());
            while (true) {

                try {
                    String command = (String) inputObjectFromClient.readObject();
                    CommandAction commandAction = CommandAction.valueOf(command);
                    Command commandObject = ActionFactory.getCommand(commandAction, inputObjectFromClient, outputObjectToClient);
                    commandObject.execute();
                    switch (command) {
                        case "login":

                            break;
                        case "allUserDtos":
                            outputObjectToClient.writeObject(new UserRepository().getAllUsers(multiThreadServer.connection));
                            break;
                        case "reallyDisableUser":
                            Object object = inputObjectFromClient.readObject();
                            if (object instanceof UserDto) {
                                new UserRepository().disableUser(multiThreadServer.connection, (UserDto) object);
                                outputObjectToClient.writeObject("Successfully disabled user");
                            } else {
                                outputObjectToClient.writeObject("Wrong object");
                            }
                            break;
                        case "reallyEnableUser":
                            Object object1 = inputObjectFromClient.readObject();
                            if (object1 instanceof UserDto) {
                                new UserRepository().enableUser(multiThreadServer.connection, (UserDto) object1);
                                outputObjectToClient.writeObject("Successfully enabled user");
                            } else {
                                outputObjectToClient.writeObject("Wrong object");
                            }
                            break;
                        case "addRoleForUser":
                            System.out.println("addRoleForUser");
                            Object object2 = inputObjectFromClient.readObject();
                            ArrayList<Role> allRoles = new RoleRepository().getAllRoles(multiThreadServer.connection);
                            ArrayList<Role> haveRoles = new RoleRepository().getRolesForUser(multiThreadServer.connection, (UserDto) object2);
                            System.out.println(allRoles);
                            System.out.println(haveRoles);
                            allRoles.removeAll(haveRoles);
                            outputObjectToClient.writeObject(allRoles);
                            break;
                        case "RoleForUser":
                            Object object3 = inputObjectFromClient.readObject();
                            UserRoleDto userRoleDto = (UserRoleDto) object3;
                            new RoleRepository().insertRoleForUser(multiThreadServer.connection, new UserDto(userRoleDto.getLogin()), userRoleDto.getRoleName());
                            outputObjectToClient.writeObject(new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(userRoleDto.getLogin())));
                            break;
                        case "changeEmail":
                            UserDto userDto = (UserDto) inputObjectFromClient.readObject();
                            String email = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new UserRepository().changeEmail(multiThreadServer.connection, userDto, email));
                            break;
                        case "changeFirstName":
                            UserDto userDto1 = (UserDto) inputObjectFromClient.readObject();
                            String firstName = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new UserRepository().changeFirstName(multiThreadServer.connection, userDto1, firstName));
                            break;
                        case "changeLastName":
                            UserDto userDto2 = (UserDto) inputObjectFromClient.readObject();
                            String lastName = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new UserRepository().changeLastName(multiThreadServer.connection, userDto2, lastName));
                            break;
                        case "changePhone":
                            UserDto userDto3 = (UserDto) inputObjectFromClient.readObject();
                            String phone = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new UserRepository().changePhone(multiThreadServer.connection, userDto3, phone));
                            break;
                        case "changePassword":
                            UserDto userDto4 = (UserDto) inputObjectFromClient.readObject();
                            String password = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new UserRepository().changePassword(multiThreadServer.connection, userDto4, password));
                            break;
                        case "getStatuses":
                            outputObjectToClient.writeObject(new StatusRepository().getAllStatuses(multiThreadServer.connection));
                            break;
                        case "getNumberOfInstruments":
                            outputObjectToClient.writeObject(InstrumentRepository.numberOfInstruments(multiThreadServer.connection));
                            break;
                        case "addInstrument":
                            Instrument instrument = (Instrument) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(InstrumentRepository.insertInstrument(multiThreadServer.connection, instrument));
                            break;
                        case "getAllInstruments":
                            outputObjectToClient.writeObject(InstrumentRepository.getAllInstruments(multiThreadServer.connection));
                            break;
                        case "getInstrumentByTitle":
                            String title = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(InstrumentRepository.getInstrumentByTitle(multiThreadServer.connection, title));
                            break;
                        case "makeOrder":
                            Status nextStatus = new StatusRepository().findNextStatus(multiThreadServer.connection, new StatusRepository().getStatusByName(multiThreadServer.connection, "Available"));
                            outputObjectToClient.writeObject(nextStatus);
                            OrderDto orderDto = (OrderDto) inputObjectFromClient.readObject();
                            ArrayList<InstrumentOrder> instrumentOrders = (ArrayList<InstrumentOrder>) inputObjectFromClient.readObject();
                            Order order = new OrderRepository().insertOrder(multiThreadServer.connection, orderDto);
                            for (InstrumentOrder instrumentOrder : instrumentOrders) {
                                InstrumentOrderRepository.insertInstrumentOrder(multiThreadServer.connection, instrumentOrder, order);
                            }
                            outputObjectToClient.writeObject(order);
                            break;
                        case "getAllOrdersForUser":
                            Object object8 = inputObjectFromClient.readObject();
                            System.out.println(object8);
                            UserDto userDto5 = (UserDto) object8;
                            outputObjectToClient.writeObject(new OrderRepository().getAllOrders(multiThreadServer.connection, userDto5));
                            break;
                        case "getAllHistoryOrdersForUser":
                            UserDto userDto6 = (UserDto) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new OrderRepository().getUserOrderHistory(multiThreadServer.connection, userDto6));
                            break;
                        case "getOrderByUser":
                            UserDto userDto7 = (UserDto) inputObjectFromClient.readObject();
                            ArrayList<Order> orders = new OrderRepository().getAllOrders(multiThreadServer.connection, userDto7);
                            outputObjectToClient.writeObject(orders);
                            break;
                        case "changeStatusOfOrder":
                            Long id = (long) Integer.parseInt(String.valueOf(inputObjectFromClient.readObject()));
                            Status nextStatuss = new OrderRepository().updateOrderStatus(multiThreadServer.connection, id, new StatusRepository().findNextStatus(multiThreadServer.connection, new OrderRepository().getOrderStatus(multiThreadServer.connection, id)));
                            System.out.println(nextStatuss);
                            if (Objects.equals(nextStatuss.getName(), "Arrived")) {
                                double totalSum = new OrderRepository().getTotalSum(multiThreadServer.connection, id);
                                Order orderToHistory = new OrderRepository().getOrderById(multiThreadServer.connection, id);
                                InstrumentOrderRepository.deleteAllInstrumentOrdersByOrderId(multiThreadServer.connection, id);
                                new OrderRepository().deleteOrder(multiThreadServer.connection, id);
                                System.out.println(new OrderRepository().insertOrderHistory(multiThreadServer.connection, new OrderHistoryBuilderImpl().setUser(new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(orderToHistory.getLogin()))).setTotalSum(totalSum).setTitle(orderToHistory.getTitle()).setStatus(orderToHistory.getStatus()).createOrderHistory()));
                            }
                            break;
                        case "changeStatusOfInstrument":
                            Instrument instrumentToChangeStatus = (Instrument) inputObjectFromClient.readObject();
                            Status statusToUse = (Status) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(InstrumentRepository.changeStatusOfInstrument(multiThreadServer.connection, instrumentToChangeStatus, statusToUse));
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + command);
                    }
                    System.out.println(command);
                } catch (SQLException e) {
                    multiThreadServer.connection.rollback();
                    logger.log(Level.WARNING, "Error in transaction", e);
                }
            }
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Error in transaction", ex);
        }
    }
}
