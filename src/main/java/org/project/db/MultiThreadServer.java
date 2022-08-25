package org.project.db;

import org.project.db.Dto.*;
import org.project.db.Model.*;
import org.project.db.Repository.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class MultiThreadServer extends JFrame implements Runnable {
    private final JTextArea ta;

    Connection connection;

    private int clientNo = 0;

    public MultiThreadServer() throws SQLException {
        ta = new JTextArea(10, 10);
        JScrollPane sp = new JScrollPane(ta);
        this.add(sp);
        this.setTitle("MultiThreadServer");
        this.setSize(400, 200);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
                        System.getenv("DB_SCHEMA"), System.getenv("MYSQL_USERNAME"),
                System.getenv("MYSQL_PASSWORD"));
        connection.setAutoCommit(false);
        Thread t = new Thread(this);
        t.start();
    }

    public static void main(String[] args) throws SQLException {
        MultiThreadServer mts = new MultiThreadServer();
        mts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mts.setVisible(true);
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            ta.append("MultiThreadServer started at "
                    + new Date() + '\n');


            while (true) {
                Socket socket = serverSocket.accept();

                clientNo++;

                ta.append("Starting thread for client " + clientNo +
                        " at " + new Date() + '\n');

                InetAddress inetAddress = socket.getInetAddress();
                ta.append("Client " + clientNo + "'s host name is "
                        + inetAddress.getHostName() + "\n");
                ta.append("Client " + clientNo + "'s IP Address is "
                        + inetAddress.getHostAddress() + "\n");
                new Thread(new HandleAClient(socket, clientNo)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private final Socket socket;
        private final int clientNum;
        private ObjectOutputStream outputObjectToClient;
        private ObjectInputStream inputObjectFromClient;


        /**
         * Construct a thread
         */
        public HandleAClient(Socket socket, int clientNum) throws IOException {
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
                                User user = new UserRepository().findUserByLogin(connection, new UserDto(s.getLogin()));
                                if (user == null) {
                                    new UserRepository().insertUser(connection, s);
                                    connection.commit();
                                    outputObjectToClient.writeObject(new UserRepository().findUserByLogin(connection, new UserDto(s.getLogin())));
                                } else {
                                    outputObjectToClient.writeObject("User already exists");
                                }
                                break;
                            }
                            case "login": {
                                LoginDto s = (LoginDto) inputObjectFromClient.readObject();
                                User user = new UserRepository().findUserByLogin(connection, new UserDto(s.getLogin()));
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
                                outputObjectToClient.writeObject(new UserRepository().getAllUsers(connection));
                                break;
                            case "reallyDisableUser":
                                Object object = inputObjectFromClient.readObject();
                                if (object instanceof UserDto) {
                                    new UserRepository().disableUser(connection, (UserDto) object);
                                    outputObjectToClient.writeObject("Successfully disabled user");
                                } else {
                                    outputObjectToClient.writeObject("Wrong object");
                                }
                                break;
                            case "reallyEnableUser":
                                Object object1 = inputObjectFromClient.readObject();
                                if (object1 instanceof UserDto) {
                                    new UserRepository().enableUser(connection, (UserDto) object1);
                                    outputObjectToClient.writeObject("Successfully enabled user");
                                } else {
                                    outputObjectToClient.writeObject("Wrong object");
                                }
                                break;
                            case "addRoleForUser":
                                System.out.println("addRoleForUser");
                                Object object2 = inputObjectFromClient.readObject();
                                ArrayList<Role> allRoles = new RoleRepository().getAllRoles(connection);
                                ArrayList<Role> haveRoles = new RoleRepository().getRolesForUser(connection, (UserDto) object2);
                                System.out.println(allRoles);
                                System.out.println(haveRoles);
                                allRoles.removeAll(haveRoles);
                                outputObjectToClient.writeObject(allRoles);
                                break;
                            case "RoleForUser":
                                Object object3 = inputObjectFromClient.readObject();
                                UserRoleDto userRoleDto = (UserRoleDto) object3;
                                new RoleRepository().insertRoleForUser(connection, new UserDto(userRoleDto.getLogin()), userRoleDto.getRoleName());
                                outputObjectToClient.writeObject(new UserRepository().findUserByLogin(connection, new UserDto(userRoleDto.getLogin())));
                                break;
                            case "changeEmail":
                                UserDto userDto = (UserDto) inputObjectFromClient.readObject();
                                String email = (String) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new UserRepository().changeEmail(connection, userDto, email));
                                break;
                            case "changeFirstName":
                                UserDto userDto1 = (UserDto) inputObjectFromClient.readObject();
                                String firstName = (String) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new UserRepository().changeFirstName(connection, userDto1, firstName));
                                break;
                            case "changeLastName":
                                UserDto userDto2 = (UserDto) inputObjectFromClient.readObject();
                                String lastName = (String) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new UserRepository().changeLastName(connection, userDto2, lastName));
                                break;
                            case "changePhone":
                                UserDto userDto3 = (UserDto) inputObjectFromClient.readObject();
                                String phone = (String) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new UserRepository().changePhone(connection, userDto3, phone));
                                break;
                            case "changePassword":
                                UserDto userDto4 = (UserDto) inputObjectFromClient.readObject();
                                String password = (String) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new UserRepository().changePassword(connection, userDto4, password));
                                break;
                            case "getStatuses":
                                outputObjectToClient.writeObject(new StatusRepository().getAllStatuses(connection));
                                break;
                            case "getNumberOfInstruments":
                                outputObjectToClient.writeObject(new InstrumentRepository().numberOfInstruments(connection));
                                break;
                            case "addInstrument":
                                Instrument instrument = (Instrument) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new InstrumentRepository().insertInstrument(connection, instrument));
                                break;
                            case "getAllInstruments":
                                outputObjectToClient.writeObject(new InstrumentRepository().getAllInstruments(connection));
                                break;
                            case "getInstrumentByTitle":
                                String title = (String) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new InstrumentRepository().getInstrumentByTitle(connection, title));
                                break;
                            case "makeOrder":
                                Status nextStatus = new StatusRepository().findNextStatus(connection, new StatusRepository().getStatusByName(connection, "Available"));
                                outputObjectToClient.writeObject(nextStatus);
                                OrderDto orderDto = (OrderDto) inputObjectFromClient.readObject();
                                ArrayList<InstrumentOrder> instrumentOrders = (ArrayList<InstrumentOrder>) inputObjectFromClient.readObject();
                                Order order = new OrderRepository().insertOrder(connection, orderDto);
                                for (InstrumentOrder instrumentOrder : instrumentOrders) {
                                    new InstrumentOrderRepository().insertInstrumentOrder(connection, instrumentOrder, order);
                                }
                                outputObjectToClient.writeObject(order);
                                break;
                            case "getAllOrdersForUser":
                                Object object8 = inputObjectFromClient.readObject();
                                System.out.println(object8);
                                UserDto userDto5 = (UserDto) object8;
                                outputObjectToClient.writeObject(new OrderRepository().getAllOrders(connection, userDto5));
                                break;
                            case "getAllHistoryOrdersForUser":
                                UserDto userDto6 = (UserDto) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new OrderRepository().getUserOrderHistory(connection, userDto6));
                                break;
                            case "getOrderByUser":
                                UserDto userDto7 = (UserDto) inputObjectFromClient.readObject();
                                ArrayList<Order> orders = new OrderRepository().getAllOrders(connection, userDto7);
                                outputObjectToClient.writeObject(orders);
                                break;
                            case "changeStatusOfOrder":
                                Long id = (long) Integer.parseInt(String.valueOf(inputObjectFromClient.readObject()));
                                Status nextStatuss = new OrderRepository().updateOrderStatus(connection, id, new StatusRepository().findNextStatus(connection, new OrderRepository().getOrderStatus(connection, id)));
                                System.out.println(nextStatuss);
                                if (Objects.equals(nextStatuss.getName(), "Arrived")) {
                                    double totalSum = new OrderRepository().getTotalSum(connection, id);
                                    Order orderToHistory = new OrderRepository().getOrderById(connection, id);
                                    new InstrumentOrderRepository().deleteAllInstrumentOrdersByOrderId(connection, id);
                                    new OrderRepository().deleteOrder(connection, id);
                                    System.out.println(new OrderRepository().insertOrderHistory(connection, new OrderHistory(
                                            new UserRepository().findUserByLogin(connection, new UserDto(orderToHistory.getLogin())),
                                            totalSum, orderToHistory.getTitle(), orderToHistory.getStatus())));
                                }
                                break;
                            case "changeStatusOfInstrument":
                                Instrument instrumentToChangeStatus = (Instrument) inputObjectFromClient.readObject();
                                Status statusToUse = (Status) inputObjectFromClient.readObject();
                                outputObjectToClient.writeObject(new InstrumentRepository().changeStatusOfInstrument(connection, instrumentToChangeStatus, statusToUse));
                        }
                        System.out.println(command);
                    } catch (SQLException e) {
                        connection.rollback();
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
}