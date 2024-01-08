package com.pizzadelivery.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static int calculateGrade(ArrayList<Order> orders) throws RateLimitExceededException {
        int grade = 0;
        // Calculate the grade for a package of 5 orders
        // 1 point for each time interval (based on constants in Grades)
        // 2 points per order delivered without a discount
        // Return the grade on a scale of 0 to 20
        // ...
        double totalTime = Pizzeria.totalDeliveryTime(orders);
        int totalDiscount = Pizzeria.numberOfDiscount(orders);

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
        grade += 2*(5-totalDiscount);

        return grade;
    }

    public void sortOrders(ArrayList<Order> orders) {
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria
        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        // Sort the original list based on the best criterion (maximum grade)
        // ...

        // Placeholder, implement the logic
    }

    public static void main(String[] args) {
        try {
            // Create some sample orders
            Order order1 = new Order(1, new GPS(48.712, 2.166), LocalDateTime.now());
            Order order2 = new Order(2, new GPS(48.713, 2.167), LocalDateTime.now());
            Order order3 = new Order(3, new GPS(48.714, 2.168), LocalDateTime.now());
            Order order4 = new Order(4, new GPS(48.715, 2.169), LocalDateTime.now());
            Order order5 = new Order(5, new GPS(48.716, 2.170), LocalDateTime.now());

            // Create an ArrayList of orders
            ArrayList<Order> orders = new ArrayList<>(Arrays.asList(order1, order2, order3, order4, order5));

            // Call the calculateGrade method
            int grade = calculateGrade(orders);

            // Print the result
            System.out.println("Grade: " + grade);
        } catch (RateLimitExceededException e) {
            e.printStackTrace();
        }
    }
}
