package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DeliveryPerson extends Thread {
    private final int id;
    private final String name;
    private final String firstName;
    private final ArrayList<Order> ordersToDeliver;
    private boolean available;

    public DeliveryPerson(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.ordersToDeliver = new ArrayList<>();
        this.available = true;
    }

    public void run() {
        while (true) {
            synchronized (this) {
                while (ordersToDeliver.isEmpty()) {
                    try {
                        wait(); // Attendre une commande
                    } catch (InterruptedException e) {
                        Logger logger = Logger.getLogger(DeliveryPerson.class.getName());
                        logger.warning("Thread interrupted: " + e.getMessage());
                    }
                }

                // Préparer les commandes pour la livraison
                ArrayList<Order> combination = new ArrayList<>(ordersToDeliver);
                ordersToDeliver.clear();
                setAvailable(false);

                // Simuler la livraison
                simulateDelivery(combination);
                setAvailable(true);
            }
        }
    }

    public synchronized void simulateDelivery(ArrayList<Order> combination) {
        try {
            setAvailable(false);
            String orderIds = combination.stream()
                    .map(order -> String.valueOf(order.id()))
                    .collect(Collectors.joining(", "));
            System.out.println("Delivery man #" + id() + " delivering orders: " + orderIds + ". It will take " +
                    Order.totalDeliveryTime(combination) + " minutes");

            // Simuler le temps de livraison
            Thread.sleep((long) Order.totalDeliveryTime(combination) * 1000);
            System.out.println("Delivery completed for delivery man #" + id());
            setAvailable(true);
        } catch (InterruptedException e) {
            Logger logger = Logger.getLogger(DeliveryPerson.class.getName());
            logger.warning("Delivery interrupted");
        }
    }


    public synchronized void setOrdersToDeliver(ArrayList<Order> ordersToDeliver) {
        this.ordersToDeliver.addAll(ordersToDeliver);
    }

    public synchronized void setAvailable(boolean available) {
        this.available = available;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    // Getters pour id, name et firstName
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
