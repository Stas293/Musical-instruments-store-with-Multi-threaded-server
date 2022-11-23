package org.project.db.dto;

public class UserDto implements java.io.Serializable {
    private final String login;

    public UserDto(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "login='" + login + '\'' +
                '}';
    }
}
