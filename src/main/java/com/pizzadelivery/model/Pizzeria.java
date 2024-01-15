package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class Pizzeria {
    private final ArrayList<DeliveryPerson> deliveryTeam;
    private final ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294, 2.165678);
    public static final int ORDER_MAX_WAIT = 30;
    public static final int DELIVERY_TEAM_SIZE = 15;
    public static final int TIME_TO_WAIT_BEFORE_REFRESH = 15;

    public Pizzeria() {
        orders = new ArrayList<>();
        deliveryTeam = new ArrayList<>();
        for (int i = 0; i < DELIVERY_TEAM_SIZE; i++) {
            deliveryTeam.add(new DeliveryPerson(i, "DeliveryPerson " + i, "DP"));
        }
    }

    public void run() {
        // Lancer les threads des livreurs
        for (DeliveryPerson dp : deliveryTeam) {
            dp.start();
        }

        while (true) {
            synchronized (orders) {
                if (!orders.isEmpty()) {
                    // display orders
                    System.out.println(orders.size() + " orders to deliver: ");
                    for (Order order : orders) {
                        System.out.print(order.id() + " ");
                    }
                    assignOrdersToDeliveryPersons();
                }
            }

            try {
                // Pause avant le prochain cycle
                Thread.sleep(TIME_TO_WAIT_BEFORE_REFRESH * 1000);
            } catch (InterruptedException e) {
                Logger logger = Logger.getLogger(Pizzeria.class.getName());
                logger.warning(e.getMessage());
            }
        }
    }

    private void assignOrdersToDeliveryPersons() {
        // Créer une liste temporaire pour stocker les commandes à retirer
        ArrayList<Order> ordersToRemove = new ArrayList<>();

        for (Order order : orders) {
            if (order.isCritical()) {
                DeliveryPerson dpInCharge = firstAvailableDP();
                if (dpInCharge != null) {
                    ArrayList<Order> combination = new ArrayList<>(
                            SailorManAlgorithm.selectAlgorithm(new ArrayList<>(orders), order)
                    );
                    SailorManAlgorithm.sortOrders(combination);

                    dpInCharge.setOrdersToDeliver(combination);
                    ordersToRemove.addAll(combination);
                }
            }
        }

        orders.removeAll(ordersToRemove);
    }



    private DeliveryPerson firstAvailableDP() {
        for (DeliveryPerson dp : deliveryTeam) {
            if (dp.isAvailable()) {
                return dp;
            }
        }
        System.out.println("No delivery person available, we have to wait");
        return null;
    }

    public void setOrders(ArrayList<Order> orders) {
        synchronized (this.orders) {
            this.orders.addAll(orders);
        }
    }
}
