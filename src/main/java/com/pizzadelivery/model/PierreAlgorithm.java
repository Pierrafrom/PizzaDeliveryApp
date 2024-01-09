package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Collections;

public class PierreAlgorithm {

    // -----------------------------------------------------------------------------------------------------------------
    // Brute force algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    public static ArrayList<Order> bruteForceTime(ArrayList<Order> orders) {
        ArrayList<Order> bestOrderSequence = new ArrayList<>(orders);
        double[] bestTime = new double[] { Double.MAX_VALUE };
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
    //
    // -----------------------------------------------------------------------------------------------------------------
}
