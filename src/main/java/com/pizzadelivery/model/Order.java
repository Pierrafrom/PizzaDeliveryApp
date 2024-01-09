package com.pizzadelivery.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private final int id;
    private final GPS location;
    private LocalDateTime time;

    public Order(int id, GPS location, LocalDateTime time) {
        this.id = id;
        this.location = location;
        this.time = time;
    }

    public static double calculateDeliveryTime(Order order1, Order order2) {
        return order1.location().timeTravel(order2.location());
    }

    public String toString() {
        return "Order " + id() + " at " + location() + " at " + time();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters and setters
    // -----------------------------------------------------------------------------------------------------------------
    public int id() {
        return id;
    }

    public GPS location() {
        return location;
    }

    public LocalDateTime time() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
