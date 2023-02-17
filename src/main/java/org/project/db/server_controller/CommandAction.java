package org.project.db.server_controller;

public enum CommandAction {
    REGISTER("register"),
    LOGIN("login"),
    ALLUSERDTOS("allUserDtos"),
    REALLYDISABLEUSER("reallyDisableUser"),
    REALLYENABLEUSER("reallyEnableUser"),
    ADDROLEFORUSER("addRoleForUser"),
    ROLEFORUSER("RoleForUser"),
    CHANGEEMAIL("changeEmail"),
    CHANGEFIRSTNAME("changeFirstName"),
    CHANGELASTNAME("changeLastName"),
    CHANGEPHONE("changePhone"),
    CHANGEPASSWORD("changePassword"),
    GETSTATUSES("getStatuses"),
    GETNUMBEROFINSTRUMENTS("getNumberOfInstruments"),
    ADDINSTRUMENT("addInstrument"),
    GETALLINSTRUMENTS("getAllInstruments"),
    FINDBYTITLE("findByTitle"),
    MAKEORDER("makeOrder"),
    GETALLORDERSFORUSER("getAllOrdersForUser"),
    GETALLHISTORYORDERSFORUSER("getAllHistoryOrdersForUser"),
    GETORDERBYUSER("getOrderByUser"),
    CHANGESTATUSOFORDER("changeStatusOfOrder"),
    CHANGESTATUSOFINSTRUMENT("changeStatusOfInstrument"),;

    private final String action;
    CommandAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
