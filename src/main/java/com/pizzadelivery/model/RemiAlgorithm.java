package com.pizzadelivery.model;

import java.util.ArrayList;

public class RemiAlgorithm {
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

    public static ArrayList<Order> brute_force_distance(ArrayList<Order> orders){
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
}
