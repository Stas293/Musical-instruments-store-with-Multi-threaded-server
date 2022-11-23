package org.project.db.model.builder_interface;

import org.project.db.model.Status;

public interface StatusBuilder {
    StatusBuilder setId(Long id);

    StatusBuilder setCode(String code);

    StatusBuilder setName(String name);

    StatusBuilder setClosed(boolean closed);

    Status createStatus();
}
