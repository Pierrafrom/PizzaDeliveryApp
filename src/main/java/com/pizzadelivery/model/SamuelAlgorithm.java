package com.pizzadelivery.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;


public class SamuelAlgorithm {
    /*--------------------------------------------------------------------------------------
                                  BRUTE FORCE discount ALGORITHM
      --------------------------------------------------------------------------------------*/

    /**
     * Generates all possible combinations of orders using a recursive permutation approach.
     * The method aims to find the combination with the least discount, updating the result in the provided containers.
     *
     * @param orders            The list of orders to generate combinations from.
     * @param k                 The current index in the recursive permutation process.
     * @param bestOrderSequence A container to store the best combination of orders with the least discount.
     * @param leastDiscount     An array to store the least discount found during the recursive permutations.
     * @implNote The method uses a recursive approach to generate all permutations of orders,
     * swapping elements to explore different combinations. It updates the bestOrderSequence
     * and leastDiscount containers when a combination with a lower discount is found.
     * @see Order#numberOfDiscount(ArrayList)
     */
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


    /**
     * Applies a brute-force approach to find the optimal combination of orders with the least discount.
     * The method generates all possible combinations of orders and selects the one with the minimum discount.
     *
     * @param allOrders The list of orders for which the optimal combination is to be determined.
     * @return An ArrayList containing the best combination of orders with the least discount.
     * @implNote The method utilizes the {@link #generateCombinations(ArrayList, int, ArrayList, int[])} method
     * to explore all possible permutations of orders.
     * @complexity The time complexity of this algorithm is O(n!), where n is the number of orders.
     * This is due to the generation of all possible permutations of orders using a recursive approach.
     * The space complexity is O(n) for the recursive call stack and O(1) for other variables.
     * @see #generateCombinations(ArrayList, int, ArrayList, int[])
     */
    public static ArrayList<Order> bruteForceDiscount(ArrayList<Order> allOrders) {
        // Initialize containers to store the best combination and least discount
        ArrayList<Order> bestCombination = new ArrayList<>();
        int[] leastDiscount = {Integer.MAX_VALUE};

        // Generate all combinations and update the best combination and least discount
        generateCombinations(allOrders, 0, bestCombination, leastDiscount);

        // Return the best combination found
        return bestCombination;
    }



    /*--------------------------------------------------------------------------------------
                                 GREEDY distance ALGORITHM
      --------------------------------------------------------------------------------------*/

    /**
     * Applies a greedy approach to construct a combination of orders with the minimum total distance traveled.
     * The method iteratively selects the order with the nearest location to the previous order.
     *
     * @param orders      The list of orders to be considered for constructing the combination.
     * @param orderToTake The initial order to be included in the combination.
     * @return An ArrayList containing a combination of orders with the least total distance traveled.
     * @implNote The method initializes with an initial order and iteratively selects the order with the nearest
     * location to the previous order until the combination reaches the desired size or there are no more orders.
     * The total distance is minimized during the process.
     * @complexity The time complexity of this algorithm is O(n^2), where n is the number of orders.
     * This is due to the nested loop structure, where for each order in the outer loop, all remaining orders
     * are considered in the inner loop to find the order with the nearest location.
     * The space complexity is O(1) for variables and containers used in the method.
     */
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

    /**
     * Applies a dynamic programming approach to find the optimal combination of orders with the least total discount.
     * The method considers orders with 0 discount tickets and forms combinations based on their delivery times.
     *
     * @param orders      The list of orders for which the optimal combination is to be determined.
     * @param orderToTake The initial order to be included in the combination.
     * @return An ArrayList containing the best combination of orders with the least total discount.
     * @implNote The method first sorts the orders based on their delivery times from Pizzeria.PIZZERIA_LOCATION.
     * It then iterates through each order, creating combinations with the first order and adding additional orders
     * with 0 discount tickets until a combination of exactly 4 orders is formed. Combinations with 0 discount tickets
     * are stored, and the one with the least total discount is returned as the result.
     * @complexity The time complexity of this algorithm is O(n^3), where n is the number of orders.
     * This is due to the nested loops: the outer loop iterates through each order, the middle loop searches for
     * additional orders with 0 discount tickets, and the innermost loop calculates the total discount of each combination.
     * The space complexity is O(n^2) for the discountCombinations list, where each combination can have up to n orders.
     * @see Order#numberOfDiscount(ArrayList)
     * @see Pizzeria#PIZZERIA_LOCATION
     */
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
        ArrayList<Order> bestCombination = new ArrayList<>();
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

