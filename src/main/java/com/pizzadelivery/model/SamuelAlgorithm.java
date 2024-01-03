package com.pizzadelivery.model;

import java.util.ArrayList;

public class SamuelAlgorithm {

    public static void generateCombinations(ArrayList<Order> remainingOrders, ArrayList<Order> currentCombination, ArrayList<ArrayList<Order>> allCombinations) {
        if (currentCombination.size() == 5) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = 0; i < remainingOrders.size(); i++) {
            ArrayList<Order> newRemainingOrders = new ArrayList<>(remainingOrders);
            newRemainingOrders.remove(i);

            ArrayList<Order> newCombination = new ArrayList<>(currentCombination);
            newCombination.add(remainingOrders.get(i));

            generateCombinations(newRemainingOrders, newCombination, allCombinations);
        }
    }

    public static ArrayList<Order> bruteForceDiscount(ArrayList<ArrayList<Order>> allCombinations) throws RateLimitExceededException {
        ArrayList<Order> bestCombination = null;
        int minDiscount = Integer.MAX_VALUE;
        int compteur = 1;
        for (ArrayList<Order> currentCombination : allCombinations) {
            System.out.println("------------------------------appel n°"+compteur++);
            int currentDiscount = Pizzeria.numberOfDiscount(currentCombination);
            System.out.println("------------------------------fin");
            if (currentDiscount < minDiscount) {
                minDiscount = currentDiscount;
                bestCombination = new ArrayList<>(currentCombination);
            }
        }

        return bestCombination;
    }



}
