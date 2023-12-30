package com.pizzadelivery.model;

import java.util.ArrayList;

public class SailorManAlgorithm {

    // Enumerations
    public enum Criteria { TIME, DISTANCE, DISCOUNT }
    public enum AlgorithmType { BRUTE_FORCE, DYNAMIC, GENETIC, GREEDY }

    // Constants
    public static final int GREEDY_SIZE = 100;
    public static final int GENETIC_SIZE = 50;

    // Methods

    public ArrayList<Integer> selectAlgorithm(ArrayList<Order> orders, int index) {
        // Selection of the algorithm type based on the number of elements in the list
        AlgorithmType algorithmType;
        if (orders.size() < GENETIC_SIZE) {
            algorithmType = AlgorithmType.DYNAMIC;
        } else if (orders.size() < GREEDY_SIZE) {
            algorithmType = AlgorithmType.GENETIC;
        } else {
            algorithmType = AlgorithmType.GREEDY;
        }

        // Call the selected algorithm with 3 criteria
        // Return the list of indexes of the top 5 orders based on the specified criteria
        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        // ...

        return new ArrayList<>(); // Placeholder, replace with actual result
    }

    public int calculateGrade(ArrayList<Order> orders) {
        // Calculate the grade for a package of 5 orders
        // 1 point for each time interval (based on constants in Grades)
        // 2 points per order delivered without a discount
        // Return the grade on a scale of 0 to 20
        // ...

        return 0; // Placeholder, replace with actual result
    }

    public void sortOrders(ArrayList<Order> orders) {
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria
        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        // Sort the original list based on the best criterion (maximum grade)
        // ...

        // Placeholder, implement the logic
    }
}
