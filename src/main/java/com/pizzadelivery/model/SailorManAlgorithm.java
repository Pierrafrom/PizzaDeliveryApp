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
                System.out.println("Dynamic");
                comb1 = SamuelAlgorithm.dynamicDiscount(orders, mandatoryOrder);
                comb2 = PierreAlgorithm.dynamicDistance(orders, mandatoryOrder);
                comb3 = RemiAlgorithm.dynamicTime(orders, mandatoryOrder);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                grade3 = calculateGrade(comb3);
            }
            case GENETIC -> {
                System.out.println("Genetic");
                comb1 = SamuelAlgorithm.geneticTime(orders, 15, 4, mandatoryOrder);
                comb2 = PierreAlgorithm.geneticDiscount(orders, mandatoryOrder);
                //comb3 = RemiAlgorithm.geneticDistance(orders, 15, 4,orderToTake);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
            case GREEDY -> {
                System.out.println("Greedy");
                comb1 = SamuelAlgorithm.greedyDistance(orders, mandatoryOrder);
                comb2 = PierreAlgorithm.greedyTime(orders, mandatoryOrder);
                comb3 = RemiAlgorithm.greedyDiscount(orders, mandatoryOrder);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                grade3 = calculateGrade(comb3);
            }
        }
        ArrayList<Order> maxComb;
        int maxGrade = Math.max(Math.max(grade1, grade2), grade3);

        if ((grade1 == maxGrade && grade2 == maxGrade) || (grade3 == maxGrade && grade1 == maxGrade) || (grade2 == maxGrade && grade3 == maxGrade)) {
            maxComb = getPriorityComb(comb1, comb2, comb3);
        } else if (maxGrade == grade1) {
            maxComb = comb1;
            System.out.println("Samuel's algorithm grade: " + grade1);
        } else if (maxGrade == grade2) {
            maxComb = comb2;
            System.out.println("Pierre's algorithm grade: " + grade2);
        } else {
            maxComb = comb3;
            System.out.println("Remi's algorithm grade: " + grade3);
        }
        return maxComb;
    }

    private static ArrayList<Order> getPriorityComb(ArrayList<Order> comb1, ArrayList<Order> comb2, ArrayList<Order> comb3) {
        int discountsForComb1 = Order.numberOfDiscount(comb1);
        int discountsForComb2 = Order.numberOfDiscount(comb2);
        int discountsForComb3 = Order.numberOfDiscount(comb3);

        if (discountsForComb1 == discountsForComb2) {
            // In case of equality in discounts between comb1 and comb2
            double totalTimeComb1 = Order.totalDeliveryTime(comb1);
            double totalTimeComb2 = Order.totalDeliveryTime(comb2);

            if (totalTimeComb1 <= totalTimeComb2) {
                return comb1;
            } else {
                return comb2;
            }
        } else if (discountsForComb2 == discountsForComb3) {
            // In case of equality in discounts between comb2 and comb3
            double totalTimeComb2 = Order.totalDeliveryTime(comb2);
            double totalTimeComb3 = Order.totalDeliveryTime(comb3);

            if (totalTimeComb2 <= totalTimeComb3) {
                return comb2;
            } else {
                return comb3;
            }
        } else if (discountsForComb1 == discountsForComb3) {
            // In case of equality in discounts between comb1 and comb3
            double totalTimeComb1 = Order.totalDeliveryTime(comb1);
            double totalTimeComb3 = Order.totalDeliveryTime(comb3);

            if (totalTimeComb1 <= totalTimeComb3) {
                return comb1;
            } else {
                return comb3;
            }
        } else {
            // If no equality, return the combination with the maximum discounts
            if (discountsForComb1 >= discountsForComb2 && discountsForComb1 >= discountsForComb3) {
                return comb1;
            } else if (discountsForComb2 >= discountsForComb1 && discountsForComb2 >= discountsForComb3) {
                return comb2;
            } else {
                return comb3;
            }
        }
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
                grade = 10 - i;
            }
        }
        grade += 2 * (5 - totalDiscount);

        return grade;
    }

    public static void sortOrders(ArrayList<Order> orders) {
        System.out.println("Brute force sort");
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria
        // If grades are the same, return the result of DISCOUNT, then TIME, then DISTANCE
        // Sort the original list based on the best criterion (maximum grade)
        // ...
        // Call each BRUTE_FORCE algorithm on a copy of the list to find the best grade among the 3 criteria

        ArrayList<Order> copyOrders = new ArrayList<>(orders);

        ArrayList<Order> comb1 = SamuelAlgorithm.bruteForceDiscount(new ArrayList<>(copyOrders));
        ArrayList<Order> comb2 = PierreAlgorithm.bruteForceTime(new ArrayList<>(copyOrders));
        ArrayList<Order> comb3 = RemiAlgorithm.bruteForceDistance(new ArrayList<>(copyOrders));

        int grade1 = calculateGrade(comb1);
        int grade2 = calculateGrade(comb2);
        int grade3 = calculateGrade(comb3);

        int maxGrade = Math.max(Math.max(grade1, grade2), grade3);

        if ((grade1 == maxGrade && grade2 == maxGrade) || (grade3 == maxGrade && grade1 == maxGrade) || (grade2 == maxGrade && grade3 == maxGrade)) {
            System.out.println("two or more grades are equals to maxGrade");
            orders = getPriorityComb(comb1, comb2, comb3);
        } else if (maxGrade == grade1) {
            orders = comb1;
            System.out.println("Samuel's brute force algorithm grade: " + grade1);
        } else if (maxGrade == grade2) {
            orders = comb2;
            System.out.println("Pierre's brute force algorithm grade: " + grade2);
        } else {
            orders = comb3;
            System.out.println("Remi's brute force algorithm grade: " + grade3);
        }
    }
}
