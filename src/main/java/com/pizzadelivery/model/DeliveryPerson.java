package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPerson {
    public static final int MAX_ORDERS = 5;
    private final int id;
    private final String name;
    private final String firstName;
    private boolean available;
    private ArrayList<Order> orders;

    public DeliveryPerson(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.available = true;
        this.orders = new ArrayList<>();
    }

    public void simulateDelivery() {
        //TO DO: implement the method
        System.out.println("delivery man n°"+getId()+" delivering orders... it will take "+Order.totalDeliveryTime(orders)+" minutes");
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}
