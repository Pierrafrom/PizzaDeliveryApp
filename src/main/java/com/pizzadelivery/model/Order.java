package com.pizzadelivery.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a delivery order with an identifier, location, and timestamp.
 * Provides methods to calculate delivery metrics and check order criticality.
 */
public class Order implements Serializable {
    private final int id;
    private final GPS location;
    private LocalDateTime time;

    /**
     * Constructs an Order with the specified ID, location, and timestamp.
     *
     * @param id       The unique identifier for the order.
     * @param location The GPS location where the order needs to be delivered.
     * @param time     The timestamp when the order was created.
     */
    public Order(int id, GPS location, LocalDateTime time) {
        this.id = id;
        this.location = location;
        this.time = time;
    }

    /**
     * Calculates the delivery time between two orders based on their locations.
     *
     * @param order1 The first order.
     * @param order2 The second order.
     * @return The calculated delivery time in minutes.
     */
    public static double calculateDeliveryTime(Order order1, Order order2) {
        return order1.location().timeTravel(order2.location());
    }

    /**
     * Calculates the number of orders eligible for a discount based on their delivery times.
     *
     * @param orders The list of orders to analyze.
     * @return The count of orders eligible for a discount.
     */
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

    /**
     * Calculates the total delivery time for a list of orders.
     *
     * @param orders The list of orders to calculate the total delivery time.
     * @return The total delivery time in minutes.
     */
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

    /**
     * Calculates the total delivery distance for a list of orders.
     *
     * @param orders The list of orders to calculate the total delivery distance.
     * @return The total delivery distance in units corresponding to GPS coordinates.
     */
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

    /**
     * Checks if the order is critical based on its waiting time and travel time to the pizzeria.
     *
     * @return True if the order is critical, false otherwise.
     */
    public boolean isCritical() {
        Duration duration = Duration.between(time, LocalDateTime.now());
        double waitingTime = duration.toMinutes();
        return waitingTime + Pizzeria.PIZZERIA_LOCATION.timeTravel(location) > 30;
    }

    /**
     * Returns a string representation of the order.
     *
     * @return The string representation of the order.
     */
    public String toString() {
        return "Order " + id() + " at " + location() + " at " + time();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getters and setters
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Gets the unique identifier of the order.
     *
     * @return The order ID.
     */
    public int id() {
        return id;
    }

    /**
     * Gets the GPS location where the order needs to be delivered.
     *
     * @return The order location.
     */
    public GPS location() {
        return location;
    }

    /**
     * Gets the timestamp when the order was created.
     *
     * @return The order timestamp.
     */
    public LocalDateTime time() {
        return time;
    }

    /**
     * Sets the timestamp of the order.
     *
     * @param time The new timestamp for the order.
     */
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
