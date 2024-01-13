package com.pizzadelivery.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public static double calculateDeliveryDistance(Order order1, Order order2) {
        return order1.location().calculateDistance(order2.location());
    }

    public static int numberOfDiscount(ArrayList<Order> orders) {
        int discountCount = 0;
        double deliveryTime = Pizzeria.PIZZERIA_LOCATION.timeTravel(orders.get(0).location());
        if (deliveryTime > Pizzeria.ORDER_MAX_WAIT) {
            discountCount++;
        }
        for (int i = 0; i < orders.size() - 1; i++) {
            Order previousOrder = orders.get(i);
            Order currentOrder = orders.get(i + 1);
            deliveryTime = previousOrder.location().timeTravel(currentOrder.location());
            if (deliveryTime >= Pizzeria.ORDER_MAX_WAIT) {
                discountCount++;
            }
        }
        return discountCount;
    }

    public static double totalDeliveryTime(ArrayList<Order> orders) {
        double totalTime = 0;
        double deliveryTime = Pizzeria.PIZZERIA_LOCATION.timeTravel(orders.get(0).location());
        totalTime += deliveryTime;
        for (int i = 0; i < orders.size() - 1; i++) {
            Order previousOrder = orders.get(i);
            Order currentOrder = orders.get(i + 1);
            deliveryTime = previousOrder.location().timeTravel(currentOrder.location());
            totalTime += deliveryTime;
        }
        totalTime += orders.get(orders.size() - 1).location().timeTravel(Pizzeria.PIZZERIA_LOCATION);
        return totalTime;
    }

    public static double totalDeliveryDistance(ArrayList<Order> orders) {
        double totalDistance = 0;
        double deliveryDistance = Pizzeria.PIZZERIA_LOCATION.calculateDistance(orders.get(0).location());
        totalDistance += deliveryDistance;
        for (int i = 0; i < orders.size() - 1; i++) {
            Order previousOrder = orders.get(i);
            Order currentOrder = orders.get(i + 1);
            deliveryDistance = previousOrder.location().calculateDistance(currentOrder.location());
            totalDistance += deliveryDistance;
        }
        totalDistance += orders.get(orders.size() - 1).location().calculateDistance(Pizzeria.PIZZERIA_LOCATION);
        return totalDistance;
    }

    public boolean isCritical () {
        Duration duration = Duration.between(time, LocalDateTime.now());
        double waitingTime = duration.toMinutes();
        return waitingTime+Pizzeria.PIZZERIA_LOCATION.timeTravel(location) > 30;
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