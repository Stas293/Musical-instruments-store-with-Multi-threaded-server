package org.project.db.multi_threaded_server;

public enum CommandAction {
    REGISTER("register"),
    LOGIN("login");



    private final String action;
    CommandAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
