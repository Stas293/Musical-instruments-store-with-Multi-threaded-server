package org.project.db.dto;

public record LoginDto(String login, String password) implements java.io.Serializable {
    @Override
    public String toString() {
        return "LoginDto{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
