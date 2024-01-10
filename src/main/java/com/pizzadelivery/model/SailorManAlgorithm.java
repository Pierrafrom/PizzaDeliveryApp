package com.pizzadelivery.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class SailorManAlgorithm {

    // Enumerations
    public enum Criteria {TIME, DISTANCE, DISCOUNT}

    public enum AlgorithmType {BRUTE_FORCE, DYNAMIC, GENETIC, GREEDY}

    // Constants
    public static final int GREEDY_SIZE = 50;
    public static final int GENETIC_SIZE = 20;

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

    public static int calculateGrade(ArrayList<Order> orders) {
        int grade = 0;
        // Calculate the grade for a package of 5 orders
        // 1 point for each time interval (based on constants in Grades)
        // 2 points per order delivered without a discount
        // Return the grade on a scale of 0 to 20
        // ...
        double totalTime = Order.totalDeliveryTime(orders);
        int totalDiscount = Order.numberOfDiscount(orders);

        int[] noteMaxTimes = {
                Grades.NOTE_10_MAX_TIME,
                Grades.NOTE_9_MAX_TIME,
                Grades.NOTE_8_MAX_TIME,
                Grades.NOTE_7_MAX_TIME,
                Grades.NOTE_6_MAX_TIME,
                Grades.NOTE_5_MAX_TIME,
                Grades.NOTE_4_MAX_TIME,
                Grades.NOTE_3_MAX_TIME,
                Grades.NOTE_2_MAX_TIME,
                Grades.NOTE_1_MAX_TIME
        };

        for (int i = 0; i < noteMaxTimes.length; i++) {
            if (totalTime >= noteMaxTimes[i]) {
                grade += 10 - i;
                break;
            }
        }
        grade += 2 * (5 - totalDiscount);

        return grade;
    }

    public void sortOrders(ArrayList<Order> orders) {
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria
        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        // Sort the original list based on the best criterion (maximum grade)
        // ...

        // Placeholder, implement the logic
    }
}
