package org.project.db.model.builder;

import org.project.db.model.Role;
import org.project.db.model.builder_interface.RoleBuilder;

public class RoleBuilderImpl implements RoleBuilder {
    private Long id;
    private String code;
    private String name;

    @Override
    public RoleBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public RoleBuilder setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public RoleBuilder setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Role createRole() {
        return new Role(id, code, name);
    }
}