package org.project.db.model.builder;

import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;
import org.project.db.model.Status;
import org.project.db.model.builder_interface.OrderBuilder;

import java.util.Date;
import java.util.List;

public class OrderBuilderImpl implements OrderBuilder {
    private Long id;
    private Date dateCreated;
    private String login;
    private String title;
    private Status status;
    private boolean closed;
    private List<InstrumentOrder> instruments;

    @Override
    public OrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public OrderBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    @Override
    public OrderBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    @Override
    public OrderBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public OrderBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public OrderBuilder setClosed(boolean closed) {
        this.closed = closed;
        return this;
    }

    @Override
    public OrderBuilder setInstruments(List<InstrumentOrder> instruments) {
        this.instruments = instruments;
        return this;
    }

    @Override
    public Order createOrder() {
        return new Order(id, dateCreated, login, title, status, closed, instruments);
    }
}