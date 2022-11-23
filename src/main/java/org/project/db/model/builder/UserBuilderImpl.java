package org.project.db.model.builder;

import org.project.db.model.Role;
import org.project.db.model.User;
import org.project.db.model.builder_interface.UserBuilder;

import java.util.Date;
import java.util.List;

public class UserBuilderImpl implements UserBuilder {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean enabled;
    private Date dateCreated;
    private Date dateUpdated;
    private List<Role> roles;

    @Override
    public UserBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public UserBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    @Override
    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Override
    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public UserBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    @Override
    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public UserBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public UserBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public UserBuilder setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }

    @Override
    public UserBuilder setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public User createUser() {
        return new User(id, login, firstName, lastName, email, phone, password, enabled, dateCreated, dateUpdated, roles);
    }
}