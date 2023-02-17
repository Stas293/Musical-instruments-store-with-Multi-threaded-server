package org.project.db.utility;

import org.project.db.model.Order;
import org.project.db.model.OrderHistory;
import org.project.db.model.User;

public class Mapper {
    public OrderHistory mapOrderHistory(Order orderToHistory, double totalSum, User user) {
        return OrderHistory.builder()
                .setUser(user)
                .setTotalSum(totalSum)
                .setTitle(orderToHistory.getTitle())
                .setStatus(orderToHistory.getStatus())
                .createOrderHistory();
    }
}
