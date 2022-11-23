package org.project.db.model.builder_interface;

import org.project.db.model.Role;

public interface RoleBuilder {
    RoleBuilder setId(Long id);

    RoleBuilder setCode(String code);

    RoleBuilder setName(String name);

    Role createRole();
}
