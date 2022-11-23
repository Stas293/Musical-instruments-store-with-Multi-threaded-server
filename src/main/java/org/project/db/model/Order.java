package org.project.db.model;

import java.util.Date;
import java.util.List;

public class Order implements java.io.Serializable {
    private final Long id;
    private Date dateCreated;
    private String login;
    private String title;
    private Status status;
    private boolean closed;
    private List<InstrumentOrder> instruments;

    public Order(Long id, Date dateCreated, String login, String title, Status status, boolean closed,
                 List<InstrumentOrder> instruments) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.login = login;
        this.title = title;
        this.status = status;
        this.closed = closed;
        this.instruments = instruments;
    }

    public Long getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public List<InstrumentOrder> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<InstrumentOrder> instruments) {
        this.instruments = instruments;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", login='" + login + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", closed=" + closed +
                ", instruments=" + instruments +
                '}';
    }
}
