package com.pizzadelivery.model;

import java.util.*;
import java.util.stream.Collectors;

public class PierreAlgorithm {

    // -----------------------------------------------------------------------------------------------------------------
    // Brute force algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    public static ArrayList<Order> bruteForceTime(ArrayList<Order> orders) {
        ArrayList<Order> bestOrderSequence = new ArrayList<>(orders);
        double[] bestTime = new double[]{Double.MAX_VALUE};
        permute(orders, 0, bestOrderSequence, bestTime);
        return bestOrderSequence;
    }

    private static void permute(ArrayList<Order> orders, int k, ArrayList<Order> bestOrderSequence, double[] bestTime) {
        for (int i = k; i < orders.size(); i++) {
            Collections.swap(orders, i, k);
            permute(orders, k + 1, bestOrderSequence, bestTime);
            Collections.swap(orders, k, i);
        }
        if (k == orders.size() - 1) {
            double currentTime = Order.totalDeliveryTime(orders);
            if (currentTime < bestTime[0]) {
                bestTime[0] = currentTime;
                bestOrderSequence.clear();
                bestOrderSequence.addAll(orders);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // greedy algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------
    public static ArrayList<Order> greedyTime(ArrayList<Order> orders, Order mandatoryOrder) {
        if (orders.size() < 4) {
            throw new IllegalArgumentException("The number of orders must be at least 4");
        }
        ArrayList<Order> selectedOrders = new ArrayList<>();
        selectedOrders.add(mandatoryOrder);

        Set<Order> remainingOrders = new HashSet<>(orders);
        remainingOrders.remove(mandatoryOrder);

        for (int i = 0; i < 4; i++) {
            Order closestOrder = findClosestOrder(selectedOrders.get(selectedOrders.size() - 1), remainingOrders);
            if (closestOrder != null) {
                selectedOrders.add(closestOrder);
                remainingOrders.remove(closestOrder);
            }
        }

        return selectedOrders;
    }

    private static Order findClosestOrder(Order referenceOrder, Set<Order> orders) {
        Order closestOrder = null;
        double closestTime = Double.MAX_VALUE;

        for (Order order : orders) {
            double deliveryTime = Order.calculateDeliveryTime(referenceOrder, order);
            if (deliveryTime < closestTime) {
                closestTime = deliveryTime;
                closestOrder = order;
            }
        }
        return closestOrder;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Dynamic algorithm with distance criterion
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Calculates the optimal sequence of orders based on delivery distance using a dynamic programming approach.
     * This method is designed to find a sequence of orders that minimizes the total delivery distance,
     * ensuring that a specific mandatory order is included in the resulting path. The algorithm is tailored
     * for scenarios where the number of possible orders is manageable, and precise optimization is required.
     *
     * <p>The algorithm works by building a memoization table that keeps track of the minimum distance
     * for each subset of orders, progressively increasing the size of these subsets. For each subset,
     * it calculates the minimum distance by considering the last order in the set and adding its distance
     * from the remaining set. This approach ensures that all possible combinations of orders are considered,
     * and the best sequence is chosen.</p>
     *
     * <p><b>Advantages:</b>
     * <ul>
     * <li>Ensures inclusion of a mandatory order in the optimal path.</li>
     * <li>Provides precise optimization for a small set of orders.</li>
     * <li>Utilizes dynamic programming for efficient computation, reducing redundant calculations.</li>
     * </ul>
     * </p>
     *
     * <p><b>Disadvantages:</b>
     * <ul>
     * <li>Not scalable for a large number of orders due to exponential growth in computational complexity.</li>
     * </ul>
     * </p>
     *
     * <p><b>Complexity Analysis:</b>
     * The time complexity is O(n^2 * 2^n), where n is the number of orders. This is due to the need to
     * examine each subset of orders and calculate distances for each pair within these subsets.</p>
     *
     * @param orders         An ArrayList of {@code Order} objects to choose from. The list should contain at least 5
     *                       orders.
     * @param mandatoryOrder An {@code Order} object that must be included in the result. This order is a critical part
     *                       of the path optimization.
     * @return An ArrayList of {@code Order} objects representing the optimal path. The sequence of these orders
     * represents the path with the minimum total delivery distance, including the mandatory order.
     * @throws IllegalArgumentException If the number of orders is less than 5. This exception ensures that the method
     *                                  has a sufficient number of orders to perform the optimization.
     */
    public static ArrayList<Order> dynamicDistance(ArrayList<Order> orders, Order mandatoryOrder) {
        // Validate that the order list has the minimum required size
        validateOrderListSize(orders);

        // Initialize a memoization table to store minimum distances for subsets of orders
        Map<Set<Order>, Double> memoTable = new HashMap<>();
        // Initialize a map to track the last order in each subset for path construction
        Map<Set<Order>, Order> lastOrderInSet = new HashMap<>();
        // Set up the memoization table with the mandatory order as the starting point
        initializeMemoTable(mandatoryOrder, memoTable, lastOrderInSet);

        // Iterate over subsets of increasing sizes (up to 5) to compute distances
        for (int size = 2; size <= 5; size++) {
            // Compute the minimum distances for all subsets of the current size
            computeDistancesForSubsets(orders, size, mandatoryOrder, memoTable, lastOrderInSet);
        }

        // Construct and return the optimal path of orders based on the computed distances
        return buildOptimalPath(memoTable, lastOrderInSet, mandatoryOrder);
    }

    /**
     * Validates that the list of orders meets the minimum size requirement.
     * This function is an auxiliary part of the dynamic distance algorithm, ensuring that the input
     * list of orders is large enough for the algorithm to work effectively. The algorithm requires
     * a minimum of four orders to operate correctly. If the size requirement is not met, an
     * {@code IllegalArgumentException} is thrown, indicating improper usage of the main algorithm.
     *
     * @param orders The ArrayList of {@code Order} objects to be validated.
     * @throws IllegalArgumentException if the number of orders in the list is less than the required minimum (5).
     */
    private static void validateOrderListSize(ArrayList<Order> orders) {
        // Check if the size of the order list is less than the minimum required size
        if (orders.size() < 4) {
            throw new IllegalArgumentException("Number of orders must be at least 4");
        }
    }

    /**
     * Initializes the memoization tables for the dynamic distance algorithm.
     * This method sets up the foundational data structures used in the dynamic programming approach
     * by initializing the memoization table and the last order in set map. It starts by adding
     * the mandatory order with a base distance of 0.0, ensuring that this order is always included
     * in the computed paths.
     *
     * <p>This is a crucial step in the dynamic distance algorithm, as it provides the starting point
     * for building up the subsets of orders and their associated minimum distances.</p>
     *
     * @param mandatoryOrder The mandatory {@code Order} object that must be included in all paths.
     * @param memoTable      A map representing the memoization table, mapping sets of {@code Order} objects
     *                       to their corresponding minimum delivery distance.
     * @param lastOrderInSet A map to keep track of the last order in each set of orders for path reconstruction.
     */
    private static void initializeMemoTable(Order mandatoryOrder, Map<Set<Order>, Double> memoTable, Map<Set<Order>,
            Order> lastOrderInSet) {
        // Set initial conditions for the dynamic programming algorithm with the mandatory order
        Set<Order> initialSet = new HashSet<>();
        initialSet.add(mandatoryOrder);
        memoTable.put(initialSet, 0.0);  // Initializing distance for the mandatory order
        lastOrderInSet.put(initialSet, mandatoryOrder);  // Setting the mandatory order as the starting point
    }

    /**
     * Computes the minimum delivery distances for all subsets of a given size.
     * This method is an integral part of the dynamic distance algorithm. It iterates over each subset
     * of orders of a specified size, calculating the minimum delivery distance for that subset. This
     * is done by considering each order as a potential last order in the subset and calculating the
     * total distance to reach that order from the rest of the subset.
     *
     * <p>The method updates the memoization table and the last order in set map for each subset.
     * It ensures that the mandatory order is always included in the subsets considered.</p>
     *
     * @param orders         The complete list of {@code Order} objects to consider.
     * @param size           The size of the subsets to be considered.
     * @param mandatoryOrder The mandatory {@code Order} object that must be included in each subset.
     * @param memoTable      The memoization table, mapping sets of {@code Order} objects to their
     *                       corresponding minimum delivery distance.
     * @param lastOrderInSet A map to keep track of the last order in each subset for path reconstruction.
     */
    private static void computeDistancesForSubsets(ArrayList<Order> orders, int size, Order mandatoryOrder,
                                                   Map<Set<Order>, Double> memoTable, Map<Set<Order>, Order> lastOrderInSet) {
        // Iterate over all subsets of a given size, updating the memoTable and lastOrderInSet
        for (Set<Order> subset : allSubsetsOfSize(orders, size, mandatoryOrder)) {
            double minDistance = Double.MAX_VALUE;  // Initialize minDistance to maximum value
            Order lastOrder = null;                 // Initialize lastOrder to null

            // Iterate over each order in the subset to find the minimum distance
            for (Order order : subset) {
                // Skip the iteration if it's the mandatory order and not the only order in the subset
                if (order.equals(mandatoryOrder) && subset.size() != 1) {
                    continue;
                }

                // Create a new subset excluding the current order to find the remaining distance
                Set<Order> remaining = new HashSet<>(subset);
                remaining.remove(order);
                // Calculate the distance to the current order from the remaining subset
                double distance = memoTable.get(remaining) +
                        Order.calculateDeliveryDistance(lastOrderInSet.get(remaining), order);


                // Update minDistance and lastOrder if a shorter path is found
                if (distance < minDistance) {
                    minDistance = distance;
                    lastOrder = order;
                }
            }

            // Update the memoTable and lastOrderInSet with the minimum distance and corresponding last order for this subset
            memoTable.put(subset, minDistance);
            lastOrderInSet.put(subset, lastOrder);
        }
    }

    /**
     * Builds the optimal path of orders based on the computed memoization data.
     * This method reconstructs the optimal sequence of orders from the memoization table and
     * last order in set map. It starts with the optimal set of orders that includes the mandatory
     * order and has the minimum delivery distance, then traces back to build the complete path.
     *
     * <p>The method iterates backwards through the sets of orders, selecting the last order in
     * each set and removing it to find the next set, until all orders are included in the path.
     * The result is a list of orders that represents the sequence with the minimum total delivery
     * distance while including the mandatory order.</p>
     *
     * @param memoTable      The memoization table, mapping sets of {@code Order} objects to their
     *                       corresponding minimum delivery distance.
     * @param lastOrderInSet A map to keep track of the last order in each subset for path reconstruction.
     * @param mandatoryOrder The mandatory {@code Order} object that must be included in the optimal path.
     * @return An ArrayList of {@code Order} objects representing the optimal path.
     */
    private static ArrayList<Order> buildOptimalPath(Map<Set<Order>, Double> memoTable, Map<Set<Order>,
            Order> lastOrderInSet, Order mandatoryOrder) {
        // Find the optimal set of orders including the mandatory order with the minimum distance
        Set<Order> currentSet = findOptimalSet(memoTable, mandatoryOrder);
        ArrayList<Order> path = new ArrayList<>();

        // Iteratively build the path by tracing back through the sets in the memoTable
        while (currentSet != null && !currentSet.isEmpty()) {
            // Retrieve and add the last order of the current set to the path
            Order last = lastOrderInSet.get(currentSet);
            path.add(last);
            // Remove the last order to move to the next subset in the sequence
            currentSet.remove(last);
        }
        // Reverse the path to get the correct order sequence from start to end
        Collections.reverse(path);
        return path; // Return the constructed optimal path
    }

    /**
     * Generates all subsets of a specified size from the list of orders, ensuring each subset includes the mandatory order.
     * This method is a key component of the dynamic distance algorithm, used for generating every possible combination
     * of orders of a given size that includes the mandatory order. It leverages a recursive helper method
     * {@code generateSubsets} to build these subsets.
     *
     * <p>This method is crucial for the dynamic programming approach, as it provides the set of all potential
     * subsets that are evaluated to find the optimal delivery path.</p>
     *
     * @param orders         The complete list of {@code Order} objects to generate subsets from.
     * @param size           The size of the subsets to be generated.
     * @param mandatoryOrder The mandatory {@code Order} object that must be included in each subset.
     * @return A Set containing Sets of {@code Order} objects, each representing a possible subset of orders of the
     * specified size.
     */
    private static Set<Set<Order>> allSubsetsOfSize(ArrayList<Order> orders, int size, Order mandatoryOrder) {
        // Initialize a set to hold all subsets of the given size including the mandatory order
        Set<Set<Order>> allSubsets = new HashSet<>();
        // Start the recursive process to generate subsets, including the mandatory order in each
        generateSubsets(orders, size, 0, new HashSet<>(Collections.singletonList(mandatoryOrder)), allSubsets,
                mandatoryOrder);
        return allSubsets;  // Return the set of all generated subsets
    }

    /**
     * Recursively generates all subsets of orders of a specified size, including the mandatory order.
     * This auxiliary method is used by {@code allSubsetsOfSize} to create subsets of orders. It employs
     * a recursive approach to generate combinations of orders, ensuring that each combination
     * includes the mandatory order. The generated subsets are stored in a set of sets.
     *
     * <p>This method is a fundamental part of the subset generation process in the dynamic distance
     * algorithm. It ensures that all potential subsets that could form part of the optimal path
     * are considered.</p>
     *
     * @param orders         The complete list of {@code Order} objects to generate subsets from.
     * @param size           The size of the subsets to be generated.
     * @param start          The starting index for generating subsets in the current recursive call.
     * @param current        The current subset being generated.
     * @param allSubsets     A set to collect all the generated subsets.
     * @param mandatoryOrder The mandatory {@code Order} object that must be included in each subset.
     */
    private static void generateSubsets(ArrayList<Order> orders, int size, int start, Set<Order> current,
                                        Set<Set<Order>> allSubsets, Order mandatoryOrder) {
        // Base case: if the current subset reaches the desired size and contains the mandatory order, add it to allSubsets
        if (current.size() == size) {
            if (current.contains(mandatoryOrder)) {
                allSubsets.add(new HashSet<>(current));
            }
            return;
        }

        // Recursively generate subsets by adding each order and then backtracking
        for (int i = start; i < orders.size(); i++) {
            // Add the next order to the current subset
            current.add(orders.get(i));
            // Recursive call to generate further subsets
            generateSubsets(orders, size, i + 1, current, allSubsets, mandatoryOrder);
            // Backtrack by removing the last added order
            current.remove(orders.get(i));
        }
    }

    /**
     * Identifies the optimal set of orders that includes the mandatory order and has the minimum delivery distance.
     * This method scans the memoization table to find the subset of orders that constitutes the optimal path.
     * The optimal path is defined as the set of orders, including the mandatory order, that results in the minimum
     * total delivery distance.
     *
     * <p>This method is an essential component of the dynamic distance algorithm, as it determines the
     * most efficient sequence of orders that must be followed.</p>
     *
     * @param memoTable      The memoization table, mapping sets of {@code Order} objects to their
     *                       corresponding minimum delivery distance.
     * @param mandatoryOrder The mandatory {@code Order} object that must be included in the optimal set.
     * @return The optimal set of {@code Order} objects, or {@code null} if no such set is found.
     */
    private static Set<Order> findOptimalSet(Map<Set<Order>, Double> memoTable, Order mandatoryOrder) {
        // Initialize variables to track the optimal set and its minimum distance
        Set<Order> optimalSet = null;
        double minDistance = Double.MAX_VALUE;

        // Iterate over each subset in the memoTable to find the one with the minimum distance
        for (Set<Order> subset : memoTable.keySet()) {
            // Check if the subset is of the desired size and contains the mandatory order
            if (subset.size() == 5 && subset.contains(mandatoryOrder)) {
                double distance = memoTable.get(subset);  // Get the distance for the current subset
                // Update the optimal set if the current distance is less than the minimum found so far
                if (distance < minDistance) {
                    minDistance = distance;
                    optimalSet = subset;
                }
            }
        }
        return optimalSet;  // Return the found optimal set of orders
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Genetic algorithm with discount criterion
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Optimizes the sequence of orders based on discounts and delivery time using a genetic algorithm.
     * This method applies a genetic algorithm approach to find an optimal arrangement of orders that
     * maximizes discounts while considering delivery time. The algorithm starts with a population of
     * potential solutions and evolves them over generations.
     *
     * <p>The genetic algorithm includes selection, crossover, and mutation processes to generate new
     * populations. Each individual in the population is a possible sequence of orders, and the fitness
     * of each individual is assessed based on the number of discounts and total delivery time.</p>
     *
     * <p><b>Advantages:</b>
     * <ul>
     * <li>Capable of finding near-optimal solutions in complex optimization scenarios.</li>
     * <li>Effective in scenarios where multiple objectives (like discounts and delivery time) need to be balanced.</li>
     * <li>Adaptable to changes in the problem parameters or objectives.</li>
     * </ul>
     * </p>
     *
     * <p><b>Disadvantages:</b>
     * <ul>
     * <li>May not always find the absolute best solution due to its heuristic nature.</li>
     * <li>Requires careful tuning of parameters like population size and mutation rate.</li>
     * <li>Computationally intensive, especially for large populations or a high number of generations.</li>
     * </ul>
     * </p>
     *
     * <p><b>Complexity Analysis:</b>
     * The complexity is O(MAX_GENERATION * POPULATION_SIZE * individualSize), assuming the cost of
     * selection, crossover, and mutation processes are linear with respect to the population size
     * and individual size.</p>
     *
     * @param orders         An ArrayList of {@code Order} objects representing the available orders.
     * @param mandatoryOrder An {@code Order} object that must be included in each solution.
     * @return An ArrayList of {@code Order} objects representing the optimized order sequence.
     * @throws IllegalArgumentException If the number of orders is less than the minimum required (4 in this case).
     */
    public static ArrayList<Order> geneticDiscount(ArrayList<Order> orders, Order mandatoryOrder) {
        if (orders.size() < 4) {
            throw new IllegalArgumentException("The number of orders must be at least 4");
        }
        final int INDIVIDUAL_SIZE = 5;
        final int POPULATION_SIZE = 10;
        final int MAX_GENERATION = 100;
        ArrayList<ArrayList<Order>> population = generatePopulation(orders, mandatoryOrder, POPULATION_SIZE, INDIVIDUAL_SIZE);
        sortByDiscountNumber(population);
        int generation = 1;
        while (generation < MAX_GENERATION) {
            //displayPopulation(population, "Generation " + generation);
            population = generateNewPopulation(population, orders, mandatoryOrder, POPULATION_SIZE, INDIVIDUAL_SIZE);
            generation++;
        }
        //System.out.println(population.get(0));
        return population.get(0);
    }

    private static ArrayList<ArrayList<Order>> generateNewPopulation(ArrayList<ArrayList<Order>> population,
                                                                     ArrayList<Order> orders,
                                                                     Order mandatoryOrder,
                                                                     int populationSize, int individualSize) {
        ArrayList<ArrayList<Order>> newPopulation = selection(population);
        for (ArrayList<Order> individual : newPopulation) {
            mutate(individual);
        }
        while (newPopulation.size() < populationSize) {
            newPopulation.add(generateIndividual(orders, mandatoryOrder, individualSize));
        }
        sortByDiscountNumber(newPopulation);
        return newPopulation;

    }

    private static void mutate(ArrayList<Order> individual) {
        Random rand = new Random();
        int index1 = 1 + rand.nextInt(individual.size() - 1);
        int index2 = 1 + rand.nextInt(individual.size() - 1);

        while (index1 == index2) {
            index2 = 1 + rand.nextInt(individual.size() - 1);
        }

        Order temp = individual.get(index1);
        individual.set(index1, individual.get(index2));
        individual.set(index2, temp);
    }

    private static ArrayList<ArrayList<Order>> selection(ArrayList<ArrayList<Order>> population) {
        ArrayList<ArrayList<Order>> selectedIndividuals = new ArrayList<>();
        selectedIndividuals.add(population.get(0));
        selectedIndividuals.add(crossover(population.get(0), population.get(1)));
        selectedIndividuals.add(crossover(population.get(0), population.get(2)));
        selectedIndividuals.add(crossover(population.get(1), population.get(2)));
        selectedIndividuals.add(crossover(population.get(0), population.get(3)));
        return selectedIndividuals;
    }

    private static ArrayList<Order> crossover(ArrayList<Order> parent1, ArrayList<Order> parent2) {
        ArrayList<Order> child = new ArrayList<>();
        Random rand = new Random();

        child.add(parent1.get(0));

        for (int i = 1; i < parent1.size(); i++) {
            if (rand.nextBoolean()) {
                addOrderIfNotExists(child, parent1.get(i), parent2);
            } else {
                addOrderIfNotExists(child, parent2.get(i), parent1);
            }
        }

        return child;
    }

    private static void addOrderIfNotExists(ArrayList<Order> child, Order order, ArrayList<Order> otherParent) {
        if (!child.contains(order)) {
            child.add(order);
        } else {
            for (Order altOrder : otherParent) {
                if (!child.contains(altOrder)) {
                    child.add(altOrder);
                    break;
                }
            }
        }
    }

    private static void sortByDiscountNumber(ArrayList<ArrayList<Order>> population) {
        population.sort((individual1, individual2) -> {
            int discountsForIndividual1 = Order.numberOfDiscount(individual1);
            int discountsForIndividual2 = Order.numberOfDiscount(individual2);
            if (discountsForIndividual1 == discountsForIndividual2) {
                // in case of equality, we sort by total delivery time
                double totalTimeIndividual1 = Order.totalDeliveryTime(individual1);
                double totalTimeIndividual2 = Order.totalDeliveryTime(individual2);
                return Double.compare(totalTimeIndividual1, totalTimeIndividual2);
            }
            return Integer.compare(discountsForIndividual1, discountsForIndividual2);
        });
    }

    private static ArrayList<ArrayList<Order>> generatePopulation(ArrayList<Order> orders, Order mandatoryOrder,
                                                                  int populationSize, int individualSize) {
        ArrayList<ArrayList<Order>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(generateIndividual(orders, mandatoryOrder, individualSize));
        }
        return population;
    }

    private static ArrayList<Order> generateIndividual(ArrayList<Order> orders, Order mandatoryOrder, int individualSize) {
        ArrayList<Order> individual = new ArrayList<>();
        ArrayList<Order> ordersCopy = new ArrayList<>(orders);
        individual.add(mandatoryOrder);

        for (int i = 0; i < individualSize - 1; i++) {
            if (!ordersCopy.isEmpty()) {
                int randomIndex = (int) (Math.random() * ordersCopy.size());
                individual.add(ordersCopy.remove(randomIndex));
            }
        }
        return individual;
    }

    private static void displayPopulation(ArrayList<ArrayList<Order>> population, String message) {
        System.out.println("---------------------------" + message + "---------------------------");
        for (ArrayList<Order> individual : population) {
            for (Order order : individual) {
                System.out.print(order.id() + " ");

            }
            System.out.println(", Number of discount: " + Order.numberOfDiscount(individual) +
                    ", Total delivery time: " + Order.totalDeliveryTime(individual));
        }
    }

}