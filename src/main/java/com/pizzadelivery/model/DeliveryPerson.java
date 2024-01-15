package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DeliveryPerson {
    private final int id;
    private final String name;
    private final String firstName;
    private boolean available;

    public DeliveryPerson(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.available = true;
    }

    public void deliverOrders(ArrayList<Order> ordersToDeliver) {
        setAvailable(false);
        new Thread(() -> {
            try {
                String ordersIds = ordersToDeliver.stream().map(order -> String.valueOf(order.id())).collect(Collectors.joining(", "));
                System.out.println("DeliveryPerson " + id + " is delivering orders " + ordersIds + "\n");
                // Simuler le temps de trajet et de livraison pour chaque commande
                long deliveryTime = (long) Order.totalDeliveryTime(ordersToDeliver);
                Thread.sleep(deliveryTime * 1000); // Convertir en millisecondes
            } catch (InterruptedException e) {
                Logger logger = Logger.getLogger(DeliveryPerson.class.getName());
                logger.warning(e.getMessage());
            } finally {
                setAvailable(true);
            }
        }).start();
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
