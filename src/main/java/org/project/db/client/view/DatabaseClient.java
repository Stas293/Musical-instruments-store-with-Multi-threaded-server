package org.project.db.client.view;

import org.jetbrains.annotations.NotNull;
import org.project.db.client.constants.MainConstants;
import org.project.db.dto.UserDto;
import org.project.db.model.Role;
import org.project.db.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DatabaseClient extends JFrame {
    private final JButton register = new JButton(MainConstants.REGISTER);
    private final JButton login = new JButton(MainConstants.LOGIN);
    private final transient Logger logger = Logger.getLogger(this.getClass().getName());
    private JButton openButton;
    private JButton closeButton;
    transient SocketChannel socket = null;
    private transient ObjectOutputStream toServer;
    private transient ObjectInputStream fromServer;
    private User user = null;
    JTextField tfLogin;
    JTextField tfEmail;
    JTextField tfFirstName;
    JTextField tfLastName;
    JTextField tfPhone;
    JTextField tfPassword;

    public DatabaseClient() {
        super("DatabaseClient");

        startView();
        register.addActionListener(new RegisterListener(this, toServer, fromServer));
        login.addActionListener(new LoginListener(this, toServer, fromServer));

        closeButton.addActionListener(e -> {
            try {
                socket.socket().close();
            } catch (Exception e1) {
                logger.log(java.util.logging.Level.SEVERE, "Error closing socket", e1);
            }
        });
        openButton.addActionListener(new OpenConnectionListener(this));
    }

    @NotNull
    public static String checkUser(String login, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(login.getBytes());
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        password = sb.toString();
        return password;
    }

    public static void printObject(DatabaseClient databaseClient) throws IOException, ClassNotFoundException {
        Object object = databaseClient.fromServer.readObject();
        if (object instanceof User) {
            databaseClient.user = (User) object;
            databaseClient.loggedInUser();
            System.out.println(object);
        } else if (object instanceof String) {
            System.out.println((String) object);
        }
    }

    public void startView() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        openButton = new JButton("Open Connection");
        closeButton = new JButton("Close Connection");

        JPanel controlConnectionPanel = new JPanel();

        controlConnectionPanel.add(openButton);
        controlConnectionPanel.add(closeButton);

        JPanel infoPanel = new JPanel();
        infoPanel.add(register);
        infoPanel.add(login);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        mainPanel.add(controlConnectionPanel, BorderLayout.NORTH);
        this.add(mainPanel);

        setSize(400, 176);
        repaint();
    }

    public void loggedInUser() {
        getContentPane().removeAll();
        openButton = new JButton("Open Connection");
        closeButton = new JButton("Close Connection");
        JPanel controlConnectionPanel = new JPanel();
        controlConnectionPanel.add(openButton);
        controlConnectionPanel.add(closeButton);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        List<Role> userRoles = user.getRoles();
        int numberOfRows = 0;
        List<String> userRolesNames = userRoles.stream().map(Role::getName).collect(Collectors.toCollection(ArrayList::new));
        numberOfRows = countRows(userRolesNames, numberOfRows);
        JPanel choicePanel = new JPanel(new GridLayout(numberOfRows, 1));
        addGUIs(userRolesNames, choicePanel);
        JButton exit = new JButton("Exit");
        choicePanel.add(exit);
        JButton logout = new JButton("Logout");
        choicePanel.add(logout);
        mainPanel.add(controlConnectionPanel, BorderLayout.NORTH);
        mainPanel.add(choicePanel, BorderLayout.CENTER);
        this.add(mainPanel);
        setSize(400, numberOfRows * 30 + 100);
        repaint();
        exit.addActionListener(e -> {
            try {
                socket.close();
            } catch (Exception e1) {
                logger.log(Level.SEVERE, "Error closing socket", e1);
            }
            System.exit(0);
        });
        logout.addActionListener(e -> {
            getContentPane().removeAll();
            user = null;
            startView();
        });
    }

    private void addGUIs(List<String> userRolesNames, JPanel choicePanel) {
        if (userRolesNames.contains("user")) {
            addUserGUI(choicePanel);
        }
        if (userRolesNames.contains("seller")) {
            addSellerGUI(choicePanel);
        }
        if (userRolesNames.contains("admin")) {
            addAdminGUI(choicePanel);
        }
    }

    private static int countRows(List<String> userRolesNames, int numberOfRows) {
        if (userRolesNames.contains("user")) {
            numberOfRows += 6;
        }
        if (userRolesNames.contains("seller")) {
            numberOfRows += 7;
        }
        if (userRolesNames.contains("admin")) {
            numberOfRows += 3;
        }
        numberOfRows += 2;
        return numberOfRows;
    }

    private void addAdminGUI(JPanel choicePanel) {
        JButton disableUser = new JButton("Disable user");
        choicePanel.add(disableUser);
        disableUser.addActionListener(new DisableUserListener(this, toServer, fromServer));
        JButton enableUser = new JButton("Enable user");
        choicePanel.add(enableUser);
        enableUser.addActionListener(new EnableUserListener(this, toServer, fromServer));
        JButton addRoleForUser = new JButton("Add role for user");
        choicePanel.add(addRoleForUser);
        addRoleForUser.addActionListener(new AddRoleForUserListener(this, toServer, fromServer));
    }

    private void addSellerGUI(JPanel choicePanel) {
        JButton showOrdersByUser = new JButton("Show orders by user login");
        choicePanel.add(showOrdersByUser);
        showOrdersByUser.addActionListener(new ShowOrdersByUserListener(this));
        JButton showAllOrders = new JButton("Show all orders");
        choicePanel.add(showAllOrders);
        showAllOrders.addActionListener(new ShowAllOrdersListener(this, toServer, fromServer));
        JButton changeStatusOfOrder = new JButton("Change status of order");
        choicePanel.add(changeStatusOfOrder);
        changeStatusOfOrder.addActionListener(new ChangeStatusOfOrderListener(this, toServer, fromServer));
        JButton changeStatusOfInstrument = new JButton("Change status of instrument");
        choicePanel.add(changeStatusOfInstrument);
        changeStatusOfInstrument.addActionListener(new ChangeStatusOfInstrumentListener(this, toServer, fromServer));
        JButton getStatuses = new JButton("Get statuses");
        choicePanel.add(getStatuses);
        getStatuses.addActionListener(new GetStatusesListener(this, toServer, fromServer));
        JButton getNumberOfInstruments = new JButton("Get number of instruments");
        choicePanel.add(getNumberOfInstruments);
        getNumberOfInstruments.addActionListener(new GetNumberOfInstrumentsListener(this, toServer, fromServer));
        JButton addInstrument = new JButton("Add instrument");
        choicePanel.add(addInstrument);
        addInstrument.addActionListener(new AddInstrumentListener(this, fromServer, toServer));
    }

    private void addUserGUI(JPanel choicePanel) {
        JButton modifyProfile = new JButton("Modify profile");
        choicePanel.add(modifyProfile);
        modifyProfile.addActionListener(new ModifyProfileListener(this, toServer, fromServer));
        JButton makeOrder = new JButton("Make order");
        choicePanel.add(makeOrder);
        makeOrder.addActionListener(new MakeOrderListener(this, toServer, fromServer));
        JButton showOrders = new JButton("Show orders");
        choicePanel.add(showOrders);
        showOrders.addActionListener(new ShowOrdersListener(this));
        JButton showAllInstruments = new JButton("Show all instruments");
        choicePanel.add(showAllInstruments);
        showAllInstruments.addActionListener(new ShowAllInstrumentsListener(this, fromServer, toServer));
        JButton showInstrumentByTitle = new JButton("Show instrument by title");
        choicePanel.add(showInstrumentByTitle);
        showInstrumentByTitle.addActionListener(new ShowInstrumentByTitleListener(this, fromServer, toServer));
        JButton showHistoryOrders = new JButton("Show history orders");
        choicePanel.add(showHistoryOrders);
        showHistoryOrders.addActionListener(new ShowHistoryOrdersListener(this, fromServer, toServer));
    }

    static JButton registerMainPanel(JPanel infoPanel, JPanel mainPanel, JButton btRegister, DatabaseClient databaseClient) {
        JButton btBack = new JButton(MainConstants.BACK);
        JPanel registerPanel = new JPanel();
        registerPanel.add(btBack);
        registerPanel.add(btRegister);
        mainPanel.add(registerPanel, BorderLayout.SOUTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.add(databaseClient.openButton);
        controlPanel.add(databaseClient.closeButton);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        databaseClient.add(mainPanel);
        return btBack;
    }

    public UserDto getUserDto() {
        return new UserDto(user.getLogin());
    }

    class OpenConnectionListener implements ActionListener {
        private final DatabaseClient databaseClient;

        private static final Logger logger = Logger.getLogger(OpenConnectionListener.class.getName());

        public OpenConnectionListener(DatabaseClient databaseClient) {
            this.databaseClient = databaseClient;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                databaseClient.socket = SocketChannel.open(new InetSocketAddress("localhost", 8000));
                fromServer = new ObjectInputStream(Channels.newInputStream(databaseClient.socket));
                toServer = new ObjectOutputStream(Channels.newOutputStream(databaseClient.socket));
            } catch (IOException e1) {
                logger.log(Level.WARNING, "Error while opening connection", e1);
            }
        }
    }
}