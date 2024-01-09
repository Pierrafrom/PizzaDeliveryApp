package com.pizzadelivery.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Logger;


public class SamuelAlgorithm {

    public static void generateCombinations(ArrayList<Order> orders, int startIdx, int currentSize,
                                            ArrayList<Order> currentCombination,
                                            ArrayList<ArrayList<Order>> allCombinations) {
        if (currentSize == 5) {
            allCombinations.add(new ArrayList<>(currentCombination));
            return;
        }

        for (int i = startIdx; i < orders.size(); i++) {
            currentCombination.add(orders.get(i));
            generateCombinations(orders, i + 1, currentSize + 1, currentCombination, allCombinations);
            currentCombination.remove(currentCombination.size() - 1);
        }
    }

    // Wrapper method for the initial call
    public static void generateCombinations(ArrayList<Order> orders, ArrayList<ArrayList<Order>> allCombinations) {
        generateCombinations(orders, 0, 0, new ArrayList<>(), allCombinations);
    }


    public static ArrayList<Order> bruteForceDiscount(ArrayList<ArrayList<Order>> allCombinations) {
        ArrayList<Order> bestCombination = null;
        int minDiscount = Integer.MAX_VALUE;
        for (ArrayList<Order> currentCombination : allCombinations) {
            int currentDiscount = Pizzeria.numberOfDiscount(currentCombination);
            if (currentDiscount < minDiscount) {
                minDiscount = currentDiscount;
                bestCombination = new ArrayList<>(currentCombination);
            }
        }
        return bestCombination;
    }

    public static ArrayList<Order> greedyDistance(ArrayList<Order> orders) {
        ArrayList<Order> bestCombination = new ArrayList<>();
        Order previousOrder = new Order(0, Pizzeria.PIZZERIA_LOCATION, LocalDateTime.now());

        while (!orders.isEmpty() && bestCombination.size() < 5) {
            double minDistance = Double.MAX_VALUE;
            int selectedIndex = -1;

            for (int i = 0; i < orders.size(); i++) {
                double distance = previousOrder.location().calculateDistance(orders.get(i).location());
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedIndex = i;
                }
            }

            if (selectedIndex != -1) {
                previousOrder = orders.get(selectedIndex);
                bestCombination.add(previousOrder);
                orders.remove(selectedIndex);
            }
        }

        return bestCombination;
    }

    public static ArrayList<Order> dynamicDiscount(ArrayList<Order> orders) {
        ArrayList<ArrayList<Order>> allCombinations = new ArrayList<>();
        generateCombinations(orders, allCombinations);

        if (allCombinations.isEmpty()) {
            return null;
        }

        int n = allCombinations.size();
        int[] tab = new int[n];
        int[] allDiscounts = new int[n];

        for (int i = 0; i < n; i++) {
            tab[i] = -1; // Initialize tab array with -1 to indicate that the value is not memoized
            allDiscounts[i] = -1; // Initialize allDiscounts array with -1 to indicate that the value is not memoized
        }

        // Populate the allDiscounts array using memoization
        for (int i = 0; i < n; i++) {
            allDiscounts[i] = memoizedCalculateDiscount(allCombinations.get(i), allCombinations, allDiscounts);
        }

        // Continue with the dynamic programming approach
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int discountI = allDiscounts[i];
                int discountJ = allDiscounts[j];

                // Compare the discounts and update tab[i] if the current combination has a lower discount
                if (discountI < discountJ && tab[i] < tab[j] + 1) {
                    tab[i] = tab[j] + 1;
                }
            }
        }

        // Find the maximum length in the tab array, which represents the index of the best combination
        int maxDiscountIndex = 0;
        for (int i = 1; i < n; i++) {
            if (tab[i] > tab[maxDiscountIndex]) {
                maxDiscountIndex = i;
            }
        }

        return allCombinations.get(maxDiscountIndex);
    }

    private static int memoizedCalculateDiscount(ArrayList<Order> orders, ArrayList<ArrayList<Order>> allCombinations,
                                                 int[] allDiscounts) {
        int index = allCombinations.indexOf(orders);
        if (allDiscounts[index] == -1) {
            allDiscounts[index] = Pizzeria.numberOfDiscount(orders);
        }
        return allDiscounts[index];
    }


    public static ArrayList<Order> geneticTime(ArrayList<Order> orders, int populationSize, int generations) {
        ArrayList<ArrayList<Order>> population = generatePopulation(orders, populationSize);

        for (int generation = 0; generation < generations; generation++) {
            ArrayList<ArrayList<Order>> newPopulation = new ArrayList<>();
            for (int i = 0; i < populationSize; i++) {
                ArrayList<Order> parent1 = selectParent(population);
                ArrayList<Order> parent2 = selectParent(population);

                ArrayList<Order> child = crossover(parent1, parent2);
                mutate(child);

                newPopulation.add(child);
            }

            population = newPopulation;
        }

        // Find the best individual in the final population
        return findBestIndividual(population);
    }

    private static ArrayList<ArrayList<Order>> generatePopulation(ArrayList<Order> orders, int populationSize) {
        ArrayList<ArrayList<Order>> population = new ArrayList<>(populationSize);
        ArrayList<Order> individual = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; individual.size() < 5; j++) {
                Order randomOrder = orders.get(random.nextInt(orders.size()) + 1);
                if (!individual.contains(randomOrder)) {
                    individual.add(randomOrder);
                }
            }
            population.add(individual);
            individual.clear();
        }
        return population;
    }


    private static ArrayList<Order> selectParent(ArrayList<ArrayList<Order>> population) {
        population.sort(Comparator.comparingDouble(individual -> {
            try {
                return Pizzeria.totalDeliveryTime(individual);
            } catch (Exception e) {
                Logger logger = Logger.getLogger(SamuelAlgorithm.class.getName());
                logger.warning("Exception occurred: " + e.getMessage());
                throw (e);
            }
        }));
        ArrayList<Order> topPerformer = population.remove(0);
        return new ArrayList<>(topPerformer);
    }

    /**
     * Applies a crossover between two individuals
     * to create a child individual
     *
     * @param parent1 The first parent individual.
     * @param parent2 The second parent individual.
     * @return A new child individual resulting from the crossover operation.
     */
    private static ArrayList<Order> crossover(ArrayList<Order> parent1, ArrayList<Order> parent2) {
        Random random = new Random();
        // Retrieving the size of the individuals
        int size = parent1.size();

        // Randomly selecting a start and end position for crossover
        int start = random.nextInt(size);
        int end = random.nextInt(size);

        if (end < start) {
            int temp = start;
            start = end;
            end = temp;
        }

        // Creating a child with the orders from parent1
        ArrayList<Order> child = new ArrayList<>(Collections.nCopies(size, null));
        for (int i = start; i <= end; i++) {
            child.set(i, parent1.get(i));
        }

        // Filling in the remaining positions in the child with orders from parent2
        int currentIndex = 0;
        for (int i = 0; i < size; i++) {
            // If the current index reaches the start position, moving to the end of the crossover segment
            if (currentIndex == start) {
                currentIndex = end + 1;
            }

            // Retrieving the order from parent2 at the current position
            Order currentOrder = parent2.get(i);

            // If the child is not already containing the order, adding it to the child at the current index
            if (!child.contains(currentOrder)) {
                child.set(currentIndex, currentOrder);
                currentIndex = (currentIndex + 1) % size;
            }
        }

        // Return the resulting child individual after crossover
        return child;
    }

    private static void mutate(ArrayList<Order> child) {
    }

    private static ArrayList<Order> findBestIndividual(ArrayList<ArrayList<Order>> population) {

        return null;
    }
}
