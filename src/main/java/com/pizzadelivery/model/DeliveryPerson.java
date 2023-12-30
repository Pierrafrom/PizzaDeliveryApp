package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPerson {
    public static final int MAX_ORDERS = 5;
    private final int id;
    private final String name;
    private final String firstName;
    private boolean available;
    private List<Order> orders;

    public DeliveryPerson(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.available = true;
        this.orders = new ArrayList<>();
    }

    private void simulateDelivery() {
        //TO DO: implement the method
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static int getMaxOrders() {
        return MAX_ORDERS;
    }

    // Other methods

    public void addOrder(Order order) {
        if (orders.size() < MAX_ORDERS) {
            orders.add(order);
            System.out.println("Commande ajoutée au livreur " + id);
        } else {
            System.out.println("Le livreur " + id + " a atteint le nombre maximum de commandes.");
        }
    }

    public void completeDelivery(Order order) {
        if (orders.contains(order)) {
            orders.remove(order);
            simulateDelivery();
            System.out.println("Livraison complétée pour le livreur " + id);
        } else {
            System.out.println("La commande n'est pas attribuée à ce livreur.");
        }
    }
}
