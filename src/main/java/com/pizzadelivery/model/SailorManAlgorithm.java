package com.pizzadelivery.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SailorManAlgorithm {

    // Enumerations
    public enum Criteria {TIME, DISTANCE, DISCOUNT}

    public enum AlgorithmType {BRUTE_FORCE, DYNAMIC, GENETIC, GREEDY}

    // Constants
    public static final int GREEDY_SIZE = 50;
    public static final int GENETIC_SIZE = 20;

    // Methods

    public static ArrayList<Order> selectAlgorithm(ArrayList<Order> orders, Order orderToTake) {
        Order mandatoryOrder = orders.remove(orders.indexOf(orderToTake));
        // Selection of the algorithm type based on the number of elements in the list
        AlgorithmType algorithmType;
        if (orders.size() < GENETIC_SIZE) {
            algorithmType = AlgorithmType.DYNAMIC;
        } else if (orders.size() < GREEDY_SIZE) {
            algorithmType = AlgorithmType.GENETIC;
        } else {
            algorithmType = AlgorithmType.GREEDY;
        }

        int grade1 = 0;
        int grade2 = 0;
        int grade3 = 0;
        ArrayList<Order> comb1 = new ArrayList<>();
        ArrayList<Order> comb2 = new ArrayList<>();
        ArrayList<Order> comb3 = new ArrayList<>();
        switch(algorithmType) {
            case DYNAMIC -> {
                comb1 = SamuelAlgorithm.dynamicDiscount(orders, mandatoryOrder);
                comb2 = PierreAlgorithm.dynamicDistance(orders, mandatoryOrder);
                //comb3 = RemiAlgorithm.dynamicTime(orders, orderToTake);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
            case GENETIC -> {
                comb1 = SamuelAlgorithm.geneticTime(orders, 15, 4, mandatoryOrder);
                //comb2 = PierreAlgorithm.geneticDiscount(orders, 15, 4,orderToTake);
                //comb3 = RemiAlgorithm.geneticDistance(orders, 15, 4,orderToTake);
                grade1 = calculateGrade(comb1);
                //grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
            case GREEDY -> {
                comb1 = SamuelAlgorithm.greedyDistance(orders, mandatoryOrder);
                comb2 = PierreAlgorithm.greedyTime(orders, 0);// REPLACE 0 BY mandatoryOrder WHEN THE METHOD WILL BE UPDATED
                //comb3 = RemiAlgorithm.greedyDiscount(orders, orderToTake);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
        }
        ArrayList<Order> maxComb;
        int maxGrade = Math.max(Math.max(grade1, grade2), grade3);

        if (maxGrade == grade1) {
            maxComb = comb1;
        } else if (maxGrade == grade2) {
            maxComb = comb2;
        } else {
            maxComb = comb3;
        }
        return maxComb;
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

    public static void sortOrders(ArrayList<Order> orders) {
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria
        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        // Sort the original list based on the best criterion (maximum grade)
        // ...
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria
        ArrayList<Order> copyOrders = new ArrayList<>(orders);

        ArrayList<Order> comb1 = SamuelAlgorithm.bruteForceDiscount(new ArrayList<>(copyOrders));
        ArrayList<Order> comb2 = PierreAlgorithm.bruteForceTime(new ArrayList<>(copyOrders));
        ArrayList<Order> comb3 = new ArrayList<>();//RemiAlgorithm.bruteForceDistance(new ArrayList<>(copyOrders));

        int grade1 = calculateGrade(comb1);
        int grade2 = calculateGrade(comb2);
        int grade3 = 0;// calculateGrade(comb3);

        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        if (grade1 == grade2 && grade2 == grade3) {
            // TO-DO
        } else {
            // Sort the original list based on the best criterion (maximum grade)
            if (grade1 >= grade2 && grade1 >= grade3) {
                orders = new ArrayList<>(comb1);
            } else if (grade2 >= grade1 && grade2 >= grade3) {
                orders = new ArrayList<>(comb2);
            } else {
                orders = new ArrayList<>(comb3);
            }
        }
    }
}
