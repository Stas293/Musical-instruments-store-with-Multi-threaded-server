package org.project.db.model.builder_interface;

import org.project.db.model.Role;
import org.project.db.model.User;

import java.util.Date;
import java.util.List;

public interface UserBuilder {
    UserBuilder setId(Long id);

    UserBuilder setLogin(String login);

    UserBuilder setFirstName(String firstName);

    UserBuilder setLastName(String lastName);

    UserBuilder setEmail(String email);

    UserBuilder setPhone(String phone);

    UserBuilder setPassword(String password);

    UserBuilder setEnabled(boolean enabled);

    UserBuilder setDateCreated(Date dateCreated);

    UserBuilder setDateUpdated(Date dateUpdated);

    UserBuilder setRoles(List<Role> roles);

    User createUser();
}
