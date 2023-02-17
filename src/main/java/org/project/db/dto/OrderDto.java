package org.project.db.dto;

import org.project.db.model.Status;

public record OrderDto(String title, String login, Status status) implements java.io.Serializable {
    @Override
    public String toString() {
        return "OrderDto{" +
                "title='" + title + '\'' +
                ", login='" + login + '\'' +
                ", status=" + status +
                '}';
    }
}
