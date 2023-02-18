package org.project.db.client.view;

import org.project.db.client.DatabaseClient;
import org.project.db.client.constants.MainConstants;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class StartView implements Serializable {
    private final DatabaseClient databaseClient;
    private final JButton register = new JButton(MainConstants.REGISTER);
    private final JButton login = new JButton(MainConstants.LOGIN);
    private final ObjectOutputStream toServer;
    private final ObjectInputStream fromServer;
    private JButton openButton;
    private JButton closeButton;

    public StartView(DatabaseClient databaseClient, ObjectOutputStream toServer, ObjectInputStream fromServer) {
        this.databaseClient = databaseClient;
        this.toServer = toServer;
        this.fromServer = fromServer;
    }

    public static int countRows(List<String> userRolesNames, int numberOfRows) {
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

    public JButton getRegister() {
        return register;
    }

    public JButton getLogin() {
        return login;
    }

    public JButton getOpenButton() {
        return openButton;
    }

    public JButton getCloseButton() {
        return closeButton;
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
        databaseClient.add(mainPanel);

        databaseClient.setSize(400, 176);
        databaseClient.repaint();
    }

    public void addGUIs(List<String> userRolesNames, JPanel choicePanel) {
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

    public void addAdminGUI(JPanel choicePanel) {
        JButton disableUser = new JButton("Disable user");
        choicePanel.add(disableUser);
        disableUser.addActionListener(new DisableUserListener(databaseClient, toServer, fromServer));
        JButton enableUser = new JButton("Enable user");
        choicePanel.add(enableUser);
        enableUser.addActionListener(new EnableUserListener(databaseClient, toServer, fromServer));
        JButton addRoleForUser = new JButton("Add role for user");
        choicePanel.add(addRoleForUser);
        addRoleForUser.addActionListener(new AddRoleForUserListener(databaseClient, toServer, fromServer));
    }

    public void addSellerGUI(JPanel choicePanel) {
        JButton showOrdersByUser = new JButton("Show orders by user login");
        choicePanel.add(showOrdersByUser);
        showOrdersByUser.addActionListener(new ShowOrdersByUserListener(databaseClient, toServer, fromServer));
        JButton showAllOrders = new JButton("Show all orders");
        choicePanel.add(showAllOrders);
        showAllOrders.addActionListener(new ShowAllOrdersListener(databaseClient, toServer, fromServer));
        JButton changeStatusOfOrder = new JButton("Change status of order");
        choicePanel.add(changeStatusOfOrder);
        changeStatusOfOrder.addActionListener(new ChangeStatusOfOrderListener(databaseClient, toServer, fromServer));
        JButton changeStatusOfInstrument = new JButton("Change status of instrument");
        choicePanel.add(changeStatusOfInstrument);
        changeStatusOfInstrument.addActionListener(
                new ChangeStatusOfInstrumentListener(databaseClient, toServer, fromServer));
        JButton getStatuses = new JButton("Get statuses");
        choicePanel.add(getStatuses);
        getStatuses.addActionListener(new GetStatusesListener(databaseClient, toServer, fromServer));
        JButton getNumberOfInstruments = new JButton("Get number of instruments");
        choicePanel.add(getNumberOfInstruments);
        getNumberOfInstruments.addActionListener(new GetNumberOfInstrumentsListener(databaseClient, toServer, fromServer));
        JButton addInstrument = new JButton("Add instrument");
        choicePanel.add(addInstrument);
        addInstrument.addActionListener(new AddInstrumentListener(databaseClient, fromServer, toServer));
    }

    public void addUserGUI(JPanel choicePanel) {
        JButton modifyProfile = new JButton("Modify profile");
        choicePanel.add(modifyProfile);
        modifyProfile.addActionListener(new ModifyProfileListener(databaseClient, toServer, fromServer));
        JButton makeOrder = new JButton("Make order");
        choicePanel.add(makeOrder);
        makeOrder.addActionListener(new CreateOrderListener(databaseClient, toServer, fromServer));
        JButton showOrders = new JButton("Show orders");
        choicePanel.add(showOrders);
        showOrders.addActionListener(new ShowOrdersListener(databaseClient, toServer, fromServer));
        JButton showAllInstruments = new JButton("Show all instruments");
        choicePanel.add(showAllInstruments);
        showAllInstruments.addActionListener(new ShowAllInstrumentsListener(databaseClient, fromServer, toServer));
        JButton showInstrumentByTitle = new JButton("Show instrument by title");
        choicePanel.add(showInstrumentByTitle);
        showInstrumentByTitle.addActionListener(new ShowInstrumentByTitleListener(databaseClient, fromServer, toServer));
        JButton showHistoryOrders = new JButton("Show history orders");
        choicePanel.add(showHistoryOrders);
        showHistoryOrders.addActionListener(new ShowHistoryOrdersListener(databaseClient, fromServer, toServer));
    }

    public JButton registerMainPanel(JPanel infoPanel, JPanel mainPanel, JButton btRegister) {
        JButton btBack = new JButton(MainConstants.BACK);
        JPanel registerPanel = new JPanel();
        registerPanel.add(btBack);
        registerPanel.add(btRegister);
        mainPanel.add(registerPanel, BorderLayout.SOUTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel();
        controlPanel.add(openButton);
        controlPanel.add(closeButton);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        databaseClient.add(mainPanel);
        return btBack;
    }
}