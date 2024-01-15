package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Remi
 */
public class RemiAlgorithm {
    //////////////////////////////////////////////////////////////////////////////////
/////////////////    BRUTE FORCE PROGRAMMING ALGORITHM ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<ArrayList<Order>> generate_combinations(ArrayList<Order> orders) {
        ArrayList<ArrayList<Order>> results = new ArrayList<>();
        if (orders.isEmpty()) {
            results.add(new ArrayList<>());
            return results;
        }

        for (int i = 0; i < orders.size(); i++) {
            Order current = orders.remove(i);
            ArrayList<ArrayList<Order>> permutations = generate_combinations(orders);
            for (ArrayList<Order> permutation : permutations) {
                permutation.add(0, current);
            }
            results.addAll(permutations);
            orders.add(i, current);
        }
        return results;
    }

    public static ArrayList<Order> bruteForceDistance(ArrayList<Order> orders){
        double min_distance = Double.POSITIVE_INFINITY;
        ArrayList<Order> best_combination = null;
        ArrayList<ArrayList<Order>> ALL_Orders = generate_combinations(orders);
        for (ArrayList<Order> order : ALL_Orders) {
            double total_distance = 0;
            total_distance += Order.totalDeliveryDistance(order);
            if (total_distance < min_distance) {
                min_distance = total_distance;
                best_combination = new ArrayList<>(order);
            }
        }
        return best_combination;
    }
    //////////////////////////////////////////////////////////////////////////////////
/////////////////    DYNAMIC PROGRAMMING ALGORITHM    ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<Order> dynamicTime(ArrayList<Order> allOrders, Order mandatoryOrder) {
        ArrayList<Order> bestCombination = null;
        double minDeliveryTime = Double.MAX_VALUE;

        for (int i = 0; i < allOrders.size(); i++) {
            for (int j = i + 1; j < allOrders.size(); j++) {
                for (int k = j + 1; k < allOrders.size(); k++) {
                    for (int l = k + 1; l < allOrders.size(); l++) {
                        ArrayList<Order> currentCombination = new ArrayList<>();
                        currentCombination.add(mandatoryOrder);
                        currentCombination.add(allOrders.get(i));
                        currentCombination.add(allOrders.get(j));
                        currentCombination.add(allOrders.get(k));
                        currentCombination.add(allOrders.get(l));

                        double currentDeliveryTime = Order.totalDeliveryTime(currentCombination);
                        if (currentDeliveryTime < minDeliveryTime) {
                            minDeliveryTime = currentDeliveryTime;
                            bestCombination = new ArrayList<>(currentCombination);
                        }
                    }
                }
            }
        }

        return bestCombination;
    }
//////////////////////////////////////////////////////////////////////////////////
/////////////////    GENETIC PROGRAMMING ALGORITHM    ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////
/////////////////    GREEDY PROGRAMMING ALGORITHM    ///////////////////////////
//////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<Order> greedyDiscount(ArrayList<Order> allOrders, Order mandatoryOrder) {
        ArrayList<Order> selectedOrders = new ArrayList<>();
        selectedOrders.add(mandatoryOrder);

        while (selectedOrders.size() < 5) {
            Order nextOrder = null;
            int minAdditionalDiscounts = Integer.MAX_VALUE;

            for (Order order : allOrders) {
                if (!selectedOrders.contains(order)) {
                    ArrayList<Order> tempSet = new ArrayList<>(selectedOrders);
                    tempSet.add(order);
                    int additionalDiscounts = Order.numberOfDiscount(tempSet) - Order.numberOfDiscount(selectedOrders);

                    if (additionalDiscounts < minAdditionalDiscounts) {
                        minAdditionalDiscounts = additionalDiscounts;
                        nextOrder = order;
                    }
                }
            }

            if (nextOrder != null) {
                selectedOrders.add(nextOrder);
                allOrders.remove(nextOrder);
            }
        }

        return selectedOrders;
    }
}