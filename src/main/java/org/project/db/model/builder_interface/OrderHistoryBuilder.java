package org.project.db.model.builder_interface;

import org.project.db.model.OrderHistory;
import org.project.db.model.Status;
import org.project.db.model.User;

import java.util.Date;

public interface OrderHistoryBuilder {
    OrderHistoryBuilder setId(Long id);

    OrderHistoryBuilder setDateCreated(Date dateCreated);

    OrderHistoryBuilder setUser(User user);

    OrderHistoryBuilder setTotalSum(double totalSum);

    OrderHistoryBuilder setTitle(String title);

    OrderHistoryBuilder setStatus(Status status);

    OrderHistory createOrderHistory();
}
