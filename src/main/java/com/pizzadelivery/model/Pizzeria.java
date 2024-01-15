package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Represents a Pizzeria with a delivery team and a list of orders.
 * The Pizzeria class is responsible for processing and delivering orders.
 *
 * @author Team
 */
public class Pizzeria {
    private final ArrayList<DeliveryPerson> deliveryTeam;
    private final ArrayList<Order> orders;

    // Constants
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294, 2.165678);
    public static final int ORDER_MAX_WAIT = 30;
    public static final int DELIVERY_TEAM_SIZE = 15;
    public static final int TIME_TO_WAIT_BEFORE_REFRESH = 5;

    /**
     * Constructs a new Pizzeria instance.
     * Initializes the orders list and the delivery team.
     */
    public Pizzeria() {
        orders = new ArrayList<>();
        deliveryTeam = new ArrayList<>();
        // Initialize delivery team
        for (int i = 0; i < DELIVERY_TEAM_SIZE; i++) {
            deliveryTeam.add(new DeliveryPerson(i, "DeliveryPerson " + i, "DP"));
        }
    }

    /**
     * Runs the Pizzeria's main process.
     * Continuously processes orders while there are orders in the queue.
     * In general, we use an infinite loop to process orders, but for testing purposes, we stop after a certain number of iterations.
     * This method is synchronized to handle concurrent modifications to the orders list.
     */
    public void run() {
        // Loop to process orders
        while (!orders.isEmpty()) {
            processOrders();
        }
    }

    /**
     * Processes orders by assigning them to available delivery persons.
     * Critical orders are prioritized and processed first.
     */
    public void processOrders() {
        Iterator<Order> iterator = orders.iterator();
        ArrayList<Order> toRemove = new ArrayList<>();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            // Skip already processed orders
            if (toRemove.contains(order)) {
                continue;
            }

            // Handle critical orders
            if (order.isCritical()) {
                System.out.println("Order " + order.id() + " is critical and must be delivered");
                ArrayList<Order> copy = new ArrayList<>(orders);
                copy.removeAll(toRemove);
                int bestGrade = 0;
                ArrayList<Order> ordersToDeliver = SailorManAlgorithm.selectAlgorithm(copy, order, bestGrade);

                SailorManAlgorithm.sortOrders(ordersToDeliver, bestGrade);
                assignOrdersToDeliveryPerson(ordersToDeliver);

                toRemove.addAll(ordersToDeliver);

                System.out.println();
                try {
                    Thread.sleep(TIME_TO_WAIT_BEFORE_REFRESH * 1000);
                } catch (InterruptedException e) {
                    Logger logger = Logger.getLogger(Pizzeria.class.getName());
                    logger.warning(e.getMessage());
                }
            }
        }

        // Remove processed orders
        orders.removeAll(toRemove);

        displayStatus();
    }

    /**
     * Assigns a list of orders to the first available delivery person.
     *
     * @param ordersToDeliver the list of orders to be delivered
     */
    private void assignOrdersToDeliveryPerson(ArrayList<Order> ordersToDeliver) {
        for (DeliveryPerson person : deliveryTeam) {
            // Assign orders to the first available delivery person
            if (person.isAvailable() && !ordersToDeliver.isEmpty()) {
                person.deliverOrders(new ArrayList<>(ordersToDeliver));
                return;
            }
        }
    }

    /**
     * Displays the current status of the orders and the delivery team.
     */
    private void displayStatus() {
        // Display orders and delivery team status
        System.out.println("---------------------- " + orders.size() + " orders left" + " ----------------------");
        for (Order order : orders) {
            System.out.print(order.id() + " ");
        }
        System.out.println("\nDelivery team:");
        for (DeliveryPerson person : deliveryTeam) {
            System.out.println(person.id() + " " + person.isAvailable());
        }
        System.out.println("------------------------------------------------------------");
        try {
            Thread.sleep(TIME_TO_WAIT_BEFORE_REFRESH * 1000);
        } catch (InterruptedException e) {
            Logger logger = Logger.getLogger(Pizzeria.class.getName());
            logger.warning(e.getMessage());
        }
    }


    /**
     * Sets the orders for the Pizzeria.
     * This method is synchronized to handle concurrent modifications to the orders list.
     *
     * @param orders the list of new orders to be added
     */
    public void setOrders(ArrayList<Order> orders) {
        synchronized (this.orders) {
            this.orders.addAll(orders);
        }
    }
}