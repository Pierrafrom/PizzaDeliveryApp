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
    public static ArrayList<Order> dynamicDistance(ArrayList<Order> orders, int mandatoryOrderIndex) {
        if (orders.size() < 5) {
            throw new IllegalArgumentException("The number of orders must be at least 5");
        }
        Order mandatoryOrder = orders.get(mandatoryOrderIndex);
        ArrayList<ArrayList<Order>> allCombinations = generateCombinations(orders, mandatoryOrder);

        double minDistance = Double.MAX_VALUE;
        ArrayList<Order> bestCombination = new ArrayList<>();

        for (ArrayList<Order> combination : allCombinations) {
            // in this method, we use memoization to avoid computing the same distance multiple times
            double distance = Order.totalDeliveryDistance(combination);
            if (distance < minDistance) {
                minDistance = distance;
                bestCombination = new ArrayList<>(combination);
            }
        }

        for (Order order : bestCombination) {
            System.out.println(order);
        }
        System.out.println("Distance: " + minDistance);
        return bestCombination;
    }

    private static ArrayList<ArrayList<Order>> generateCombinations(ArrayList<Order> orders, Order mandatoryOrder) {
        ArrayList<ArrayList<Order>> allCombinations = new ArrayList<>();
        ArrayList<Order> startingCombination = new ArrayList<>();
        startingCombination.add(mandatoryOrder);
        generateCombinationsRecursive(orders, startingCombination, 1, allCombinations);
        return allCombinations;
    }

    private static void generateCombinationsRecursive(ArrayList<Order> orders, ArrayList<Order> current, int count, ArrayList<ArrayList<Order>> allCombinations) {
        if (count == 5) {
            allCombinations.add(new ArrayList<>(current));
            return;
        }

        for (Order order : orders) {
            if (!current.contains(order)) {
                current.add(order);
                generateCombinationsRecursive(orders, current, count + 1, allCombinations);
                current.remove(order);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Genetic algorithm with discount criterion
    // -----------------------------------------------------------------------------------------------------------------
}
