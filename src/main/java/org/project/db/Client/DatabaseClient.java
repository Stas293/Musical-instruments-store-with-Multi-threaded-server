package org.project.db.Client;

import org.project.db.Model.*;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class DatabaseClient extends JFrame {
    private final JButton register = new JButton("Register");
    private final JButton login = new JButton("Login");
    JButton openButton;
    JButton closeButton;
    Socket socket = null;
    ObjectOutputStream toServer;
    ObjectInputStream fromServer;
    User user;
    JTextField tfLogin;
    JTextField tfEmail;
    JTextField tfFirstName;
    JTextField tfLastName;
    JTextField tfPhone;
    JTextField tfPassword;

    public DatabaseClient() {
        super("DatabaseClient");
        user = null;

        startView();
        register.addActionListener(new RegisterListener(this));
        login.addActionListener(new LoginListener(this));

        closeButton.addActionListener((e) -> {
            try {
                socket.close();
            } catch (Exception e1) {
                System.err.println("error");
            }
        });
        openButton.addActionListener(new OpenConnectionListener(this));
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
        ArrayList<Role> userRoles = user.getRoles();
        ArrayList<String> userRolesNames = new ArrayList<>();
        int numberOfRows = 0;
        for (Role role : userRoles) {
            userRolesNames.add(role.getName());
        }
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
        JPanel choicePanel = new JPanel(new GridLayout(numberOfRows, 1));
        if (userRolesNames.contains("user")) {
            JButton modifyProfile = new JButton("Modify profile");
            choicePanel.add(modifyProfile);
            modifyProfile.addActionListener(new ModifyProfileListener(this));
            JButton makeOrder = new JButton("Make order");
            choicePanel.add(makeOrder);
            makeOrder.addActionListener(new MakeOrderListener(this));
            JButton showOrders = new JButton("Show orders");
            choicePanel.add(showOrders);
            showOrders.addActionListener(new ShowOrdersListener(this));
            JButton showAllInstruments = new JButton("Show all instruments");
            choicePanel.add(showAllInstruments);
            showAllInstruments.addActionListener(new ShowAllInstrumentsListener(this));
            JButton showInstrumentByTitle = new JButton("Show instrument by title");
            choicePanel.add(showInstrumentByTitle);
            showInstrumentByTitle.addActionListener(new ShowInstrumentByTitleListener(this));
            JButton showHistoryOrders = new JButton("Show history orders");
            choicePanel.add(showHistoryOrders);
            showHistoryOrders.addActionListener(new ShowHistoryOrdersListener(this));
        }
        if (userRolesNames.contains("seller")) {
            JButton showOrdersByUser = new JButton("Show orders by user login");
            choicePanel.add(showOrdersByUser);
            showOrdersByUser.addActionListener(new ShowOrdersByUserListener(this));
            JButton showAllOrders = new JButton("Show all orders");
            choicePanel.add(showAllOrders);
            showAllOrders.addActionListener(new ShowAllOrdersListener(this));
            JButton changeStatusOfOrder = new JButton("Change status of order");
            choicePanel.add(changeStatusOfOrder);
            changeStatusOfOrder.addActionListener(new ChangeStatusOfOrderListener(this));
            JButton changeStatusOfInstrument = new JButton("Change status of instrument");
            choicePanel.add(changeStatusOfInstrument);
            changeStatusOfInstrument.addActionListener(new ChangeStatusOfInstrumentListener(this));
            JButton getStatuses = new JButton("Get statuses");
            choicePanel.add(getStatuses);
            getStatuses.addActionListener(new GetStatusesListener(this));
            JButton getNumberOfInstruments = new JButton("Get number of instruments");
            choicePanel.add(getNumberOfInstruments);
            getNumberOfInstruments.addActionListener(new GetNumberOfInstrumentsListener(this));
            JButton addInstrument = new JButton("Add instrument");
            choicePanel.add(addInstrument);
            addInstrument.addActionListener(new AddInstrumentListener(this));
        }
        if (userRolesNames.contains("admin")) {
            JButton disableUser = new JButton("Disable user");
            choicePanel.add(disableUser);
            disableUser.addActionListener(new DisableUserListener(this));
            JButton enableUser = new JButton("Enable user");
            choicePanel.add(enableUser);
            enableUser.addActionListener(new EnableUserListener(this));
            JButton addRoleForUser = new JButton("Add role for user");
            choicePanel.add(addRoleForUser);
            addRoleForUser.addActionListener(new AddRoleForUserListener(this));
        }
        JButton exit = new JButton("Exit");
        choicePanel.add(exit);
        JButton logout = new JButton("Logout");
        choicePanel.add(logout);
        mainPanel.add(controlConnectionPanel, BorderLayout.NORTH);
        mainPanel.add(choicePanel, BorderLayout.CENTER);
        this.add(mainPanel);
        setSize(400, numberOfRows * 30 + 100);
        repaint();
        exit.addActionListener((e) -> {
            try {
                socket.close();
            } catch (Exception e1) {
                System.err.println("error");
            }
            System.exit(0);
        });
        logout.addActionListener((e) -> {
            getContentPane().removeAll();
            user = null;
            startView();
        });
    }

}