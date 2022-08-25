package org.project.db.Model;

import java.util.Date;

public class Instrument implements java.io.Serializable {
    private final Long id;
    private Date dateCreated;
    private Date dateUpdated;
    private String description;
    private String title;
    private Status status;
    private double price;

    public Instrument(Long id, Date dateCreated, Date dateUpdated, String description, String title, Status status, double price) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.description = description;
        this.title = title;
        this.status = status;
        this.price = price;
    }

    public Instrument(String description, String title, Status status, double price) {
        this.id = null;
        this.dateCreated = null;
        this.dateUpdated = null;
        this.description = description;
        this.title = title;
        this.status = status;
        this.price = price;
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

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "id=" + id +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
