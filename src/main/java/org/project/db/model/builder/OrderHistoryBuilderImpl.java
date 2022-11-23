package org.project.db.model.builder;

import org.project.db.model.OrderHistory;
import org.project.db.model.Status;
import org.project.db.model.User;
import org.project.db.model.builder_interface.OrderHistoryBuilder;

import java.util.Date;

public class OrderHistoryBuilderImpl implements OrderHistoryBuilder {
    private Long id;
    private Date dateCreated;
    private User user;
    private double totalSum;
    private String title;
    private Status status;

    @Override
    public OrderHistoryBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public OrderHistoryBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public OrderHistoryBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public OrderHistoryBuilder setTotalSum(double totalSum) {
        this.totalSum = totalSum;
        return this;
    }

    @Override
    public OrderHistoryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public OrderHistoryBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public OrderHistory createOrderHistory() {
        return new OrderHistory(id, dateCreated, user, totalSum, title, status);
    }
}