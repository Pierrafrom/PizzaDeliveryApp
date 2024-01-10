package com.pizzadelivery.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;


public class SamuelAlgorithm {
    /*--------------------------------------------------------------------------------------
                                  BRUTE FORCE discount ALGORITHM
      --------------------------------------------------------------------------------------*/

    private static void generateCombinations(ArrayList<Order> orders, int k, ArrayList<Order> bestOrderSequence, int[] leastDiscount) {
        for (int i = k; i < orders.size(); i++) {
            Collections.swap(orders, i, k);
            generateCombinations(orders, k + 1, bestOrderSequence, leastDiscount);
            Collections.swap(orders, k, i);
        }
        if (k == orders.size() - 1) {
            int currentDiscount = Order.numberOfDiscount(orders);
            if (currentDiscount < leastDiscount[0]) {
                leastDiscount[0] = currentDiscount;
                bestOrderSequence.clear();
                bestOrderSequence.addAll(orders);
            }
        }
    }

    public static ArrayList<Order> bruteForceDiscount(ArrayList<Order> allOrders) {
        ArrayList<Order> bestCombination = new ArrayList<>();
        int[] leastDiscount = {Integer.MAX_VALUE};
        generateCombinations(allOrders, 0, bestCombination, leastDiscount);
        return bestCombination;
    }

    /*--------------------------------------------------------------------------------------
                                 GREEDY distance ALGORITHM
      --------------------------------------------------------------------------------------*/
    public static ArrayList<Order> greedyDistance(ArrayList<Order> orders, Order orderToTake) {
        ArrayList<Order> bestCombination = new ArrayList<>();
        Order previousOrder = new Order(0, Pizzeria.PIZZERIA_LOCATION, LocalDateTime.now());
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
        bestCombination.add(0, orderToTake);
        return bestCombination;
    }

    /*--------------------------------------------------------------------------------------
                                 DYNAMIC discount ALGORITHM
      --------------------------------------------------------------------------------------*/

    public static ArrayList<Order> dynamicDiscount(ArrayList<Order> orders, Order orderToTake) {
        // Sort orders based on delivery time from Pizzeria.PIZZERIA_LOCATION in ascending order
        orders.sort(Comparator.comparingDouble(order -> Pizzeria.PIZZERIA_LOCATION.timeTravel(order.location())));

        // Create a list to store combinations of orders with 0 discount tickets
        ArrayList<ArrayList<Order>> discountCombinations = new ArrayList<>();

        // Iterate through each order
        for (int i = 0; i < orders.size(); i++) {
            // Create a combination with the first order
            ArrayList<Order> currentCombination = new ArrayList<>();
            currentCombination.add(orders.get(i));

            // Find additional orders with 0 discount tickets to complete the combination
            for (int j = i + 1; j < orders.size(); j++) {
                if (Order.numberOfDiscount(currentCombination) == 0) {
                    currentCombination.add(orders.get(j));
                    if (currentCombination.size() == 4) {
                        // Store the combination if it has exactly 4 orders
                        discountCombinations.add(new ArrayList<>(currentCombination));
                        break;  // Stop adding more orders once we have a combination of 4 orders
                    }
                }
            }
        }

        // Find the combination with the least total discount
        ArrayList<Order> bestCombination = null;
        int minTotalDiscount = Integer.MAX_VALUE;

        for (ArrayList<Order> combination : discountCombinations) {
            int totalDiscount = Order.numberOfDiscount(combination);
            if (totalDiscount < minTotalDiscount) {
                minTotalDiscount = totalDiscount;
                bestCombination = combination;
            }
        }

        bestCombination.add(0, orderToTake);
        return bestCombination;
    }

    /*--------------------------------------------------------------------------------------
                                        GENETIC time ALGORITHM
      --------------------------------------------------------------------------------------*/

    public static ArrayList<Order> geneticTime(ArrayList<Order> orders, int populationSize, int generations, Order orderToTake) {
        ArrayList<ArrayList<Order>> population = generatePopulation(orders, populationSize);

        for (int generation = 0; generation < generations; generation++) {
            ArrayList<ArrayList<Order>> newPopulation = new ArrayList<>();

            for (int i = 0; i < populationSize; i++) {
                if (population.size() > 1) {
                    ArrayList<Order> parent1 = selectParent(population);
                    ArrayList<Order> parent2 = selectParent(population);

                    ArrayList<Order> child = crossover(parent1, parent2);
                    mutate(child);

                    newPopulation.add(child);
                } else {
                    // Handle the case where population size is 0 or 1
                    // You may choose to add new individuals to the population in this case
                    ArrayList<Order> newIndividual = generateIndividual(orders);
                    newPopulation.add(newIndividual);
                }
            }

            population = newPopulation;
        }

        // Finding the best individual in the final population
        ArrayList<Order> bestIndividual = findBestIndividual(population);
        bestIndividual.add(0, orderToTake);
        return bestIndividual;
    }

    private static ArrayList<Order> generateIndividual(ArrayList<Order> orders) {
        Random random = new Random();
        ArrayList<Order> individual = new ArrayList<>();

        for (int j = 0; j < 4; j++) {
            Order randomOrder = orders.get(random.nextInt(orders.size()));
            if (!individual.contains(randomOrder)) {
                individual.add(randomOrder);
            } else {
                j--;
            }
        }

        return individual;
    }


    private static ArrayList<ArrayList<Order>> generatePopulation(ArrayList<Order> orders, int populationSize) {
        ArrayList<ArrayList<Order>> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            ArrayList<Order> individual = generateIndividual(orders);
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
