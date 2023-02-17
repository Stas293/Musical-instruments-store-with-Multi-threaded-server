package org.project.db.dto;

public record UserDto(String login) implements java.io.Serializable {

    @Override
    public String toString() {
        return "UserDto{" +
                "login='" + login + '\'' +
                '}';
    }
}
