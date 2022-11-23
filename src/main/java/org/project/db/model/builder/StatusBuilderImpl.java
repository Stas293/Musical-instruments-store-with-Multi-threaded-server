package org.project.db.model.builder;

import org.project.db.model.Status;
import org.project.db.model.builder_interface.StatusBuilder;

public class StatusBuilderImpl implements StatusBuilder {
    private Long id;
    private String code;
    private String name;
    private boolean closed;

    @Override
    public StatusBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public StatusBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public StatusBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public StatusBuilder setClosed(boolean closed) {
        this.closed = closed;
        return this;
    }

    @Override
    public Status createStatus() {
        return new Status(id, code, name, closed);
    }
}