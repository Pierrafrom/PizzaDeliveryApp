package com.pizzadelivery.model;


import com.pizzadelivery.utils.List;

public class Delivery_Person {
    private int id;
    private String name;
    private String firstName;
    private boolean available;
    private List<Order> orders;
    private final static int MAX_ORDERS = 5;

    public Delivery_Person(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.available = true;
        this.orders = new List<Order>(null, null, MAX_ORDERS);
    }
}
