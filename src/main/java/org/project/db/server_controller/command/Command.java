package org.project.db.server_controller.command;

import java.io.IOException;

public interface Command {
    void execute() throws IOException, ClassNotFoundException;
}
