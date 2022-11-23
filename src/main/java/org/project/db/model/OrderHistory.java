package org.project.db.model;

import java.util.Date;

public class OrderHistory implements java.io.Serializable {
    private Long id;
    private Date dateCreated;
    private User user;
    private double totalSum;
    private String title;
    private Status status;

    public OrderHistory(Long id, Date dateCreated, User user, double totalSum, String title, Status status) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.user = user;
        this.totalSum = totalSum;
        this.title = title;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDareCreated(Date dareCreated) {
        this.dateCreated = dareCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
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

    @Override
    public String toString() {
        return "OrderHistory{" +
                "id=" + id +
                ", dareCreated=" + dateCreated +
                ", user=" + user +
                ", totalSum=" + totalSum +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }
}
