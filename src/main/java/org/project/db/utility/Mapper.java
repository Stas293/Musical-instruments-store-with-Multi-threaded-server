package org.project.db.utility;

import org.project.db.dto.OrderDto;
import org.project.db.dto.RegistrationDto;
import org.project.db.model.InstrumentOrder;
import org.project.db.model.Order;
import org.project.db.model.OrderHistory;
import org.project.db.model.User;

import java.util.List;

public class Mapper {
    private Mapper() {
    }

    public static OrderHistory mapOrderHistory(Order orderToHistory, double totalSum, User user) {
        return OrderHistory.builder()
                .setUser(user)
                .setTotalSum(totalSum)
                .setTitle(orderToHistory.getTitle())
                .setStatus(orderToHistory.getStatus())
                .createOrderHistory();
    }

    public static User mapUserFromRegistration(RegistrationDto registrationDto) {
        return User.builder()
                .setLogin(registrationDto.login())
                .setPassword(registrationDto.password())
                .setFirstName(registrationDto.firstName())
                .setLastName(registrationDto.lastName())
                .setEmail(registrationDto.email())
                .setPhone(registrationDto.phone())
                .createUser();
    }

    public static Order mapOrder(OrderDto orderDto, List<InstrumentOrder> instrumentOrders) {
        return Order.builder()
                .setLogin(orderDto.login())
                .setTitle(orderDto.title())
                .setStatus(orderDto.status())
                .setInstruments(instrumentOrders)
                .createOrder();
    }
}
