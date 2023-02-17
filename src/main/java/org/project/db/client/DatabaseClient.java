package org.project.db.client;

import org.jetbrains.annotations.NotNull;
import org.project.db.client.view.LoginListener;
import org.project.db.client.view.RegisterListener;
import org.project.db.client.view.StartView;
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
    private final transient Logger logger = Logger.getLogger(this.getClass().getName());
    private JTextField tfPassword;
    private JTextField tfLogin;
    private StartView startView;
    private transient SocketChannel socket = null;
    private transient ObjectOutputStream toServer;
    private transient ObjectInputStream fromServer;
    private User user = null;

    public JTextField getTfPassword() {
        return tfPassword;
    }

    public JTextField getTfLogin() {
        return tfLogin;
    }

    public DatabaseClient() {
        super("DatabaseClient");
        startView = new StartView(this, toServer, fromServer);
        startView.startView();
        startView.getRegister().addActionListener(new RegisterListener(this, toServer, fromServer));
        startView.getLogin().addActionListener(new LoginListener(this, toServer, fromServer));

        startView.getCloseButton().addActionListener(e -> {
            try {
                socket.socket().close();
            } catch (Exception e1) {
                logger.log(java.util.logging.Level.SEVERE, "Error closing socket", e1);
            }
        });
        startView.getOpenButton().addActionListener(new OpenConnectionListener(this));
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

    public void loggedInUser() {
        getContentPane().removeAll();
        JPanel controlConnectionPanel = new JPanel();
        controlConnectionPanel.add(startView.getOpenButton());
        controlConnectionPanel.add(startView.getCloseButton());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        List<Role> userRoles = user.getRoles();
        int numberOfRows = 0;
        List<String> userRolesNames = userRoles.stream().map(Role::getName).collect(Collectors.toCollection(ArrayList::new));
        numberOfRows = StartView.countRows(userRolesNames, numberOfRows);
        JPanel choicePanel = new JPanel(new GridLayout(numberOfRows, 1));
        startView.addGUIs(userRolesNames, choicePanel);
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
            startView.startView();
        });
    }
    public UserDto getUserDto() {
        return new UserDto(user.getLogin());
    }

    class OpenConnectionListener implements ActionListener {
        private static final Logger logger = Logger.getLogger(OpenConnectionListener.class.getName());
        private final DatabaseClient databaseClient;

        OpenConnectionListener(DatabaseClient databaseClient) {
            this.databaseClient = databaseClient;
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                socket = SocketChannel.open(new InetSocketAddress("localhost", 8000));
                fromServer = new ObjectInputStream(Channels.newInputStream(socket));
                toServer = new ObjectOutputStream(Channels.newOutputStream(socket));
                startView = new StartView(databaseClient, toServer, fromServer);
            } catch (IOException e1) {
                logger.log(Level.WARNING, "Error while opening connection", e1);
            }
        }
    }

    public void setTfLogin(JTextField tfLogin) {
        this.tfLogin = tfLogin;
    }

    public void setTfPassword(JTextField tfPassword) {
        this.tfPassword = tfPassword;
    }

    public StartView getStartView() {
        return startView;
    }

    public void setStartView(StartView startView) {
        this.startView = startView;
    }
}