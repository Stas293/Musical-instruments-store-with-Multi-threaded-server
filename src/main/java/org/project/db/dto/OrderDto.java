package org.project.db.dto;

import org.project.db.model.Status;

public class OrderDto implements java.io.Serializable {
    private final String title;
    private final String login;
    private final Status status;

    public OrderDto(String title, String login, Status status) {
        this.title = title;
        this.login = login;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getLogin() {
        return login;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "title='" + title + '\'' +
                ", login='" + login + '\'' +
                ", status=" + status +
                '}';
    }
}
