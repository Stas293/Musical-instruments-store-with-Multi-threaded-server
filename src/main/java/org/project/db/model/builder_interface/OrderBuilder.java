package org.project.db.model.builder_interface;

import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;
import org.project.db.model.Status;

import java.util.Date;
import java.util.List;

public interface OrderBuilder {
    OrderBuilder setId(Long id);

    OrderBuilder setDateCreated(Date dateCreated);

    OrderBuilder setLogin(String login);

    OrderBuilder setTitle(String title);

    OrderBuilder setStatus(Status status);

    OrderBuilder setClosed(boolean closed);

    OrderBuilder setInstruments(List<InstrumentOrder> instruments);

    Order createOrder();
}
