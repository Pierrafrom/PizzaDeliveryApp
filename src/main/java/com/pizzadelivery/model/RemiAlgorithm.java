package com.pizzadelivery.model;

import java.util.ArrayList;

public class RemiAlgorithm {
    public static ArrayList<ArrayList<Order> generate_combinations(ArrayList<Order> arr, int n){
        ArrayList<ArrayList<Order> result = new ArrayList<>();
        if(n == 0){
            ArrayList<ArrayList<Order> nothing = new ArrayList<ArrayList<Order>();
            return nothing;
        }
        for(int i = 0; i<arr.size();i++){
            Order m = arr.get(i);
            ArrayList<ArrayList<Order>> rem_arr = ArrayList<>();
            for (int j = i + 1; j < arr.length; j++){
                rem_arr.add(arr.get(j);
            }
            List<List<Integer>> combinations = generateCombinations(remArr, n - 1);

            for (List<Integer> p : combinations) {
                ArrayList<Order> newList = new ArrayList<>();
                newList.add(m); // 'm' est un entier dans ce contexte
                for(int n = 0; n<p.size();n++){
                    newList.add(p.get(n));
                }
                result.add(newList);
            }
        }
        return result;
    }
    public static ArrayList<Order> brute_force_distance(ArrayList<Order> orders){
        float min_distance = Float.POSITIVE_INFINITY;
        ArrayList<Order> best_combination = new ArrayList<>();
        ArrayList<ArrayList<Order> ALL_Orders = generate_combinations(orders, 5);
        float total_distance = 0;
        for(int i = 0; i<ALL_Order.size(); i++){
            for(int j = 0; j<4;j++){
                total_distance += calculateDeliveryDistance(ALL_Order.get(i).get(j), ALL_Order.get(i).get(j+1));
            }
            if(total_distance < min_distance){
                min_distance = total_distance;
                for(k = 0; k<ALL_Order.get(i).size(); k++){
                    best_combination.add(ALL_Order.get(i).get(k);
                }
            }
        }
        return totalDeliveryDistance(best_combination);
    }
}
