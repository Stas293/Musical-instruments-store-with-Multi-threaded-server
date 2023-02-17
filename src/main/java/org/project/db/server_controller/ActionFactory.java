package org.project.db.server_controller;

import org.project.db.server_controller.command.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ActionFactory {
    private ActionFactory() {
    }

    public static Command getCommand(CommandAction commandAction, ObjectInputStream inputObjectFromClient, ObjectOutputStream outputObjectToClient) {
        return switch (commandAction) {
            case REGISTER -> new RegisterAction(inputObjectFromClient, outputObjectToClient);
            case LOGIN -> new LoginAction(inputObjectFromClient, outputObjectToClient);
            case ALLUSERDTOS -> new AllUserDtos(outputObjectToClient);
            case REALLYDISABLEUSER -> new ReallyDisableUser(inputObjectFromClient, outputObjectToClient);
            case REALLYENABLEUSER -> new ReallyEnableUser(inputObjectFromClient, outputObjectToClient);
            case ADDROLEFORUSER -> new AddRoleForUser(inputObjectFromClient, outputObjectToClient);
            case ROLEFORUSER -> new RoleForUser(inputObjectFromClient, outputObjectToClient);
            case CHANGEEMAIL -> new ChangeEmail(inputObjectFromClient, outputObjectToClient);
            case CHANGEFIRSTNAME -> new ChangeFirstName(inputObjectFromClient, outputObjectToClient);
            case CHANGELASTNAME -> new ChangeLastName(inputObjectFromClient, outputObjectToClient);
            case CHANGEPHONE -> new ChangePhone(inputObjectFromClient, outputObjectToClient);
            case CHANGEPASSWORD -> new ChangePassword(inputObjectFromClient, outputObjectToClient);
            case GETSTATUSES -> new GetStatuses(outputObjectToClient);
            case GETNUMBEROFINSTRUMENTS -> new GetNumberOfInstruments(outputObjectToClient);
            case ADDINSTRUMENT -> new AddInstrument(inputObjectFromClient, outputObjectToClient);
            case GETALLINSTRUMENTS -> new GetAllInstruments(outputObjectToClient);
            case FINDBYTITLE -> new FindByTitle(inputObjectFromClient, outputObjectToClient);
            case MAKEORDER -> new MakeOrder(inputObjectFromClient, outputObjectToClient);
            case GETALLORDERSFORUSER -> new GetAllOrdersForUser(inputObjectFromClient, outputObjectToClient);
            case GETALLHISTORYORDERSFORUSER -> new GetAllHistoryOrdersForUser(inputObjectFromClient, outputObjectToClient);
            case GETORDERBYUSER -> new GetOrderByUser(inputObjectFromClient, outputObjectToClient);
            case CHANGESTATUSOFORDER -> new ChangeStatusOfOrder(inputObjectFromClient, outputObjectToClient);
            case CHANGESTATUSOFINSTRUMENT -> new ChangeStatusOfInstrument(inputObjectFromClient, outputObjectToClient);

            default -> null;
        };
    }
}
