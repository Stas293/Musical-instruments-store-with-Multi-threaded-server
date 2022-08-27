package org.project.db.MultiThreadedServer;

import org.project.db.Dto.*;
import org.project.db.Model.*;
import org.project.db.Repository.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

// Define the thread class for handling new connection
class HandleAClient implements Runnable {
    private final MultiThreadServer multiThreadServer;
    private final Socket socket;
    private final int clientNum;
    private ObjectOutputStream outputObjectToClient;
    private ObjectInputStream inputObjectFromClient;


    /**
     * Construct a thread
     */
    public HandleAClient(MultiThreadServer multiThreadServer, Socket socket, int clientNum) throws IOException {
        this.multiThreadServer = multiThreadServer;
        this.socket = socket;
        this.clientNum = clientNum;
    }

    /**
     * Run a thread
     */
    public void run() {
        try {
            outputObjectToClient = new ObjectOutputStream(socket.getOutputStream());
            inputObjectFromClient = new ObjectInputStream(socket.getInputStream());
            while (true) {

                try {
                    String command = (String) inputObjectFromClient.readObject();
                    switch (command) {
                        case "register": {
                            RegistrationDto s = (RegistrationDto) inputObjectFromClient.readObject();
                            User user = new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(s.getLogin()));
                            if (user == null) {
                                new UserRepository().insertUser(multiThreadServer.connection, s);
                                multiThreadServer.connection.commit();
                                outputObjectToClient.writeObject(new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(s.getLogin())));
                            } else {
                                outputObjectToClient.writeObject("User already exists");
                            }
                            break;
                        }
                        case "login": {
                            LoginDto s = (LoginDto) inputObjectFromClient.readObject();
                            User user = new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(s.getLogin()));
                            if (user != null) {
                                if (!user.isEnabled()) {
                                    outputObjectToClient.writeObject("User is disabled");
                                } else if (user.getPassword().equals(s.getPassword())) {
                                    outputObjectToClient.writeObject(user);
                                } else {
                                    outputObjectToClient.writeObject("Wrong password");
                                }
                            } else {
                                outputObjectToClient.writeObject("User not found");
                            }
                            break;
                        }
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
                            outputObjectToClient.writeObject(new InstrumentRepository().numberOfInstruments(multiThreadServer.connection));
                            break;
                        case "addInstrument":
                            Instrument instrument = (Instrument) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new InstrumentRepository().insertInstrument(multiThreadServer.connection, instrument));
                            break;
                        case "getAllInstruments":
                            outputObjectToClient.writeObject(new InstrumentRepository().getAllInstruments(multiThreadServer.connection));
                            break;
                        case "getInstrumentByTitle":
                            String title = (String) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new InstrumentRepository().getInstrumentByTitle(multiThreadServer.connection, title));
                            break;
                        case "makeOrder":
                            Status nextStatus = new StatusRepository().findNextStatus(multiThreadServer.connection, new StatusRepository().getStatusByName(multiThreadServer.connection, "Available"));
                            outputObjectToClient.writeObject(nextStatus);
                            OrderDto orderDto = (OrderDto) inputObjectFromClient.readObject();
                            ArrayList<InstrumentOrder> instrumentOrders = (ArrayList<InstrumentOrder>) inputObjectFromClient.readObject();
                            Order order = new OrderRepository().insertOrder(multiThreadServer.connection, orderDto);
                            for (InstrumentOrder instrumentOrder : instrumentOrders) {
                                new InstrumentOrderRepository().insertInstrumentOrder(multiThreadServer.connection, instrumentOrder, order);
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
                                new InstrumentOrderRepository().deleteAllInstrumentOrdersByOrderId(multiThreadServer.connection, id);
                                new OrderRepository().deleteOrder(multiThreadServer.connection, id);
                                System.out.println(new OrderRepository().insertOrderHistory(multiThreadServer.connection, new OrderHistory(
                                        new UserRepository().findUserByLogin(multiThreadServer.connection, new UserDto(orderToHistory.getLogin())),
                                        totalSum, orderToHistory.getTitle(), orderToHistory.getStatus())));
                            }
                            break;
                        case "changeStatusOfInstrument":
                            Instrument instrumentToChangeStatus = (Instrument) inputObjectFromClient.readObject();
                            Status statusToUse = (Status) inputObjectFromClient.readObject();
                            outputObjectToClient.writeObject(new InstrumentRepository().changeStatusOfInstrument(multiThreadServer.connection, instrumentToChangeStatus, statusToUse));
                    }
                    System.out.println(command);
                } catch (SQLException e) {
                    multiThreadServer.connection.rollback();
                    System.err.println(Arrays.toString(e.getStackTrace()));
                }
            }
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
