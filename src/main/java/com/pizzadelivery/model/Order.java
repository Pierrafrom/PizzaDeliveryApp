package com.pizzadelivery.model;

import java.time.LocalDateTime;

public record Order(int id, GPS location, LocalDateTime dateTime) {

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                "location=" + location +
                ", dateTime=" + dateTime +
                '}';
    }
}
