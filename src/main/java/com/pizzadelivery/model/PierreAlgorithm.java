package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Collections;

public class PierreAlgorithm {

    // -----------------------------------------------------------------------------------------------------------------
    // BRUTE FORCE ALGORITHM FOR TIME CRITERIA
    // -----------------------------------------------------------------------------------------------------------------

    public static ArrayList<Order> bruteForceTime(ArrayList<Order> orders) {
        ArrayList<Order> bestOrderSequence = new ArrayList<>();
        double bestTime = Double.MAX_VALUE;
        permute(orders, 0, bestOrderSequence, bestTime);
        return bestOrderSequence;
    }

    private static void permute(ArrayList<Order> orders, int k, ArrayList<Order> bestOrderSequence, double bestTime) {
        for (int i = k; i < orders.size(); i++) {
            Collections.swap(orders, i, k);
            permute(orders, k + 1, bestOrderSequence, bestTime);
            Collections.swap(orders, k, i);
        }
        if (k == orders.size() - 1) {
            double currentTime = calculateTotalDeliveryTime(orders);
            if (currentTime < bestTime) {
                bestTime = currentTime;
                bestOrderSequence.clear();
                bestOrderSequence.addAll(orders);
            }
        }
    }

    private static double calculateTotalDeliveryTime(ArrayList<Order> orders) {
        double totalTime = 0;
        for (int i = 0; i < orders.size() - 1; i++) {
            totalTime += Order.calculateDeliveryTime(orders.get(i), orders.get(i + 1));
        }
        return totalTime;
    }


}
