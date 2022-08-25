package org.project.db;

import org.project.db.Dto.*;
import org.project.db.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DatabaseClient extends JFrame {
    private final JButton register = new JButton("Register");
    private final JButton login = new JButton("Login");
    JButton openButton;
    JButton closeButton;
    Socket socket = null;
    ObjectOutputStream toServer;
    ObjectInputStream fromServer;
    private User user;
    private JTextField tfLogin;
    private JTextField tfEmail;
    private JTextField tfFirstName;
    private JTextField tfLastName;
    private JTextField tfPhone;
    private JTextField tfPassword;

    public DatabaseClient() {
        super("DatabaseClient");
        user = null;

        startView();
        register.addActionListener(new RegisterListener());
        login.addActionListener(new LoginListener());

        closeButton.addActionListener((e) -> {
            try {
                socket.close();
            } catch (Exception e1) {
                System.err.println("error");
            }
        });
        openButton.addActionListener(new OpenConnectionListener());
    }

    public static void main(String[] args) {
        DatabaseClient sc = new DatabaseClient();
        sc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sc.setVisible(true);
    }

    private void startView() {
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

    private void loggedInUser() {
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
            modifyProfile.addActionListener(new ModifyProfileListener());
            JButton makeOrder = new JButton("Make order");
            choicePanel.add(makeOrder);
            makeOrder.addActionListener(new MakeOrderListener());
            JButton showOrders = new JButton("Show orders");
            choicePanel.add(showOrders);
            showOrders.addActionListener(new ShowOrdersListener());
            JButton showAllInstruments = new JButton("Show all instruments");
            choicePanel.add(showAllInstruments);
            showAllInstruments.addActionListener(new ShowAllInstrumentsListener());
            JButton showInstrumentByTitle = new JButton("Show instrument by title");
            choicePanel.add(showInstrumentByTitle);
            showInstrumentByTitle.addActionListener(new ShowInstrumentByTitleListener());
            JButton showHistoryOrders = new JButton("Show history orders");
            choicePanel.add(showHistoryOrders);
            showHistoryOrders.addActionListener(new ShowHistoryOrdersListener());
        }
        if (userRolesNames.contains("seller")) {
            JButton showOrdersByUser = new JButton("Show orders by user login");
            choicePanel.add(showOrdersByUser);
            showOrdersByUser.addActionListener(new ShowOrdersByUserListener());
            JButton showAllOrders = new JButton("Show all orders");
            choicePanel.add(showAllOrders);
            showAllOrders.addActionListener(new ShowAllOrdersListener());
            JButton changeStatusOfOrder = new JButton("Change status of order");
            choicePanel.add(changeStatusOfOrder);
            changeStatusOfOrder.addActionListener(new ChangeStatusOfOrderListener());
            JButton changeStatusOfInstrument = new JButton("Change status of instrument");
            choicePanel.add(changeStatusOfInstrument);
            changeStatusOfInstrument.addActionListener(new ChangeStatusOfInstrumentListener());
            JButton getStatuses = new JButton("Get statuses");
            choicePanel.add(getStatuses);
            getStatuses.addActionListener(new GetStatusesListener());
            JButton getNumberOfInstruments = new JButton("Get number of instruments");
            choicePanel.add(getNumberOfInstruments);
            getNumberOfInstruments.addActionListener(new GetNumberOfInstrumentsListener());
            JButton addInstrument = new JButton("Add instrument");
            choicePanel.add(addInstrument);
            addInstrument.addActionListener(new AddInstrumentListener());
        }
        if (userRolesNames.contains("admin")) {
            JButton disableUser = new JButton("Disable user");
            choicePanel.add(disableUser);
            disableUser.addActionListener(new DisableUserListener());
            JButton enableUser = new JButton("Enable user");
            choicePanel.add(enableUser);
            enableUser.addActionListener(new EnableUserListener());
            JButton addRoleForUser = new JButton("Add role for user");
            choicePanel.add(addRoleForUser);
            addRoleForUser.addActionListener(new AddRoleForUserListener());
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

    class OpenConnectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                socket = new Socket("localhost", 8000);
                fromServer = new ObjectInputStream(socket.getInputStream());
                toServer = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(6, 2));
            infoPanel.add(new JLabel("Login"));
            tfLogin = new JTextField(20);
            infoPanel.add(tfLogin);
            infoPanel.add(new JLabel("Email"));
            tfEmail = new JTextField(20);
            infoPanel.add(tfEmail);
            infoPanel.add(new JLabel("First Name"));
            tfFirstName = new JTextField(20);
            infoPanel.add(tfFirstName);
            infoPanel.add(new JLabel("Last Name"));
            tfLastName = new JTextField(20);
            infoPanel.add(tfLastName);
            infoPanel.add(new JLabel("Phone"));
            tfPhone = new JTextField(20);
            infoPanel.add(tfPhone);
            infoPanel.add(new JLabel("Password"));
            tfPassword = new JTextField(20);
            infoPanel.add(tfPassword);
            JButton btRegister = new JButton("Register");
            JButton btBack = new JButton("Back");
            JPanel registerPanel = new JPanel();
            registerPanel.add(btBack);
            registerPanel.add(btRegister);
            mainPanel.add(registerPanel, BorderLayout.SOUTH);
            mainPanel.add(infoPanel, BorderLayout.CENTER);
            JPanel controlPanel = new JPanel();
            controlPanel.add(openButton);
            controlPanel.add(closeButton);
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            add(mainPanel);
            setSize(450, 300);
            repaint();
            btBack.addActionListener((event) -> {
                getContentPane().removeAll();
                startView();
            });
            btRegister.addActionListener(new RegisterButtonListener());
        }
    }

    class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String login = tfLogin.getText().trim();
                String email = tfEmail.getText().trim();
                String firstName = tfFirstName.getText().trim();
                String lastName = tfLastName.getText().trim();
                String phone = tfPhone.getText().trim();
                String password = tfPassword.getText().trim();
                if (login.length() == 0 || email.length() == 0 || firstName.length() == 0 || lastName.length() == 0 || phone.length() == 0 || password.length() == 0) {
                    return;
                }
                toServer.writeObject("register");
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(login.getBytes());
                byte[] bytes = md.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();

                for (byte aByte : bytes) {
                    sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                            .substring(1));
                }
                password = sb.toString();
                toServer.writeObject(new RegistrationDto(login, firstName, lastName, email, password, phone));
                Object object = fromServer.readObject();
                if (object instanceof User) {
                    user = (User) object;
                    loggedInUser();
                    System.out.println(object);
                } else if (object instanceof String) {
                    System.out.println((String) object);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(2, 2));
            infoPanel.add(new JLabel("Login"));
            tfLogin = new JTextField(20);
            infoPanel.add(tfLogin);
            infoPanel.add(new JLabel("Password"));
            tfPassword = new JTextField(20);
            infoPanel.add(tfPassword);
            JButton btLogin = new JButton("Login");
            JButton btBack = new JButton("Back");
            JPanel loginPanel = new JPanel();
            loginPanel.add(btBack);
            loginPanel.add(btLogin);
            mainPanel.add(loginPanel, BorderLayout.SOUTH);
            mainPanel.add(infoPanel, BorderLayout.CENTER);
            JPanel controlPanel = new JPanel();
            controlPanel.add(openButton);
            controlPanel.add(closeButton);
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            add(mainPanel);
            setSize(400, 175);
            repaint();
            btBack.addActionListener((event) -> {
                getContentPane().removeAll();
                startView();
            });
            btLogin.addActionListener(new LoginButtonListener());
        }
    }

    class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String login = tfLogin.getText().trim();
                String password = tfPassword.getText().trim();
                if (login.length() == 0 || password.length() == 0) {
                    return;
                }
                toServer.writeObject("login");
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(login.getBytes());
                byte[] bytes = md.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();

                for (byte aByte : bytes) {
                    sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                            .substring(1));
                }
                password = sb.toString();
                toServer.writeObject(new LoginDto(login, password));
                Object object = fromServer.readObject();
                if (object instanceof User) {
                    user = (User) object;
                    loggedInUser();
                    System.out.println(object);
                } else if (object instanceof String) {
                    System.out.println((String) object);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private class DisableUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getContentPane().removeAll();
                toServer.writeObject("allUserDtos");
                Object object = fromServer.readObject();
                ArrayList<UserDto> users = (ArrayList<UserDto>) object;
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(users.size(), 1));
                for (UserDto userDto : users) {
                    JButton btDisable = new JButton("Disable " + userDto.getLogin());
                    infoPanel.add(btDisable);
                    btDisable.addActionListener((event) -> {
                        try {
                            System.out.println(btDisable.getText().split(" ")[1]);
                            toServer.writeObject("reallyDisableUser");
                            toServer.writeObject(new UserDto(btDisable.getText().split(" ")[1]));
                            Object object1 = fromServer.readObject();
                            System.out.println((String) object1);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
                JScrollPane scroller = new JScrollPane(infoPanel);
                mainPanel.add(scroller, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 75 * users.size());
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class EnableUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getContentPane().removeAll();
                toServer.writeObject("allUserDtos");
                Object object = fromServer.readObject();
                ArrayList<UserDto> users = (ArrayList<UserDto>) object;
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(users.size(), 1));
                for (UserDto userDto : users) {
                    JButton btEnable = new JButton("Enable " + userDto.getLogin());
                    infoPanel.add(btEnable);
                    btEnable.addActionListener((event) -> {
                        try {
                            System.out.println(btEnable.getText().split(" ")[1]);
                            toServer.writeObject("reallyEnableUser");
                            toServer.writeObject(new UserDto(btEnable.getText().split(" ")[1]));
                            Object object1 = fromServer.readObject();
                            System.out.println((String) object1);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
                JScrollPane scroller = new JScrollPane(infoPanel);
                mainPanel.add(scroller, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 75 * users.size());
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class AddRoleForUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getContentPane().removeAll();
                toServer.writeObject("allUserDtos");
                Object object = fromServer.readObject();
                ArrayList<UserDto> users = (ArrayList<UserDto>) object;
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(users.size(), 1));
                for (UserDto userDto : users) {
                    JButton btAddRole = new JButton("Add role for " + userDto.getLogin());
                    infoPanel.add(btAddRole);
                    btAddRole.addActionListener(new addRoleButtonListner(btAddRole.getText().split(" ")[3]));
                }
                JScrollPane scroller = new JScrollPane(infoPanel);
                mainPanel.add(scroller, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 75 * users.size());
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class addRoleButtonListner implements ActionListener {
        private final String login;

        public addRoleButtonListner(String login) {
            this.login = login;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getContentPane().removeAll();
                toServer.writeObject("addRoleForUser");
                toServer.writeObject(new UserDto(login));
                Object object = fromServer.readObject();
                ArrayList<Role> roles = (ArrayList<Role>) object;
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(roles.size(), 1));
                for (Role role : roles) {
                    JButton btAddRole = new JButton("Add role " + role.getName());
                    infoPanel.add(btAddRole);
                    btAddRole.addActionListener((event) -> {
                        try {
                            toServer.writeObject("RoleForUser");
                            toServer.writeObject(new UserRoleDto(login, btAddRole.getText().split(" ")[2]));
                            Object object1 = fromServer.readObject();
                            System.out.println(object1);
                            loggedInUser();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
                JScrollPane scroller = new JScrollPane(infoPanel);
                mainPanel.add(scroller, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 75 * roles.size() + 70);
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ModifyProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(5, 1));
            JButton btChangeEmail = new JButton("Change email");
            infoPanel.add(btChangeEmail);
            JButton btChangeFirstName = new JButton("Change first name");
            infoPanel.add(btChangeFirstName);
            JButton btChangeLastName = new JButton("Change last name");
            infoPanel.add(btChangeLastName);
            JButton btChangePhone = new JButton("Change phone");
            infoPanel.add(btChangePhone);
            JButton btChangePassword = new JButton("Change password");
            infoPanel.add(btChangePassword);
            mainPanel.add(infoPanel, BorderLayout.CENTER);
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> loggedInUser());
            mainPanel.add(btBack, BorderLayout.SOUTH);
            add(mainPanel);
            setSize(400, 400);
            repaint();
            btChangeEmail.addActionListener((event) -> {
                getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfEmail = new JTextField();
                infoPanel1.add(tfEmail);
                JButton btChangeEmail1 = new JButton("Change email");
                infoPanel1.add(btChangeEmail1);
                mainPanel1.add(infoPanel1, BorderLayout.CENTER);
                JButton btBack1 = new JButton("Back");
                btBack1.addActionListener((event1) -> loggedInUser());
                mainPanel1.add(btBack1, BorderLayout.SOUTH);
                add(mainPanel1);
                setSize(400, 150);
                repaint();
                btChangeEmail1.addActionListener((event1) -> {
                    try {
                        toServer.writeObject("changeEmail");
                        toServer.writeObject(new UserDto(user.getLogin()));
                        toServer.writeObject(tfEmail.getText().trim());
                        Object object = fromServer.readObject();
                        System.out.println(object);
                        loggedInUser();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            });
            btChangeFirstName.addActionListener((event) -> {
                getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfFirstName = new JTextField();
                infoPanel1.add(tfFirstName);
                JButton btChangeFirstName1 = new JButton("Change first name");
                infoPanel1.add(btChangeFirstName1);
                mainPanel1.add(infoPanel1, BorderLayout.CENTER);
                JButton btBack1 = new JButton("Back");
                btBack1.addActionListener((event1) -> loggedInUser());
                mainPanel1.add(btBack1, BorderLayout.SOUTH);
                add(mainPanel1);
                setSize(400, 150);
                repaint();
                btChangeFirstName1.addActionListener((event1) -> {
                    try {
                        toServer.writeObject("changeFirstName");
                        toServer.writeObject(new UserDto(user.getLogin()));
                        toServer.writeObject(tfFirstName.getText().trim());
                        Object object = fromServer.readObject();
                        System.out.println(object);
                        loggedInUser();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            });
            btChangeLastName.addActionListener((event) -> {
                getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfLastName = new JTextField();
                infoPanel1.add(tfLastName);
                JButton btChangeLastName1 = new JButton("Change last name");
                infoPanel1.add(btChangeLastName1);
                mainPanel1.add(infoPanel1, BorderLayout.CENTER);
                JButton btBack1 = new JButton("Back");
                btBack1.addActionListener((event1) -> loggedInUser());
                mainPanel1.add(btBack1, BorderLayout.SOUTH);
                add(mainPanel1);
                setSize(400, 150);
                repaint();
                btChangeLastName1.addActionListener((event1) -> {
                    try {
                        toServer.writeObject("changeLastName");
                        toServer.writeObject(new UserDto(user.getLogin()));
                        toServer.writeObject(tfLastName.getText().trim());
                        Object object = fromServer.readObject();
                        System.out.println(object);
                        loggedInUser();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            });
            btChangePhone.addActionListener((event) -> {
                getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfPhone = new JTextField();
                infoPanel1.add(tfPhone);
                JButton btChangePhone1 = new JButton("Change phone");
                infoPanel1.add(btChangePhone1);
                mainPanel1.add(infoPanel1, BorderLayout.CENTER);
                JButton btBack1 = new JButton("Back");
                btBack1.addActionListener((event1) -> loggedInUser());
                mainPanel1.add(btBack1, BorderLayout.SOUTH);
                add(mainPanel1);
                setSize(400, 150);
                repaint();
                btChangePhone1.addActionListener((event1) -> {
                    try {
                        toServer.writeObject("changePhone");
                        toServer.writeObject(new UserDto(user.getLogin()));
                        toServer.writeObject(tfPhone.getText().trim());
                        Object object = fromServer.readObject();
                        System.out.println(object);
                        loggedInUser();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            });
            btChangePassword.addActionListener((event) -> {
                getContentPane().removeAll();
                JPanel mainPanel1 = new JPanel();
                mainPanel1.setLayout(new BorderLayout());
                JPanel infoPanel1 = new JPanel();
                infoPanel1.setLayout(new GridLayout(1, 1));
                JTextField tfPassword = new JTextField();
                infoPanel1.add(tfPassword);
                JButton btChangePassword1 = new JButton("Change password");
                infoPanel1.add(btChangePassword1);
                mainPanel1.add(infoPanel1, BorderLayout.CENTER);
                JButton btBack1 = new JButton("Back");
                btBack1.addActionListener((event1) -> loggedInUser());
                mainPanel1.add(btBack1, BorderLayout.SOUTH);
                add(mainPanel1);
                setSize(400, 150);
                repaint();
                btChangePassword1.addActionListener((event1) -> {
                    try {
                        MessageDigest md = MessageDigest.getInstance("MD5");
                        md.update(user.getLogin().getBytes());
                        String password = tfPassword.getText().trim();
                        byte[] bytes = md.digest(password.getBytes());
                        StringBuilder sb = new StringBuilder();

                        for (byte aByte : bytes) {
                            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                                    .substring(1));
                        }
                        password = sb.toString();
                        toServer.writeObject("changePassword");
                        toServer.writeObject(new UserDto(user.getLogin()));
                        toServer.writeObject(password);
                        Object object = fromServer.readObject();
                        System.out.println(object);
                        loggedInUser();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException | NoSuchAlgorithmException ex) {
                        System.out.println(Arrays.toString(ex.getStackTrace()) + " " + ex.getMessage());
                    }
                });
            });
        }
    }

    private class GetStatusesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("getStatuses");
                ArrayList<Status> statuses = (ArrayList<Status>) fromServer.readObject();
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(6, 4));
                JLabel lbId = new JLabel("id");
                infoPanel.add(lbId);
                JLabel lbCode = new JLabel("code");
                infoPanel.add(lbCode);
                JLabel lbName = new JLabel("description");
                infoPanel.add(lbName);
                JLabel lbClosed = new JLabel("closed");
                infoPanel.add(lbClosed);
                for (Status status : statuses) {
                    JLabel lbId1 = new JLabel(String.valueOf(status.getId()));
                    infoPanel.add(lbId1);
                    JLabel lbCode1 = new JLabel(status.getCode());
                    infoPanel.add(lbCode1);
                    JLabel lbName1 = new JLabel(status.getName());
                    infoPanel.add(lbName1);
                    JLabel lbClosed1 = new JLabel(status.isClosed() ? "true" : "false");
                    infoPanel.add(lbClosed1);
                }
                mainPanel.add(infoPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 400);
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class GetNumberOfInstrumentsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("getNumberOfInstruments");
                int numberOfInstruments = (int) fromServer.readObject();
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(1, 1));
                JLabel lbNumberOfInstruments = new JLabel("Number of instruments: " + numberOfInstruments);
                infoPanel.add(lbNumberOfInstruments);
                mainPanel.add(infoPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 150);
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class AddInstrumentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(2, 4));
            JLabel lbTitle = new JLabel("Title");
            infoPanel.add(lbTitle);
            JLabel lbDescription = new JLabel("Description");
            infoPanel.add(lbDescription);
            JLabel lbStatus = new JLabel("Status");
            infoPanel.add(lbStatus);
            JLabel lbPrice = new JLabel("Price");
            infoPanel.add(lbPrice);
            JTextField tfTitle = new JTextField();
            infoPanel.add(tfTitle);
            JTextField tfDescription = new JTextField();
            infoPanel.add(tfDescription);
            JComboBox<String> cbStatus = new JComboBox<>();
            cbStatus.addItem("Not Available");
            cbStatus.addItem("Not Processed");
            cbStatus.addItem("Available");
            cbStatus.addItem("Order Processing");
            cbStatus.addItem("Arrived");
            cbStatus.setSelectedIndex(2);
            infoPanel.add(cbStatus);
            JTextField tfPrice = new JTextField();
            infoPanel.add(tfPrice);
            mainPanel.add(infoPanel, BorderLayout.CENTER);
            JButton btAdd = new JButton("Add");
            btAdd.addActionListener((event) -> {
                try {
                    if (tfDescription.getText().trim().isEmpty() || tfTitle.getText().trim().isEmpty() || Double.parseDouble(tfPrice.getText().trim()) < 0) {
                        return;
                    }
                    toServer.writeObject("getStatuses");
                    ArrayList<Status> statuses = (ArrayList<Status>) fromServer.readObject();
                    String statusName = cbStatus.getSelectedItem().toString();
                    Status status = null;
                    for (Status s : statuses) {
                        if (s.getName().equals(statusName)) {
                            status = s;
                            break;
                        }
                    }
                    toServer.writeObject("addInstrument");
                    toServer.writeObject(new Instrument(tfDescription.getText().trim(), tfTitle.getText().trim(), status, Double.parseDouble(tfPrice.getText().trim())));
                    Object object = fromServer.readObject();
                    System.out.println(object);
                    loggedInUser();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (NumberFormatException ex) {
                    System.out.println("Price must be a number");
                }
            });
            JButton btBack = new JButton("Back");
            btBack.addActionListener((event) -> loggedInUser());
            JPanel btPanel = new JPanel();
            btPanel.add(btBack);
            btPanel.add(btAdd);
            mainPanel.add(btPanel, BorderLayout.SOUTH);
            add(mainPanel);
            setSize(400, 200);
            repaint();
        }
    }

    private class ShowAllInstrumentsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                toServer.writeObject("getAllInstruments");
                ArrayList<Instrument> instruments = (ArrayList<Instrument>) fromServer.readObject();
                infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6));
                JLabel lbId = new JLabel("id");
                infoPanel.add(lbId);
                lbId.setPreferredSize(new Dimension(1, 1));
                JLabel lbTitle = new JLabel("title");
                infoPanel.add(lbTitle);
                lbTitle.setPreferredSize(new Dimension(1, 1));
                JLabel lbDescription = new JLabel("description");
                infoPanel.add(lbDescription);
                lbDescription.setPreferredSize(new Dimension(1, 1));
                lbDescription.setMaximumSize(new Dimension(1, 1));
                lbDescription.setPreferredSize(new Dimension(1, 1));
                JLabel lbStatus = new JLabel("status");
                infoPanel.add(lbStatus);
                lbStatus.setPreferredSize(new Dimension(1, 1));
                JLabel lbPrice = new JLabel("price");
                infoPanel.add(lbPrice);
                lbPrice.setPreferredSize(new Dimension(1, 1));
                JLabel lbDateUpdated = new JLabel("dateUpdated");
                infoPanel.add(lbDateUpdated);
                for (Instrument instrument : instruments) {
                    JLabel lbId1 = new JLabel(instrument.getId() + "");
                    infoPanel.add(lbId1);
                    lbId1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbTitle1 = new JLabel(instrument.getTitle());
                    infoPanel.add(lbTitle1);
                    lbTitle1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbDescription1 = new JLabel(instrument.getDescription());
                    infoPanel.add(lbDescription1);
                    lbDescription1.setMaximumSize(new Dimension(1, 1));
                    lbDescription1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbStatus1 = new JLabel(instrument.getStatus().getName());
                    infoPanel.add(lbStatus1);
                    lbStatus1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbPrice1 = new JLabel(instrument.getPrice() + "");
                    infoPanel.add(lbPrice1);
                    lbPrice1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbDateUpdated1 = new JLabel(instrument.getDateUpdated() + "");
                    infoPanel.add(lbDateUpdated1);
                    lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
                }
                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(600, 300);
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ShowInstrumentByTitleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel controlPanel = new JPanel();
            JTextField tfTitle = new JTextField();
            controlPanel.add(tfTitle);
            tfTitle.setPreferredSize(new Dimension(100, 30));
            JButton btSearch = new JButton("Search");
            controlPanel.add(btSearch);
            JButton btBack = new JButton("Back");
            btSearch.addActionListener((event) -> {
                        try {
                            String title;
                            toServer.writeObject("getInstrumentByTitle");
                            if (!tfTitle.getText().trim().isEmpty()) {
                                title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
                            } else {
                                title = "";
                            }
                            //title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
                            toServer.writeObject(title);
                            Instrument instrument = (Instrument) fromServer.readObject();
                            mainPanel.removeAll();
                            mainPanel.add(controlPanel, BorderLayout.NORTH);
                            mainPanel.add(btBack, BorderLayout.SOUTH);
                            setSize(400, 200);
                            add(mainPanel);
                            if (instrument != null) {
                                JPanel infoPanel = new JPanel();
                                infoPanel.setLayout(new GridLayout(2, 6));
                                JLabel lbId = new JLabel("id");
                                infoPanel.add(lbId);
                                lbId.setPreferredSize(new Dimension(1, 1));
                                JLabel lbTitle = new JLabel("title");
                                infoPanel.add(lbTitle);
                                lbTitle.setPreferredSize(new Dimension(1, 1));
                                JLabel lbDescription = new JLabel("description");
                                infoPanel.add(lbDescription);
                                lbDescription.setPreferredSize(new Dimension(1, 1));
                                JLabel lbStatus = new JLabel("status");
                                infoPanel.add(lbStatus);
                                lbStatus.setPreferredSize(new Dimension(1, 1));
                                JLabel lbPrice = new JLabel("price");
                                infoPanel.add(lbPrice);
                                lbPrice.setPreferredSize(new Dimension(1, 1));
                                JLabel lbDateUpdated = new JLabel("dateUpdated");
                                infoPanel.add(lbDateUpdated);
                                JLabel lbId1 = new JLabel(instrument.getId() + "");
                                infoPanel.add(lbId1);
                                lbId1.setPreferredSize(new Dimension(1, 1));
                                JLabel lbTitle1 = new JLabel(instrument.getTitle());
                                infoPanel.add(lbTitle1);
                                lbTitle1.setPreferredSize(new Dimension(1, 1));
                                JLabel lbDescription1 = new JLabel(instrument.getDescription());
                                infoPanel.add(lbDescription1);
                                lbDescription1.setPreferredSize(new Dimension(1, 1));
                                JLabel lbStatus1 = new JLabel(instrument.getStatus().getName());
                                infoPanel.add(lbStatus1);
                                lbStatus1.setPreferredSize(new Dimension(1, 1));
                                JLabel lbPrice1 = new JLabel(instrument.getPrice() + "");
                                infoPanel.add(lbPrice1);
                                lbPrice1.setPreferredSize(new Dimension(1, 1));
                                JLabel lbDateUpdated1 = new JLabel(instrument.getDateUpdated() + "");
                                infoPanel.add(lbDateUpdated1);
                                lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
                                mainPanel.add(infoPanel, BorderLayout.CENTER);
                                add(mainPanel);
                                setSize(600, 300);
                                repaint();
                            } else {
                                mainPanel.removeAll();
                                mainPanel.add(controlPanel, BorderLayout.NORTH);
                                mainPanel.add(btBack, BorderLayout.SOUTH);
                                setSize(400, 200);
                                add(mainPanel);
                                repaint();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
            );
            btBack.addActionListener((event) -> loggedInUser());
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            mainPanel.add(btBack, BorderLayout.SOUTH);
            add(mainPanel);
            setSize(400, 200);
            repaint();
        }
    }

    private class MakeOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                toServer.writeObject("getAllInstruments");
                ArrayList<Instrument> originalInstruments = (ArrayList<Instrument>) fromServer.readObject();
                ArrayList<Instrument> instruments = new ArrayList<>();
                for (Instrument originalInstrument : originalInstruments) {
                    if (originalInstrument.getStatus().getName().equals("Available")) {
                        instruments.add(originalInstrument);
                    }
                }
                infoPanel.setLayout(new GridLayout(instruments.size() + 1, 6 + 1));
                JLabel lbId = new JLabel("id");
                infoPanel.add(lbId);
                lbId.setPreferredSize(new Dimension(1, 1));
                JLabel lbTitle = new JLabel("title");
                infoPanel.add(lbTitle);
                lbTitle.setPreferredSize(new Dimension(1, 1));
                JLabel lbDescription = new JLabel("description");
                infoPanel.add(lbDescription);
                lbDescription.setPreferredSize(new Dimension(1, 1));
                lbDescription.setMaximumSize(new Dimension(1, 1));
                lbDescription.setPreferredSize(new Dimension(1, 1));
                JLabel lbStatus = new JLabel("status");
                infoPanel.add(lbStatus);
                lbStatus.setPreferredSize(new Dimension(1, 1));
                JLabel lbPrice = new JLabel("price");
                infoPanel.add(lbPrice);
                lbPrice.setPreferredSize(new Dimension(1, 1));
                JLabel lbDateUpdated = new JLabel("dateUpdated");
                infoPanel.add(lbDateUpdated);
                JLabel lbQuantity = new JLabel("quantity");
                infoPanel.add(lbQuantity);
                JComboBox<Integer> cbQuantity = new JComboBox<>();
                for (int i = 0; i <= 10; i++) {
                    cbQuantity.addItem(i);
                }
                JComboBox[] cbQuantitys = new JComboBox[instruments.size()];
                for (int i = 0; i < instruments.size(); i++) {
                    JLabel lbId1 = new JLabel(instruments.get(i).getId() + "");
                    infoPanel.add(lbId1);
                    lbId1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbTitle1 = new JLabel(instruments.get(i).getTitle());
                    infoPanel.add(lbTitle1);
                    lbTitle1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbDescription1 = new JLabel(instruments.get(i).getDescription());
                    infoPanel.add(lbDescription1);
                    lbDescription1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbStatus1 = new JLabel(instruments.get(i).getStatus().getName());
                    infoPanel.add(lbStatus1);
                    lbStatus1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbPrice1 = new JLabel(instruments.get(i).getPrice() + "");
                    infoPanel.add(lbPrice1);
                    lbPrice1.setPreferredSize(new Dimension(1, 1));
                    JLabel lbDateUpdated1 = new JLabel(instruments.get(i).getDateUpdated() + "");
                    infoPanel.add(lbDateUpdated1);
                    lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
                    cbQuantitys[i] = new JComboBox<>();
                    for (int j = 0; j <= 10; j++) {
                        cbQuantitys[i].addItem(j);
                    }
                    infoPanel.add(cbQuantitys[i]);
                }
                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                ArrayList<InstrumentOrder> instrumentOrders = new ArrayList<>();
                JPanel controlPanel = new JPanel();
                JButton btMakeOrder = new JButton("Make order");
                btMakeOrder.addActionListener((event) -> {
                            try {
                                for (int i = 0; i < instruments.size(); i++) {
                                    if ((Integer) cbQuantitys[i].getSelectedItem() > 0) {
                                        instrumentOrders.add(new InstrumentOrder(instruments.get(i), instruments.get(i).getPrice(), (Integer) cbQuantitys[i].getSelectedItem()));
                                    }
                                }
                                if (instrumentOrders.size() > 0) {
                                    toServer.writeObject("makeOrder");
                                    Status nextstatus = (Status) fromServer.readObject();
                                    toServer.writeObject(new OrderDto(user.getLogin() + " " + new Date(), user.getLogin(), nextstatus));
                                    toServer.writeObject(instrumentOrders);
                                    System.out.println(fromServer.readObject());
                                    loggedInUser();
                                }

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                );
                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                controlPanel.add(btBack);
                controlPanel.add(btMakeOrder);
                mainPanel.add(controlPanel, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(600, 300);
                repaint();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ShowOrdersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("getAllOrdersForUser");
                toServer.writeObject(new UserDto(user.getLogin()));
                ArrayList<Order> orders = (ArrayList<Order>) fromServer.readObject();
                System.out.println(orders);
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                int size = 0;
                for (Order value : orders) {
                    for (int j = 0; j < value.getInstruments().size(); j++) {
                        size++;
                    }
                }
                infoPanel.setLayout(new GridLayout(size + 1, 6));
                JLabel orderTitle = new JLabel("Order title");
                infoPanel.add(orderTitle);
                JLabel orderStatus = new JLabel("Order status");
                infoPanel.add(orderStatus);
                JLabel isClosed = new JLabel("Is closed");
                infoPanel.add(isClosed);
                JLabel instrumentTitle = new JLabel("Instrument title");
                infoPanel.add(instrumentTitle);
                JLabel instrumentPrice = new JLabel("Instrument price");
                infoPanel.add(instrumentPrice);
                JLabel instrumentQuantity = new JLabel("Instrument quantity");
                infoPanel.add(instrumentQuantity);
                for (Order order : orders) {
                    for (int j = 0; j < order.getInstruments().size(); j++) {
                        JLabel lbOrderTitle = new JLabel(order.getTitle());
                        infoPanel.add(lbOrderTitle);
                        lbOrderTitle.setPreferredSize(new Dimension(1, 1));
                        JLabel lbOrderStatus = new JLabel(order.getStatus().getName());
                        infoPanel.add(lbOrderStatus);
                        lbOrderStatus.setPreferredSize(new Dimension(1, 1));
                        JLabel lbIsClosed = new JLabel(order.isClosed() + "");
                        infoPanel.add(lbIsClosed);
                        lbIsClosed.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentTitle = new JLabel(order.getInstruments().get(j).getInstrument().getTitle());
                        infoPanel.add(lbInstrumentTitle);
                        lbInstrumentTitle.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentPrice = new JLabel(order.getInstruments().get(j).getPrice() + "");
                        infoPanel.add(lbInstrumentPrice);
                        lbInstrumentPrice.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentQuantity = new JLabel(order.getInstruments().get(j).getQuantity() + "");
                        infoPanel.add(lbInstrumentQuantity);
                        lbInstrumentQuantity.setPreferredSize(new Dimension(1, 1));
                    }
                }
                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(600, 300);
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ShowHistoryOrdersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("getAllHistoryOrdersForUser");
                toServer.writeObject(new UserDto(user.getLogin()));
                ArrayList<OrderHistory> ordersHistory = (ArrayList<OrderHistory>) fromServer.readObject();
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(ordersHistory.size() + 1, 6));
                JLabel orderHistoryId = new JLabel("Order history id");
                infoPanel.add(orderHistoryId);
                JLabel orderHistoryTitle = new JLabel("Order history title");
                infoPanel.add(orderHistoryTitle);
                JLabel userLogin = new JLabel("User login");
                infoPanel.add(userLogin);
                JLabel dateCreated = new JLabel("Date created");
                infoPanel.add(dateCreated);
                JLabel totalSum = new JLabel("Total sum");
                infoPanel.add(totalSum);
                JLabel status = new JLabel("Status");
                infoPanel.add(status);
                for (OrderHistory orderHistory : ordersHistory) {
                    JLabel lbOrderHistoryId = new JLabel(orderHistory.getId() + "");
                    infoPanel.add(lbOrderHistoryId);
                    lbOrderHistoryId.setPreferredSize(new Dimension(1, 1));
                    JLabel lbOrderHistoryTitle = new JLabel(orderHistory.getTitle());
                    infoPanel.add(lbOrderHistoryTitle);
                    lbOrderHistoryTitle.setPreferredSize(new Dimension(1, 1));
                    JLabel lbUserLogin = new JLabel(orderHistory.getUser().getLogin());
                    infoPanel.add(lbUserLogin);
                    lbUserLogin.setPreferredSize(new Dimension(1, 1));
                    JLabel lbDateCreated = new JLabel(orderHistory.getDateCreated() + "");
                    infoPanel.add(lbDateCreated);
                    lbDateCreated.setPreferredSize(new Dimension(1, 1));
                    JLabel lbTotalSum = new JLabel(orderHistory.getTotalSum() + "");
                    infoPanel.add(lbTotalSum);
                    lbTotalSum.setPreferredSize(new Dimension(1, 1));
                    JLabel lbStatus = new JLabel(orderHistory.getStatus().getName());
                    infoPanel.add(lbStatus);
                    lbStatus.setPreferredSize(new Dimension(1, 1));
                }
                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(700, 300);
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ShowOrdersByUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().removeAll();
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            JPanel controlPanel = new JPanel();
            JTextField tfTitle = new JTextField();
            controlPanel.add(tfTitle);
            tfTitle.setPreferredSize(new Dimension(100, 30));
            JButton btSearch = new JButton("Search");
            controlPanel.add(btSearch);
            JButton btBack = new JButton("Back");
            btSearch.addActionListener((event) -> {
                        try {
                            String userLogin = tfTitle.getText().trim();
                            ArrayList<Order> orders = new ArrayList<>();
                            if (!userLogin.isEmpty()) {
                                toServer.writeObject("getOrderByUser");
                                toServer.writeObject(new UserDto(userLogin));
                                orders = (ArrayList<Order>) fromServer.readObject();
                            }
                            mainPanel.removeAll();
                            mainPanel.add(controlPanel, BorderLayout.NORTH);
                            mainPanel.add(btBack, BorderLayout.SOUTH);
                            setSize(400, 200);
                            add(mainPanel);
                            if (orders.size() != 0) {
                                JPanel infoPanel = new JPanel();
                                int size = 0;
                                for (Order value : orders) {
                                    for (int j = 0; j < value.getInstruments().size(); j++) {
                                        size++;
                                    }
                                }
                                infoPanel.setLayout(new GridLayout(size + 1, 6));
                                JLabel orderTitle = new JLabel("Order title");
                                infoPanel.add(orderTitle);
                                JLabel orderStatus = new JLabel("Order status");
                                infoPanel.add(orderStatus);
                                JLabel isClosed = new JLabel("Is closed");
                                infoPanel.add(isClosed);
                                JLabel instrumentTitle = new JLabel("Instrument title");
                                infoPanel.add(instrumentTitle);
                                JLabel instrumentPrice = new JLabel("Instrument price");
                                infoPanel.add(instrumentPrice);
                                JLabel instrumentQuantity = new JLabel("Instrument quantity");
                                infoPanel.add(instrumentQuantity);
                                for (Order order : orders) {
                                    for (int j = 0; j < order.getInstruments().size(); j++) {
                                        JLabel lbOrderTitle = new JLabel(order.getTitle());
                                        infoPanel.add(lbOrderTitle);
                                        lbOrderTitle.setPreferredSize(new Dimension(1, 1));
                                        JLabel lbOrderStatus = new JLabel(order.getStatus().getName());
                                        infoPanel.add(lbOrderStatus);
                                        lbOrderStatus.setPreferredSize(new Dimension(1, 1));
                                        JLabel lbIsClosed = new JLabel(order.isClosed() + "");
                                        infoPanel.add(lbIsClosed);
                                        lbIsClosed.setPreferredSize(new Dimension(1, 1));
                                        JLabel lbInstrumentTitle = new JLabel(order.getInstruments().get(j).getInstrument().getTitle());
                                        infoPanel.add(lbInstrumentTitle);
                                        lbInstrumentTitle.setPreferredSize(new Dimension(1, 1));
                                        JLabel lbInstrumentPrice = new JLabel(order.getInstruments().get(j).getPrice() + "");
                                        infoPanel.add(lbInstrumentPrice);
                                        lbInstrumentPrice.setPreferredSize(new Dimension(1, 1));
                                        JLabel lbInstrumentQuantity = new JLabel(order.getInstruments().get(j).getQuantity() + "");
                                        infoPanel.add(lbInstrumentQuantity);
                                        lbInstrumentQuantity.setPreferredSize(new Dimension(1, 1));
                                    }
                                }
                                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                                add(mainPanel);
                                setSize(800, 300);
                            } else {
                                mainPanel.removeAll();
                                mainPanel.add(controlPanel, BorderLayout.NORTH);
                                mainPanel.add(btBack, BorderLayout.SOUTH);
                                setSize(400, 200);
                                add(mainPanel);
                            }
                            repaint();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
            );
            btBack.addActionListener((event) -> loggedInUser());
            mainPanel.add(controlPanel, BorderLayout.NORTH);
            mainPanel.add(btBack, BorderLayout.SOUTH);
            add(mainPanel);
            setSize(400, 200);
            repaint();
        }
    }

    private class ShowAllOrdersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("allUserDtos");
                ArrayList<UserDto> users = (ArrayList<UserDto>) fromServer.readObject();
                ArrayList<Order> orders = new ArrayList<>();
                for (UserDto user : users) {
                    toServer.writeObject("getOrderByUser");
                    toServer.writeObject(user);
                    orders.addAll((ArrayList<Order>) fromServer.readObject());
                }
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                int size = 0;
                for (Order value : orders) {
                    for (int j = 0; j < value.getInstruments().size(); j++) {
                        size++;
                    }
                }
                infoPanel.setLayout(new GridLayout(size + 1, 6));
                JLabel orderTitle = new JLabel("Order title");
                infoPanel.add(orderTitle);
                JLabel orderStatus = new JLabel("Order status");
                infoPanel.add(orderStatus);
                JLabel isClosed = new JLabel("Is closed");
                infoPanel.add(isClosed);
                JLabel instrumentTitle = new JLabel("Title");
                infoPanel.add(instrumentTitle);
                JLabel instrumentPrice = new JLabel("Price");
                infoPanel.add(instrumentPrice);
                JLabel instrumentQuantity = new JLabel("Quantity");
                infoPanel.add(instrumentQuantity);
                for (Order order : orders) {
                    for (int j = 0; j < order.getInstruments().size(); j++) {
                        JLabel lbOrderTitle = new JLabel(order.getTitle());
                        infoPanel.add(lbOrderTitle);
                        lbOrderTitle.setPreferredSize(new Dimension(1, 1));
                        JLabel lbOrderStatus = new JLabel(order.getStatus().getName());
                        infoPanel.add(lbOrderStatus);
                        lbOrderStatus.setPreferredSize(new Dimension(1, 1));
                        JLabel lbIsClosed = new JLabel(order.isClosed() + "");
                        infoPanel.add(lbIsClosed);
                        lbIsClosed.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentTitle = new JLabel(order.getInstruments().get(j).getInstrument().getTitle());
                        infoPanel.add(lbInstrumentTitle);
                        lbInstrumentTitle.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentPrice = new JLabel(order.getInstruments().get(j).getPrice() + "");
                        infoPanel.add(lbInstrumentPrice);
                        lbInstrumentPrice.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentQuantity = new JLabel(order.getInstruments().get(j).getQuantity() + "");
                        infoPanel.add(lbInstrumentQuantity);
                        lbInstrumentQuantity.setPreferredSize(new Dimension(1, 1));
                    }
                }
                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(700, 300);
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ChangeStatusOfOrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("allUserDtos");
                ArrayList<UserDto> users = (ArrayList<UserDto>) fromServer.readObject();
                ArrayList<Order> orders = new ArrayList<>();
                for (UserDto user : users) {
                    toServer.writeObject("getOrderByUser");
                    toServer.writeObject(user);
                    orders.addAll((ArrayList<Order>) fromServer.readObject());
                }
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel infoPanel = new JPanel();
                int size = 0;
                for (Order value : orders) {
                    for (int j = 0; j < value.getInstruments().size(); j++) {
                        size++;
                    }
                }
                infoPanel.setLayout(new GridLayout(size + 1, 7));
                JLabel orderTitle = new JLabel("Order title");
                infoPanel.add(orderTitle);
                JLabel orderStatus = new JLabel("Order status");
                infoPanel.add(orderStatus);
                JLabel isClosed = new JLabel("Is closed");
                infoPanel.add(isClosed);
                JLabel instrumentTitle = new JLabel("Instrument title");
                infoPanel.add(instrumentTitle);
                JLabel instrumentPrice = new JLabel("Instrument price");
                infoPanel.add(instrumentPrice);
                JLabel instrumentQuantity = new JLabel("Instrument quantity");
                infoPanel.add(instrumentQuantity);
                JLabel changeStatus = new JLabel("Change status");
                infoPanel.add(changeStatus);
                long i;
                for (Order order : orders) {
                    i = order.getId();
                    for (int j = 0; j < order.getInstruments().size(); j++) {
                        JLabel lbOrderTitle = new JLabel(order.getTitle());
                        infoPanel.add(lbOrderTitle);
                        lbOrderTitle.setPreferredSize(new Dimension(1, 1));
                        JLabel lbOrderStatus = new JLabel(order.getStatus().getName());
                        infoPanel.add(lbOrderStatus);
                        lbOrderStatus.setPreferredSize(new Dimension(1, 1));
                        JLabel lbIsClosed = new JLabel(order.isClosed() + "");
                        infoPanel.add(lbIsClosed);
                        lbIsClosed.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentTitle = new JLabel(order.getInstruments().get(j).getInstrument().getTitle());
                        infoPanel.add(lbInstrumentTitle);
                        lbInstrumentTitle.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentPrice = new JLabel(order.getInstruments().get(j).getPrice() + "");
                        infoPanel.add(lbInstrumentPrice);
                        lbInstrumentPrice.setPreferredSize(new Dimension(1, 1));
                        JLabel lbInstrumentQuantity = new JLabel(order.getInstruments().get(j).getQuantity() + "");
                        infoPanel.add(lbInstrumentQuantity);
                        lbInstrumentQuantity.setPreferredSize(new Dimension(1, 1));
                        JButton btChangeStatus = new JButton("Change status " + i);
                        btChangeStatus.addActionListener((event) -> {
                            try {
                                toServer.writeObject("changeStatusOfOrder");
                                toServer.writeObject(btChangeStatus.getText().split(" ")[2]);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                        infoPanel.add(btChangeStatus);
                    }
                }
                JScrollPane scrollPanel = new JScrollPane(infoPanel);
                mainPanel.add(scrollPanel, BorderLayout.CENTER);
                JButton btBack = new JButton("Back");
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(btBack, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(1100, 300);
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private class ChangeStatusOfInstrumentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                toServer.writeObject("getStatuses");
                ArrayList<Status> statuses = (ArrayList<Status>) fromServer.readObject();
                getContentPane().removeAll();
                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                JPanel controlPanel = new JPanel();
                JTextField tfTitle = new JTextField();
                controlPanel.add(tfTitle);
                tfTitle.setPreferredSize(new Dimension(100, 30));
                JButton btSearch = new JButton("Search");
                controlPanel.add(btSearch);
                JButton btBack = new JButton("Back");
                JPanel actionPanel = new JPanel();
                actionPanel.add(btBack);
                btSearch.addActionListener((event) -> {
                            try {
                                String title;
                                toServer.writeObject("getInstrumentByTitle");
                                if (!tfTitle.getText().trim().isEmpty()) {
                                    title = tfTitle.getText().trim().substring(0, 1).toUpperCase() + tfTitle.getText().trim().substring(1).toLowerCase();
                                } else {
                                    title = "";
                                }
                                toServer.writeObject(title);
                                Instrument instrument = (Instrument) fromServer.readObject();
                                mainPanel.removeAll();
                                mainPanel.add(controlPanel, BorderLayout.NORTH);
                                mainPanel.add(btBack, BorderLayout.SOUTH);
                                setSize(400, 200);
                                add(mainPanel);
                                if (instrument != null) {
                                    JPanel infoPanel = new JPanel();
                                    infoPanel.setLayout(new GridLayout(2, 7));
                                    JLabel lbId = new JLabel("id");
                                    infoPanel.add(lbId);
                                    lbId.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbTitle = new JLabel("title");
                                    infoPanel.add(lbTitle);
                                    lbTitle.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbDescription = new JLabel("description");
                                    infoPanel.add(lbDescription);
                                    lbDescription.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbStatus = new JLabel("status");
                                    infoPanel.add(lbStatus);
                                    lbStatus.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbPrice = new JLabel("price");
                                    infoPanel.add(lbPrice);
                                    lbPrice.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbDateUpdated = new JLabel("dateUpdated");
                                    infoPanel.add(lbDateUpdated);
                                    JLabel lbNewStatus = new JLabel("newStatus");
                                    infoPanel.add(lbNewStatus);
                                    JLabel lbId1 = new JLabel(instrument.getId() + "");
                                    infoPanel.add(lbId1);
                                    lbId1.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbTitle1 = new JLabel(instrument.getTitle());
                                    infoPanel.add(lbTitle1);
                                    lbTitle1.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbDescription1 = new JLabel(instrument.getDescription());
                                    infoPanel.add(lbDescription1);
                                    lbDescription1.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbStatus1 = new JLabel(instrument.getStatus().getName());
                                    infoPanel.add(lbStatus1);
                                    lbStatus1.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbPrice1 = new JLabel(instrument.getPrice() + "");
                                    infoPanel.add(lbPrice1);
                                    lbPrice1.setPreferredSize(new Dimension(1, 1));
                                    JLabel lbDateUpdated1 = new JLabel(instrument.getDateUpdated() + "");
                                    infoPanel.add(lbDateUpdated1);
                                    lbDateUpdated1.setPreferredSize(new Dimension(1, 1));
                                    JComboBox<Status> cbNewStatus = new JComboBox<>(statuses.toArray(new Status[statuses.size()]));
                                    infoPanel.add(cbNewStatus);
                                    JButton btChangeStatus = new JButton("Change status");
                                    btChangeStatus.addActionListener((event1) -> {
                                        try {
                                            toServer.writeObject("changeStatusOfInstrument");
                                            toServer.writeObject(instrument);
                                            toServer.writeObject(cbNewStatus.getSelectedItem());
                                            System.out.println(fromServer.readObject());
                                        } catch (IOException | ClassNotFoundException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    });
                                    actionPanel.removeAll();
                                    actionPanel.add(btBack);
                                    actionPanel.add(btChangeStatus);
                                    mainPanel.add(actionPanel, BorderLayout.SOUTH);
                                    mainPanel.add(infoPanel, BorderLayout.CENTER);
                                    add(mainPanel);
                                    setSize(600, 300);
                                    repaint();
                                } else {
                                    mainPanel.removeAll();
                                    actionPanel.removeAll();
                                    actionPanel.add(btBack);
                                    mainPanel.add(controlPanel, BorderLayout.NORTH);
                                    mainPanel.add(actionPanel, BorderLayout.SOUTH);
                                    setSize(400, 200);
                                    add(mainPanel);
                                    repaint();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                );
                btBack.addActionListener((event) -> loggedInUser());
                mainPanel.add(controlPanel, BorderLayout.NORTH);
                mainPanel.add(actionPanel, BorderLayout.SOUTH);
                add(mainPanel);
                setSize(400, 200);
                repaint();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}