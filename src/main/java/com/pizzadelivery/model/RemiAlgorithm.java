package com.pizzadelivery.model;

import java.util.ArrayList;

public class RemiAlgorithm {
    public static ArrayList<ArrayList<Order>> generate_combinations(ArrayList<Order> arr, int n){
        ArrayList<ArrayList<Order>> result = new ArrayList<>();
        if(n == 0){
            return new ArrayList<>();
        }
        for(int i = 0; i<arr.size();i++){
            Order m = arr.get(i);
            ArrayList<Order> rem_arr = new ArrayList<>();
            for (int j = i + 1; j < arr.size(); j++){
                rem_arr.add(arr.get(j));
            }
            ArrayList<ArrayList<Order>> combinations = generate_combinations(rem_arr, n - 1);

            for (ArrayList<Order> p : combinations) {
                ArrayList<Order> newList = new ArrayList<>();
                newList.add(m);
                newList.addAll(p);
                result.add(newList);
            }
        }
        return result;
    }
    public static ArrayList<Order> brute_force_distance(ArrayList<Order> orders){
        double min_distance = Double.POSITIVE_INFINITY;
        ArrayList<Order> best_combination = null;
        ArrayList<ArrayList<Order>> ALL_Orders = generate_combinations(orders, 5);
        for (ArrayList<Order> allOrder : ALL_Orders) {
            double total_distance = 0;
            for (int j = 0; j < 4; j++) {
                total_distance += Order.calculateDeliveryDistance(allOrder.get(j), allOrder.get(j + 1));
            }
            if (total_distance < min_distance) {
                min_distance = total_distance;
                best_combination = new ArrayList<>(allOrder);
            }
        }
        return best_combination;
    }
}
