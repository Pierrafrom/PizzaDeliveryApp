package com.pizzadelivery.model;

import java.util.ArrayList;

public class DeliveryPerson extends Thread {
    public static final int MAX_ORDERS = 5;
    private final int id;
    private final String name;
    private final String firstName;
    private boolean available = true;

    public DeliveryPerson(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
    }

    // Thread's run method
    public void run() {
        while (true) {
            if (available) {
                ArrayList<Order> combination = selectOrdersToDeliver();
                if (!combination.isEmpty()) {
                    simulateDelivery(combination);
                }
            }
            // Optionally, you can add a sleep here to simulate time between checking for orders
        }
    }

    // Method to select orders to deliver
    public synchronized ArrayList<Order> selectOrdersToDeliver() {
        ArrayList<Order> selectedOrders = new ArrayList<>();
        return selectedOrders;
    }

    // Method to simulate order delivery
    public synchronized void simulateDelivery(ArrayList<Order> combination) {
        try {
            setAvailable(false);
            System.out.println("Delivery man #" + getId() + " delivering orders... It will take " +
                    Order.totalDeliveryTime(combination) + " minutes");
            // Simulate delivery time based on the total delivery time of selected orders
            Thread.sleep((long) Order.totalDeliveryTime(combination) * 1000);
            System.out.println("Delivery completed for delivery man #" + getId());
            setAvailable(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Setters and Getters for available
    public synchronized void setAvailable(boolean available) {
        this.available = available;
    }

    public synchronized boolean isAvailable() {
        return available;
    }
}
