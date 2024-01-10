package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PierreAlgorithm {

    // -----------------------------------------------------------------------------------------------------------------
    // Brute force algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    public static ArrayList<Order> bruteForceTime(ArrayList<Order> orders) {
        ArrayList<Order> bestOrderSequence = new ArrayList<>(orders);
        double[] bestTime = new double[]{Double.MAX_VALUE};
        permute(orders, 0, bestOrderSequence, bestTime);
        return bestOrderSequence;
    }

    private static void permute(ArrayList<Order> orders, int k, ArrayList<Order> bestOrderSequence, double[] bestTime) {
        for (int i = k; i < orders.size(); i++) {
            Collections.swap(orders, i, k);
            permute(orders, k + 1, bestOrderSequence, bestTime);
            Collections.swap(orders, k, i);
        }
        if (k == orders.size() - 1) {
            double currentTime = Order.totalDeliveryTime(orders);
            if (currentTime < bestTime[0]) {
                bestTime[0] = currentTime;
                bestOrderSequence.clear();
                bestOrderSequence.addAll(orders);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // greedy algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------
    public static ArrayList<Order> greedyTime(ArrayList<Order> orders, int mandatoryOrderIndex) {
        if (orders.size() < 5) {
            throw new IllegalArgumentException("The number of orders must be at least 5");
        }
        ArrayList<Order> selectedOrders = new ArrayList<>();
        Order mandatoryOrder = orders.get(mandatoryOrderIndex);
        selectedOrders.add(mandatoryOrder);

        Set<Order> remainingOrders = new HashSet<>(orders);
        remainingOrders.remove(mandatoryOrder);

        for (int i = 0; i < 4; i++) {
            Order closestOrder = findClosestOrder(selectedOrders.get(selectedOrders.size() - 1), remainingOrders);
            if (closestOrder != null) {
                selectedOrders.add(closestOrder);
                remainingOrders.remove(closestOrder);
            }
        }

        return selectedOrders;
    }

    private static Order findClosestOrder(Order referenceOrder, Set<Order> orders) {
        Order closestOrder = null;
        double closestTime = Double.MAX_VALUE;

        for (Order order : orders) {
            double deliveryTime = Order.calculateDeliveryTime(referenceOrder, order);
            if (deliveryTime < closestTime) {
                closestTime = deliveryTime;
                closestOrder = order;
            }
        }
        return closestOrder;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Dynamic algorithm with distance criterion
    // -----------------------------------------------------------------------------------------------------------------
    public static ArrayList<Order> dynamicDistance(ArrayList<Order> orders, Order mandatoryOrder) {

        /*
        on prend en paramètre une liste de commande et une commande obligatoire
        l'objectif est de trouver la liste de commande qui minimise la distance totale de livraison
        on doit selectionner 4 commandes dans la liste et les ajouter a la commande obligatoire
        ensuite on doit
        on doit trouver les solutions pour 1 commande puis 2 commandes puis 3 commandes etc. en étant dynamique et donc en gradnat toute les solutions
         */
        if (orders.size() < 5) {
            throw new IllegalArgumentException("The number of orders must be at least 5");
        }
        return null;
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Genetic algorithm with discount criterion
    // -----------------------------------------------------------------------------------------------------------------
}
