package com.pizzadelivery.model;

import java.time.LocalDateTime;
import java.util.*;
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
    public static ArrayList<ArrayList<Order>> generateCombinations(ArrayList<Order> orders, ArrayList<ArrayList<Order>> allCombinations) {
        generateCombinations(orders, 0, 0, new ArrayList<>(), allCombinations);
        return allCombinations;
    }


    public static ArrayList<Order> bruteForceDiscount(ArrayList<ArrayList<Order>> allCombinations) {
        ArrayList<Order> bestCombination = null;
        int minDiscount = Integer.MAX_VALUE;
        for (ArrayList<Order> currentCombination : allCombinations) {
            int currentDiscount = Order.numberOfDiscount(currentCombination);
            if (currentDiscount < minDiscount) {
                minDiscount = currentDiscount;
                bestCombination = new ArrayList<>(currentCombination);
            }
        }
        return bestCombination;
    }

    public static ArrayList<Order> greedyDistance(ArrayList<Order> orders, Order orderToTake) {
        ArrayList<Order> bestCombination = new ArrayList<>();
        Order previousOrder = new Order(0, Pizzeria.PIZZERIA_LOCATION, LocalDateTime.now());
        Order mandatoryOrder = orders.remove(orders.indexOf(orderToTake));
        while (!orders.isEmpty() && bestCombination.size() < 4) {
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
        bestCombination.add(0, mandatoryOrder);
        return bestCombination;
    }

    public static ArrayList<Order> dynamicDiscount(ArrayList<Order> orders, Order orderToTake) {
        return null;
    }

    public static ArrayList<Order> geneticTime(ArrayList<Order> orders, int populationSize, int generations, Order orderToTake) {
        Order mandatoryOrder = orders.remove(orders.indexOf(orderToTake));
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
        Random random = new Random();
        for (int i = 0; i < populationSize; i++) {
            ArrayList<Order> individual = new ArrayList<>();
            // Add 4 random orders to the individual
            for (int j = 0; j < 4; j++) {
                Order randomOrder = orders.get(random.nextInt(orders.size()));
                if (!individual.contains(randomOrder)) {
                    individual.add(randomOrder);
                }
            }
            population.add(individual);
        }
        return population;
    }



    private static ArrayList<Order> selectParent(ArrayList<ArrayList<Order>> population) {
        population.sort(Comparator.comparingDouble(individual -> {
            try {
                return Order.totalDeliveryTime(individual);
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
        int size = parent1.size();
        ArrayList<Order> child = new ArrayList<>(Collections.nCopies(size, null));

        // Step 1: Select a subset of genes (crossover segment)
        int start = new Random().nextInt(size);
        int end = new Random().nextInt(size);
        if (end < start) {
            int temp = start;
            start = end;
            end = temp;
        }

        // Step 2: Copy the crossover segment from parent1 to the child
        for (int i = start; i <= end; i++) {
            child.set(i, parent1.get(i));
        }

        // Step 3: Fill in the remaining positions with genes from parent2, avoiding duplicates
        int currentIndex = (end + 1) % size;
        for (int i = 0; i < size; i++) {
            if (child.get(currentIndex) == null) {
                Order currentOrder = parent2.get(i);
                if (!child.contains(currentOrder)) {
                    child.set(currentIndex, currentOrder);
                    currentIndex = (currentIndex + 1) % size;
                }
            }
        }

        return child;
    }








    private static void mutate(ArrayList<Order> child) {
        Random random = new Random();

        int size = child.size();

        // Choose two distinct positions in the child
        int position1 = random.nextInt(size);
        int position2;
        do {
            position2 = random.nextInt(size);
        } while (position1 == position2);

        // Swap the orders at the chosen positions
        Order order1 = child.get(position1);
        Order order2 = child.get(position2);

        child.set(position1, order2);
        child.set(position2, order1);
    }

    private static ArrayList<Order> findBestIndividual(ArrayList<ArrayList<Order>> population) {
        double minTime = Integer.MAX_VALUE;
        ArrayList<Order> minArray = new ArrayList<>();
        for (int i = 0; i< population.size(); i++) {
            if (Order.totalDeliveryTime(population.get(i))<minTime) {
                minTime = Order.totalDeliveryTime(population.get(i));
                minArray = population.get(i);
            }
        }
        return minArray;
    }
}
