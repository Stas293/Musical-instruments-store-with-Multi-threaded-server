package org.project.db.server_controller;

import java.io.IOException;

public interface Command {
    void execute() throws IOException, ClassNotFoundException;
}