    /**
     * Applies a genetic algorithm to find the optimal combination of orders with the minimum total delivery time.
     * The method uses a population of individuals, performs crossover and mutation operations, and selects the best individuals.
     *
     * @param orders         The list of orders for which the optimal combination is to be determined.
     * @param populationSize The size of the population of individuals.
     * @param generations    The number of generations to run the genetic algorithm.
     * @param orderToTake    The initial order to be included in the combination.
     * @return An ArrayList containing the best combination of orders with the least total delivery time.
     * @implNote The method initializes a population, iteratively applies crossover and mutation operations,
     * and selects the best individuals in each generation. The final result is the combination with the least
     * total delivery time.
     * @see #generatePopulation(ArrayList, int)
     * @see #selectParent(ArrayList<ArrayList<Order>>)
     * @see #crossover(ArrayList, ArrayList)
     * @see #mutate(ArrayList)
     * @see #findBestIndividual(ArrayList)
     * @see Order#totalDeliveryTime(ArrayList)
     */
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

    /**
     * Generates an individual for the genetic algorithm by randomly selecting orders from the given list.
     *
     * @param orders The list of orders from which to generate an individual.
     * @return An ArrayList representing an individual with a combination of orders.
     * @implNote The method randomly selects orders from the list, avoiding duplicates, to form an individual.
     */
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

    /**
     * Generates an initial population of individuals for the genetic algorithm.
     *
     * @param orders         The list of orders from which to generate individuals.
     * @param populationSize The size of the population to be generated.
     * @return An ArrayList containing the initial population of individuals.
     * @implNote The method creates a population by generating individuals using the {@link #generateIndividual(ArrayList)} method.
     */
    private static ArrayList<ArrayList<Order>> generatePopulation(ArrayList<Order> orders, int populationSize) {
        ArrayList<ArrayList<Order>> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            ArrayList<Order> individual = generateIndividual(orders);
            population.add(individual);
        }
        return population;
    }

    /**
     * Selects a parent individual from the given population based on their total delivery time.
     *
     * @param population The population of individuals from which to select a parent.
     * @return An ArrayList representing the selected parent individual.
     * @throws RuntimeException if an exception occurs during the total delivery time calculation.
     * @implNote The method sorts the population based on total delivery time and selects the top performer as the parent.
     * @see Order#totalDeliveryTime(ArrayList)
     */
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
     * Applies a crossover operation between two parent individuals to create a child individual.
     *
     * @param parent1 The first parent individual.
     * @param parent2 The second parent individual.
     * @return An ArrayList representing the child individual resulting from the crossover operation.
     * @implNote The method selects a subset of genes from one parent and copies them to the child,
     * filling in the remaining positions with genes from the other parent while avoiding duplicates.
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

    /**
     * Applies a mutation operation to a child individual by swapping two distinct positions.
     *
     * @param child The child individual to be mutated.
     * @implNote The method randomly chooses two distinct positions in the child and swaps the orders at those positions.
     */
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

    /**
     * Finds the best individual with the minimum total delivery time in the given population.
     *
     * @param population The population of individuals from which to find the best individual.
     * @return An ArrayList representing the best individual with the least total delivery time.
     * @implNote The method iterates through the population and selects the individual with the minimum total delivery time.
     * The result is the combination of orders with the least total delivery time.
     * @see Order#totalDeliveryTime(ArrayList)
     */
    private static ArrayList<Order> findBestIndividual(ArrayList<ArrayList<Order>> population) {
        double minTime = Integer.MAX_VALUE;
        ArrayList<Order> minArray = new ArrayList<>();
        for (ArrayList<Order> orders : population) {
            if (Order.totalDeliveryTime(orders) < minTime) {
                minTime = Order.totalDeliveryTime(orders);
                minArray = orders;
            }
        }
        return minArray;
    }
}
