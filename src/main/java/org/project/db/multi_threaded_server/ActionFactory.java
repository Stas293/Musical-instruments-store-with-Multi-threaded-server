package org.project.db.multi_threaded_server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ActionFactory {
    public static Command getCommand(CommandAction commandAction, ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        return switch (commandAction) {
            case REGISTER -> new RegisterAction(inputObjectFromClient, outputObjectToClient);
            case LOGIN -> new LoginAction(inputObjectFromClient, outputObjectToClient);

            default -> null;
        };
    }
}
