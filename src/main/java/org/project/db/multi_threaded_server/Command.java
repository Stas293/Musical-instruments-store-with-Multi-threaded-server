package org.project.db.multi_threaded_server;

import java.io.IOException;

public interface Command {
    void execute() throws IOException, ClassNotFoundException;
}
