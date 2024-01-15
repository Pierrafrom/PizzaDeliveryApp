package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Represents a delivery person in the pizzeria delivery system.
 * This class handles the delivery of orders and manages the availability status of the delivery person.
 */
public class DeliveryPerson {
    private final int id;
    private final String name;
    private final String firstName;
    private boolean available;

    /**
     * Constructs a new DeliveryPerson.
     *
     * @param id        the identifier for the delivery person
     * @param name      the last name of the delivery person
     * @param firstName the first name of the delivery person
     */
    public DeliveryPerson(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.available = true;  // Initially, all delivery persons are available
    }

    /**
     * Handles the delivery of a list of orders.
     * This method simulates the delivery process by running in a separate thread.
     *
     * @param ordersToDeliver the list of orders to be delivered
     */
    public void deliverOrders(ArrayList<Order> ordersToDeliver) {
        setAvailable(false);  // Set the delivery person as unavailable during delivery
        new Thread(() -> {
            try {
                String ordersIds = ordersToDeliver.stream()
                        .map(order -> String.valueOf(order.id()))
                        .collect(Collectors.joining(", "));
                System.out.println("DeliveryPerson " + id + " is delivering orders " + ordersIds + "\n");

                // Simulate the delivery time
                long deliveryTime = (long) Order.totalDeliveryTime(ordersToDeliver);
                Thread.sleep(deliveryTime * 1000);  // Sleep in thread to simulate delivery
            } catch (InterruptedException e) {
                Logger logger = Logger.getLogger(DeliveryPerson.class.getName());
                logger.warning(e.getMessage());
            } finally {
                setAvailable(true);  // Set the delivery person as available after delivery
            }
        }).start();
    }

    /**
     * Sets the availability of the delivery person.
     * This method is synchronized to handle concurrent access.
     *
     * @param available the new availability status
     */
    public synchronized void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns the availability status of the delivery person.
     * This method is synchronized to handle concurrent access.
     *
     * @return the availability status
     */
    public synchronized boolean isAvailable() {
        return available;
    }

    // Getters for id, name, and firstName
    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String firstName() {
        return firstName;
    }
}
