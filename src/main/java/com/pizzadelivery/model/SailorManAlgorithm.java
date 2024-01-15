package com.pizzadelivery.model;

import java.util.ArrayList;

/**
 * The SailorManAlgorithm class provides methods for selecting and sorting orders based on different algorithms.
 * It includes brute force, dynamic, genetic, and greedy algorithms for order selection.
 * The class also provides a method to calculate the grade of a given set of orders.
 * <p>
 * This class can call all of our algorithm in our three individual classes:
 * For the moment Remi's algorithm are not used in the whole class because we have some
 * trouble with them.
 * @see SamuelAlgorithm
 * @see PierreAlgorithm
 * @see RemiAlgorithm
 *
 * @author Team
 */
public class SailorManAlgorithm {

    // Enumerations
    public enum AlgorithmType {BRUTE_FORCE, DYNAMIC, GENETIC, GREEDY}

    // Constants
    public static final int GREEDY_SIZE = 50;
    public static final int GENETIC_SIZE = 20;

    /**
     * Selects an algorithm based on the number of orders and applies it to select the optimal combination of orders.
     * The selected algorithm is chosen from BRUTE_FORCE, DYNAMIC, GENETIC, and GREEDY based on the size of the order list.
     *
     * @param orders         The list of orders to select from.
     * @param orderToTake    The order that must be included in the selected combination.
     * @param bestGrade      The best grade of the current list of orders.
     * @return The selected combination of orders based on the chosen algorithm.
     * @throws IllegalArgumentException If the orderToTake is not present in the list of orders.
     */
    public static ArrayList<Order> selectAlgorithm(ArrayList<Order> orders, Order orderToTake, int bestGrade) {
        if (!orders.contains(orderToTake)) {
            throw new IllegalArgumentException("Order to take is not in the list");
        }
        Order mandatoryOrder = orders.remove(orders.indexOf(orderToTake));
        // Selection of the algorithm type based on the number of elements in the list
        AlgorithmType algorithmType;
        if (orders.size() < 5) {
            algorithmType = AlgorithmType.BRUTE_FORCE;
        } else if (orders.size() < GENETIC_SIZE) {
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
        //ArrayList<Order> comb3 = new ArrayList<>();
        switch (algorithmType) {
            case BRUTE_FORCE -> {
                orders.add(mandatoryOrder); // in this case we need to add the mandatory order because our bruteforce algorithm needs it
                System.out.println("Brute force");
                comb1 = SamuelAlgorithm.bruteForceDiscount(new ArrayList<>(orders));
                comb2 = PierreAlgorithm.bruteForceTime(new ArrayList<>(orders));
                //comb3 = RemiAlgorithm.bruteForceDistance(new ArrayList<>(orders));
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
            case DYNAMIC -> {
                System.out.println("Dynamic");
                comb1 = SamuelAlgorithm.dynamicDiscount(new ArrayList<>(orders), mandatoryOrder);
                comb2 = PierreAlgorithm.dynamicDistance(new ArrayList<>(orders), mandatoryOrder);
                //comb3 = RemiAlgorithm.dynamicTime(new ArrayList<>(orders), mandatoryOrder);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
            case GENETIC -> {
                System.out.println("Genetic");
                comb1 = SamuelAlgorithm.geneticTime(new ArrayList<>(orders), 15, 100, mandatoryOrder);
                comb2 = PierreAlgorithm.geneticDiscount(new ArrayList<>(orders), mandatoryOrder, false);
                //comb3 = RemiAlgorithm.geneticDistance(orders, 15, 4,orderToTake);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
            case GREEDY -> {
                System.out.println("Greedy");
                comb1 = SamuelAlgorithm.greedyDistance(new ArrayList<>(orders), mandatoryOrder);
                comb2 = PierreAlgorithm.greedyTime(new ArrayList<>(orders), mandatoryOrder);
                //comb3 = RemiAlgorithm.greedyDiscount(new ArrayList<>(orders), mandatoryOrder);
                grade1 = calculateGrade(comb1);
                grade2 = calculateGrade(comb2);
                //grade3 = calculateGrade(comb3);
            }
        }
        ArrayList<Order> maxComb;
        int maxGrade = Math.max(Math.max(grade1, grade2), grade3);

        if ((grade1 == maxGrade && grade2 == maxGrade) || (grade3 == maxGrade && grade1 == maxGrade) || (grade2 == maxGrade && grade3 == maxGrade)) {
            //maxComb = getPriorityComb(comb1, comb2, comb3);
            maxComb = getPriorityComb(comb1, comb2);
            if (maxComb == comb1) {
                System.out.println("we take Samuel's algorithm, its grade is: " + grade1);
            } else if (maxComb == comb2) {
                System.out.println("we take Pierre's algorithm, its grade is: " + grade2);
            } /*else {
                System.out.println("we take Remi's algorithm, its grade is: " + grade3);
            } */
        } else if (maxGrade == grade1) {
            maxComb = comb1;
            System.out.println("we take Samuel's algorithm, its grade is: " + grade1);
        } else /*if (maxGrade == grade2)*/ {
            maxComb = comb2;
            System.out.println("we take Pierre's algorithm, its grade is: " + grade2);
        }
        /* else {
            maxComb = comb3;
            System.out.println("we take Remi's algorithm, its grade is: " + grade3);
        } */
        return maxComb;
    }

    /**
     * Selects the priority combination among the provided combinations based on discounts, total delivery time, and distance.
     * The method compares the number of discounts in each combination and, in case of a tie, compares the total delivery time.
     * The combination with either the maximum discounts or the lower total delivery time is selected.
     *
     * @param comb1 The first combination of orders to be considered.
     * @param comb2 The second combination of orders to be considered.
     * @return The selected combination with the highest priority based on discounts and total delivery time.
     */
    private static ArrayList<Order> getPriorityComb
            (ArrayList<Order> comb1, ArrayList<Order> comb2/*,ArrayList<Order> comb3*/) {
        int discountsForComb1 = Order.numberOfDiscount(comb1);
        int discountsForComb2 = Order.numberOfDiscount(comb2);
        //int discountsForComb3 = Order.numberOfDiscount(comb3);

        if (discountsForComb1 == discountsForComb2) {
            // In case of equality in discounts between comb1 and comb2
            double totalTimeComb1 = Order.totalDeliveryTime(comb1);
            double totalTimeComb2 = Order.totalDeliveryTime(comb2);

            if (totalTimeComb1 <= totalTimeComb2) {
                return comb1;
            } else {
                return comb2;
            }
        }
        // Here again we have some trouble with Remi's algorithm, so we are excluding it out of the priorityComb

        /* else if (discountsForComb2 == discountsForComb3) {
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
        } */
        else {
            // If no equality, return the combination with the maximum discounts
            if (discountsForComb1 >= discountsForComb2 /*&& discountsForComb1 >= discountsForComb3*/) {
                return comb1;
            } /*else if (discountsForComb2 >= discountsForComb1 && discountsForComb2 >= discountsForComb3) {
                return comb2; *
            } else*/
            {
                //return comb3;
                return comb2;
            }
        }
    }


    /**
     * Calculates the grade of a given combination of orders based on delivery time and the number of discounts.
     *
     * @param orders The list of orders to calculate the grade for.
     * @return The calculated grade for the given combination of orders.
     */
    public static int calculateGrade(ArrayList<Order> orders) {
        int grade = 0;
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

    /**
     * Sorts the given list of orders using a brute force algorithm based on discount, time, and distance.
     * The algorithm selects the combination of orders with the highest grade.
     *
     * @param orders The list of orders to be sorted.
     * @param bestGrade The best grade of the current list of orders.
     */
    public static void sortOrders(ArrayList<Order> orders, int bestGrade) {
        System.out.println("Brute force sort");
        ArrayList<Order> copyOrders = new ArrayList<>(orders);
        ArrayList<Order> comb1 = SamuelAlgorithm.bruteForceDiscount(new ArrayList<>(copyOrders));
        ArrayList<Order> comb2 = PierreAlgorithm.bruteForceTime(new ArrayList<>(copyOrders));
        ArrayList<Order> comb3 = RemiAlgorithm.bruteForceDistance(new ArrayList<>(copyOrders));

        int grade1 = calculateGrade(comb1);
        int grade2 = calculateGrade(comb2);
        int grade3 = calculateGrade(comb3);

        int maxGrade = Math.max(Math.max(grade1, grade2), grade3);

        if(bestGrade < maxGrade) {
            if ((grade1 == maxGrade && grade2 == maxGrade) || (grade3 == maxGrade && grade1 == maxGrade) || (grade2 == maxGrade && grade3 == maxGrade)) {
                System.out.println("two or more grades are equals to maxGrade");
                //orders = getPriorityComb(comb1, comb2, comb3);
                orders = getPriorityComb(comb1, comb2);
            } else if (maxGrade == grade1) {
                orders = comb1;
                System.out.println("Samuel's brute force algorithm grade: " + grade1);
            } else /*if (maxGrade == grade2)*/ {
                orders = comb2;
                System.out.println("Pierre's brute force algorithm grade: " + grade2);
            } /*else {
                    orders = comb3;
                    System.out.println("Remi's brute force algorithm grade: " + grade3);
                } */
        }
    }
}
