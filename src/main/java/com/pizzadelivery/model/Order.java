package com.pizzadelivery.model;

import java.time.LocalDateTime;

public record Order(int id, GPS location, LocalDateTime orderDate) {
    public static double calculateDeliveryTime(Order order1, Order order2) {
        return order1.location().timeTravel(order2.location());
    }
}
