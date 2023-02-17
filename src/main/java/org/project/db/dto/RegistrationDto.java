package org.project.db.dto;

public record RegistrationDto(String login, String firstName, String lastName, String email, String password,
                              String phone) implements java.io.Serializable {

    @Override
    public String toString() {
        return "RegistrationDto{" +
                "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
