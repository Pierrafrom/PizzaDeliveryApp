package com.pizzadelivery.model;

import java.util.*;

/**
 * The PierreAlgorithm class encapsulates multiple algorithms for optimizing the sequence of pizza delivery orders.
 * This class provides methods implementing different algorithmic approaches, each tailored to optimize order sequences
 * based on specific criteria such as delivery time, distance, and discounts. The class includes brute-force, greedy,
 * dynamic programming, and genetic algorithm methods, offering a range of solutions suitable for different scenarios
 * and requirements.
 *
 * <p><b>Algorithms
 * <p>
 * Included:</b></p>
 *
 * <ul>
 * <li><b>Brute Force Algorithm (Time Criterion):</b> Finds the optimal sequence by checking every possible order
 * permutation to minimize total delivery time. Best for small datasets but computationally intensive
 * for larger ones.</li>
 * <li><b>Greedy Algorithm (Time Criterion):</b> Quickly finds a near-optimal sequence by selecting the closest next
 * order iteratively. Efficient for small datasets but may not find the globally optimal solution.</li>
 * <li><b>Dynamic Programming (Distance Criterion):</b> Optimizes the order sequence to minimize total delivery
 * distance, including a mandatory order. Suitable for scenarios with manageable order numbers, offering precise
 * optimization.</li>
 * <li><b>Genetic Algorithm (Discount Criterion):</b> Evolves solutions over generations to balance discounts
 * and delivery time, capable of finding near-optimal solutions in complex scenarios.</li>
 * </ul>
 *
 * <p>Each algorithm has its unique advantages, making the PierreAlgorithm class versatile for various optimization
 * needs in pizza delivery services. The choice of algorithm depends on the size of the dataset, the required
 * optimization criterion, and the desired balance between computational efficiency and solution optimality.</p>
 *
 * <p>Methods in this class also include utility functions for the algorithms, such as generating populations for
 * the genetic algorithm, performing mutations and crossovers, and displaying the state of populations for monitoring
 * and debugging.</p>
 *
 * <p><b>Usage:</b> Instantiate the class and call the desired algorithm method with the appropriate parameters.
 * Each method returns an optimized sequence of {@code Order} objects based on the specified criterion.</p>
 *
 * @author Pierre
 */
public class PierreAlgorithm {

    // -----------------------------------------------------------------------------------------------------------------
    // Brute force algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Optimizes the sequence of orders based on delivery time using a brute-force approach.
     * This method explores all possible sequences of the provided orders to find the one with the minimum total delivery time.
     * It uses a recursive permutation approach to generate all possible orderings of the orders list and then evaluates
     * <p>
     * each sequence to determine the one with the lowest total delivery time.
     *
     * <p>The brute-force method guarantees finding the optimal solution, as it exhaustively searches all possibilities.</p>
     *
     * <p><b>Advantages:</b>
     * <ul>
     * <li>Finds the absolute best solution by checking every possible sequence.</li>
     * <li>Simple and straightforward to implement.</li>
     * <li>Suitable for problems with a small number of orders due to its exhaustive nature.</li>
     * </ul>
     * </p>
     *
     * <p><b>Disadvantages:</b>
     * <ul>
     * <li>Highly inefficient and computationally expensive for large datasets.</li>
     * <li>Time complexity increases factorially with the number of orders, making it impractical for larger sets.</li>
     * <li>Can lead to significant delays in finding a solution as the number of orders increases.</li>
     * </ul>
     * </p>
     *
     * <p><b>Complexity Analysis:</b>
     * The time complexity is O(n!), where 'n' is the number of orders. Each permutation of the order list is generated
     * and evaluated, leading to factorial growth in the number of computations.</p>
     *
     * @param orders An ArrayList of {@code Order} objects representing the available orders.
     * @return An ArrayList of {@code Order} objects representing the optimized sequence.
     */
    public static ArrayList<Order> bruteForceTime(ArrayList<Order> orders) {
        ArrayList<Order> bestOrderSequence = new ArrayList<>(orders);
        double[] bestTime = new double[]{Double.MAX_VALUE};
        // Generate and evaluate all permutations of the order list
        permute(orders, 0, bestOrderSequence, bestTime);
        return bestOrderSequence; // Return the sequence with the minimum total delivery time
    }

    /**
     * Recursively generates permutations of the order list and evaluates their total delivery time.
     * This helper method swaps elements to create different sequences, and for each sequence,
     * it calculates the total delivery time. If a sequence has a lower delivery time than the current best,
     * it updates the best sequence and time.
     *
     * @param orders            The list of orders to permute.
     * @param k                 The current index in the permutation process.
     * @param bestOrderSequence A reference to the current best sequence of orders.
     * @param bestTime          An array holding the minimum delivery time found so far.
     */
    private static void permute(ArrayList<Order> orders, int k, ArrayList<Order> bestOrderSequence, double[] bestTime) {
        for (int i = k; i < orders.size(); i++) {
            Collections.swap(orders, i, k); // Swap to generate a new permutation
            permute(orders, k + 1, bestOrderSequence, bestTime); // Recurse for the next element
            Collections.swap(orders, k, i); // Swap back for backtracking
        }
        if (k == orders.size() - 1) {
            double currentTime = Order.totalDeliveryTime(orders); // Calculate the delivery time for the current sequence
            if (currentTime < bestTime[0]) {
                bestTime[0] = currentTime; // Update the best time
                bestOrderSequence.clear();
                bestOrderSequence.addAll(orders); // Update the best sequence
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Greedy algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Optimizes the sequence of orders based on delivery time using a greedy algorithm.
     * This method applies a greedy approach to select a sequence of orders that minimizes the total delivery time,
     * starting with a mandatory order and iteratively selecting the closest next order. The algorithm prioritizes
     * minimizing the immediate delivery time at each step, which may not always lead to the globally optimal solution.
     *
     * <p>The method is suitable for scenarios where quick decision-making is required, and the number of orders
     * is relatively small.</p>
     *
     * <p><b>Advantages:</b>
     * <ul>
     * <li>Simple and easy to implement.</li>
     * <li>Efficient for small sets of data or when a quick approximation is needed.</li>
     * <li>Guarantees inclusion of a mandatory order in the result.</li>
     * </ul>
     * </p>
     *
     * <p><b>Disadvantages:</b>
     * <ul>
     * <li>May not find the globally optimal solution, especially in complex or large datasets.</li>
     * <li>Greedy nature can lead to suboptimal choices in the long run (local maxima problem).</li>
     * </ul>
     * </p>
     *
     * <p><b>Complexity Analysis:</b>
     * The time complexity is O(n^2) for a set of 'n' orders. This arises because, for each of the 4 steps,
     * the algorithm iterates through the remaining orders to find the closest
     * one, leading to a quadratic complexity in the worst case.</p>
     *
     * @param orders         An ArrayList of {@code Order} objects representing the available orders.
     * @param mandatoryOrder An {@code Order} object that must be included in the sequence.
     * @return An ArrayList of {@code Order} objects representing the optimized sequence.
     * @throws IllegalArgumentException If the number of orders is less than the minimum required (4 in this case).
     */
    public static ArrayList<Order> greedyTime(ArrayList<Order> orders, Order mandatoryOrder) {
        // Validate that there are enough orders
        if (orders.size() < 4) {
            throw new IllegalArgumentException("The number of orders must be at least 4");
        }

        ArrayList<Order> selectedOrders = new ArrayList<>();
        // Add the mandatory order as the starting point
        selectedOrders.add(mandatoryOrder);

        // Initialize a set with the remaining orders
        Set<Order> remainingOrders = new HashSet<>(orders);
        // Remove the mandatory order from the remaining set
        remainingOrders.remove(mandatoryOrder);

        // Select orders based on closest delivery time
        for (int i = 0; i < 4; i++) {
            // Find and add the closest order
            Order closestOrder = findClosestOrder(selectedOrders.get(selectedOrders.size() - 1), remainingOrders);
            if (closestOrder != null) {
                selectedOrders.add(closestOrder);
                // Remove the chosen order from the remaining set
                remainingOrders.remove(closestOrder);
            }
        }

        return selectedOrders; // Return the list of selected orders
    }

    /**
     * Identifies the order closest to a given reference order in terms of delivery time.
     * This method is a key part of the greedy algorithm. It searches through a set of orders to find the one
     * that has the minimum delivery time when compared to a specified reference order. The method calculates
     * the delivery time between the reference order and each order in the set, selecting the one with
     * the shortest time. This approach is integral to the greedy algorithm's strategy of making the locally optimal
     * choice at each step.
     *
     * <p>This method aids in selecting the next order in the sequence that minimizes the immediate delivery time,
     * contributing to the overall efficiency of the route.</p>
     *
     * @param referenceOrder The order against which all other orders' delivery times are compared.
     * @param orders         A set of {@code Order} objects to be considered for selection.
     * @return The {@code Order} closest to the reference order in terms of delivery time, or {@code null} if no
     * orders are available.
     */
    private static Order findClosestOrder(Order referenceOrder, Set<Order> orders) {
        Order closestOrder = null;
        double closestTime = Double.MAX_VALUE;

        // Iterate through each order to find the one with the minimum delivery time
        for (Order order : orders) {
            double deliveryTime = Order.calculateDeliveryTime(referenceOrder, order);
            // Update the closest order if a shorter delivery time is found
            if (deliveryTime < closestTime) {
                closestTime = deliveryTime;
                closestOrder = order;
            }
        }
        return closestOrder; // Return the order with the shortest delivery time relative to the reference order
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
    private static void initializeMemoTable(Order
                                                    mandatoryOrder, Map<Set<Order>, Double> memoTable, Map<Set<Order>,
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
     * @param display        A boolean flag indicating whether to display the state of the population at each generation.
     * @return An ArrayList of {@code Order} objects representing the optimized order sequence.
     * @throws IllegalArgumentException If the number of orders is less than the minimum required (4 in this case).
     */
    public static ArrayList<Order> geneticDiscount(ArrayList<Order> orders, Order mandatoryOrder, boolean display) {
        if (orders.size() < 4) {
            throw new IllegalArgumentException("The number of orders must be at least 4");
        }
        // Initialize algorithm parameters
        final int INDIVIDUAL_SIZE = 5;
        final int POPULATION_SIZE = 10;
        final int MAX_GENERATION = 100;

        // Generate the initial population
        ArrayList<ArrayList<Order>> population = generatePopulation(orders, mandatoryOrder, POPULATION_SIZE, INDIVIDUAL_SIZE);
        // Sort the population based on the number of discounts
        sortByDiscountNumber(population);

        int generation = 1;
        // Evolve the population through generations
        while (generation < MAX_GENERATION) {
            if (display) {
                displayPopulation(population, "Generation " + generation);
            }
            // Generate a new population
            population = generateNewPopulation(population, orders, mandatoryOrder, POPULATION_SIZE, INDIVIDUAL_SIZE);
            generation++;
        }
        if (display) {
            System.out.println(population.get(0));
        }

        return population.get(0);  // Return the best individual from the final generation

    }

    /**
     * Generates a new population from the current population in the genetic algorithm process.
     * This method is a crucial step in the genetic algorithm, facilitating the evolution of solutions.
     * It involves selecting a subset of the current population, applying mutations to introduce variability,
     * and then filling the rest of the new population with newly generated individuals until the population
     * size is met. Finally, the new population is sorted based on the number of discounts and delivery time.
     *
     * <p>This method ensures the genetic diversity and evolution of the population towards better solutions
     * over successive generations.</p>
     *
     * @param population     The current population of individuals (sequences of {@code Order} objects).
     * @param orders         The complete list of available {@code Order} objects.
     * @param mandatoryOrder The mandatory {@code Order} that must be included in each individual.
     * @param populationSize The size of the population to be maintained.
     * @param individualSize The size of each individual (sequence of orders) in the population.
     * @return A new population of individuals for the next generation.
     */
    private static ArrayList<ArrayList<Order>> generateNewPopulation(ArrayList<ArrayList<Order>> population,
                                                                     ArrayList<Order> orders,
                                                                     Order mandatoryOrder,
                                                                     int populationSize, int individualSize) {
        // Select a subset of the current population
        ArrayList<ArrayList<Order>> newPopulation = selection(population);
        // Apply mutations to introduce variability
        for (ArrayList<Order> individual : newPopulation) {
            mutate(individual);  // Mutate each individual
        }
        // Fill the rest of the population with new individuals
        while (newPopulation.size() < populationSize) {
            newPopulation.add(generateIndividual(orders, mandatoryOrder, individualSize));
        }
        // Sort the new population based on the number of discounts and delivery time
        sortByDiscountNumber(newPopulation);
        return newPopulation;  // Return the newly generated population
    }

    /**
     * Applies a mutation operation to an individual in the genetic algorithm.
     * This method randomly swaps two orders in the given individual (sequence of {@code Order} objects),
     * excluding the first order which is mandatory. This mutation introduces variability and diversity
     * into the population, aiding in the exploration of the solution space and preventing premature convergence
     * to local optima.
     *
     * <p>The mutation is a key mechanism in genetic algorithms to maintain genetic diversity and enable
     * the algorithm to explore a wide range of solutions over generations.</p>
     *
     * @param individual The individual (sequence of {@code Order} objects) to be mutated.
     */
    private static void mutate(ArrayList<Order> individual) {
        Random rand = new Random();
        // Generate two different random indices for the orders to be swapped, excluding the first (mandatory) order
        int index1 = 1 + rand.nextInt(individual.size() - 1);
        int index2 = 1 + rand.nextInt(individual.size() - 1);

        // Ensure that the two indices are different
        while (index1 == index2) {
            index2 = 1 + rand.nextInt(individual.size() - 1);
        }

        // Swap the orders at the generated indices
        Order temp = individual.get(index1);
        individual.set(index1, individual.get(index2));
        individual.set(index2, temp);
    }

    /**
     * Performs the selection operation in the genetic algorithm.
     * This method selects a subset of individuals from the current population to be parents for the next generation.
     * The selection is based on the fitness of the individuals, with a preference for those with better fitness (e.g.,
     * higher discounts, shorter delivery times). The method includes both direct selection and generation of new
     * individuals through crossover between selected parents.
     *
     * <p>Selection is a crucial process in genetic algorithms, directing the evolution towards better solutions
     * by choosing fitter individuals for reproduction.</p>
     *
     * @param population The current population of individuals (sequences of {@code Order} objects).
     * @return A new ArrayList of selected individuals and their offspring.
     */
    private static ArrayList<ArrayList<Order>> selection(ArrayList<ArrayList<Order>> population) {
        // Initialize a list to store the selected individuals
        ArrayList<ArrayList<Order>> selectedIndividuals = new ArrayList<>();

        // Directly select the fittest individual
        selectedIndividuals.add(population.get(0));

        // Generate new individuals by crossover using selected parents
        selectedIndividuals.add(crossover(population.get(0), population.get(1)));
        selectedIndividuals.add(crossover(population.get(0), population.get(2)));
        selectedIndividuals.add(crossover(population.get(1), population.get(2)));
        selectedIndividuals.add(crossover(population.get(0), population.get(3)));

        return selectedIndividuals;  // Return the list of selected and crossover-generated individuals
    }

    /**
     * Performs a crossover operation between two parent individuals in the genetic algorithm.
     * This method combines parts of two parent individuals (sequences of {@code Order} objects) to create
     * a new child individual. The first order of the child is always inherited from the first parent,
     * ensuring the mandatory order is included. For the remaining orders, the method randomly selects
     * orders from either parent, ensuring no duplicate orders in the child.
     *
     * <p>Crossover is a fundamental genetic algorithm operation that allows for the mixing of genetic
     * material (in this case, order sequences) from two parents to produce a new individual, potentially
     * with better fitness.</p>
     *
     * @param parent1 The first parent individual.
     * @param parent2 The second parent individual.
     * @return A new individual (ArrayList of {@code Order}) created by combining orders from both parents.
     */
    private static ArrayList<Order> crossover(ArrayList<Order> parent1, ArrayList<Order> parent2) {
        ArrayList<Order> child = new ArrayList<>();
        Random rand = new Random();

        // Always inherit the first order from parent1 (mandatory order)
        child.add(parent1.get(0));

        // For each remaining order, randomly choose from either parent1 or parent2
        for (int i = 1; i < parent1.size(); i++) {
            if (rand.nextBoolean()) {
                // Add order from parent1 if it does not already exist in child
                addOrderIfNotExists(child, parent1.get(i), parent2);
            } else {
                // Add order from parent2 if it does not already exist in child
                addOrderIfNotExists(child, parent2.get(i), parent1);
            }
        }

        return child;  // Return the newly created child individual
    }

    /**
     * Adds an order to a child individual if it's not already present, otherwise adds an alternative order.
     * This method is used during the crossover process in the genetic algorithm. It attempts to add a specified
     * order to the child individual. If the order is already in the child, the method searches the other parent
     * for an alternative order that is not yet in the child and adds it. This ensures diversity in the child
     * individual while avoiding duplicates.
     *
     * <p>This method helps in maintaining a valid sequence of orders in the child individual by preventing
     * duplicate orders and ensuring a mix of orders from both parents.</p>
     *
     * @param child       The child individual (sequence of {@code Order} objects) being constructed.
     * @param order       The order to be added to the child.
     * @param otherParent The other parent individual used as an alternative source of orders.
     */
    private static void addOrderIfNotExists(ArrayList<Order> child, Order order, ArrayList<Order> otherParent) {
        // Check if the order is already in the child
        if (!child.contains(order)) {
            // If not, add the order to the child
            child.add(order);
        } else {
            // If the order is already in the child, look for an alternative order from the other parent
            for (Order altOrder : otherParent) {
                if (!child.contains(altOrder)) {
                    child.add(altOrder);  // Add the first alternative order found
                    break;
                }
            }
        }
    }

    /**
     * Sorts the population based on the number of discounts and, in case of a tie, total delivery time.
     * This method is used in the genetic algorithm to order the population of individuals (sequences of {@code Order} objects)
     * primarily by the number of discounts each individual achieves, and secondarily by the total delivery time in case of
     * equal discount numbers. This sorting criterion aims to prioritize solutions that maximize discounts while minimizing
     * delivery time.
     *
     * <p>Sorting the population based on fitness criteria (discounts and delivery time) is crucial in genetic algorithms
     * for selecting top-performing individuals for further evolution.</p>
     *
     * @param population The population of individuals to be sorted.
     */
    private static void sortByDiscountNumber(ArrayList<ArrayList<Order>> population) {
        // Sort the population based on the number of discounts and total delivery time
        population.sort((individual1, individual2) -> {
            int discountsForIndividual1 = Order.numberOfDiscount(individual1);
            int discountsForIndividual2 = Order.numberOfDiscount(individual2);

            // If the number of discounts is the same, sort by total delivery time
            if (discountsForIndividual1 == discountsForIndividual2) {
                double totalTimeIndividual1 = Order.totalDeliveryTime(individual1);
                double totalTimeIndividual2 = Order.totalDeliveryTime(individual2);
                return Double.compare(totalTimeIndividual1, totalTimeIndividual2);
            }
            // Otherwise, sort by the number of discounts
            return Integer.compare(discountsForIndividual1, discountsForIndividual2);
        });
    }

    /**
     * Generates the initial population for the genetic algorithm.
     * This method creates a population of individuals, each representing a potential solution to the optimization problem.
     * Each individual is a sequence of {@code Order} objects of a specified size, including the mandatory order.
     * The initial population is generated randomly, ensuring diversity, which is crucial for the effectiveness
     * of the genetic algorithm.
     *
     * <p>This initial population serves as the starting point for the genetic algorithm's process of evolution
     * towards optimal solutions.</p>
     *
     * @param orders         The complete list of available {@code Order} objects.
     * @param mandatoryOrder The mandatory {@code Order} that must be included in each individual.
     * @param populationSize The size of the population to be generated.
     * @param individualSize The size of each individual (sequence of orders) in the population.
     * @return An ArrayList representing the initial population, where each element is an individual.
     */
    private static ArrayList<ArrayList<Order>> generatePopulation(ArrayList<Order> orders, Order mandatoryOrder,
                                                                  int populationSize, int individualSize) {
        ArrayList<ArrayList<Order>> population = new ArrayList<>();
        // Create each individual in the population
        for (int i = 0; i < populationSize; i++) {
            // Generate and add a new individual to the population
            population.add(generateIndividual(orders, mandatoryOrder, individualSize));
        }
        return population; // Return the generated initial population
    }

    /**
     * Generates an individual for the genetic algorithm's population.
     * This method creates a single individual (sequence of {@code Order} objects) for the genetic algorithm's population.
     * It starts by adding the mandatory order to ensure its presence in every individual. The rest of the individual
     * is filled with randomly chosen orders from the available list, ensuring that the individual's size matches
     * the specified size. This random selection contributes to the diversity of the initial population.
     *
     * <p>Generating diverse individuals is crucial for the genetic algorithm to effectively explore the solution space.</p>
     *
     * @param orders         The complete list of available {@code Order} objects.
     * @param mandatoryOrder The mandatory {@code Order} that must be included in each individual.
     * @param individualSize The size of the individual to be generated.
     * @return An individual (ArrayList of {@code Order}), a potential solution for the optimization problem.
     */
    private static ArrayList<Order> generateIndividual(ArrayList<Order> orders, Order mandatoryOrder,
                                                       int individualSize) {
        ArrayList<Order> individual = new ArrayList<>();
        ArrayList<Order> ordersCopy = new ArrayList<>(orders);
        // Start the individual with the mandatory order
        individual.add(mandatoryOrder);

        // Randomly select orders to fill the rest of the individual
        for (int i = 0; i < individualSize - 1; i++) {
            if (!ordersCopy.isEmpty()) {
                int randomIndex = (int) (Math.random() * ordersCopy.size());
                // Add the randomly selected order and remove it from the copy list
                individual.add(ordersCopy.remove(randomIndex));
            }
        }
        return individual;  // Return the generated individual
    }

    /**
     * Displays the current state of the population in the genetic algorithm.
     * This method prints out each individual in the population along with key metrics such as the number of discounts
     * and the total delivery time. It's primarily used for debugging or monitoring the progress of the genetic algorithm.
     * The output is prefaced by a custom message, allowing for contextual information like the generation number.
     *
     * <p>This method is useful for observing the evolution of the population over time and
     * <p>
     * assessing the effectiveness of the genetic algorithm's operations like selection, crossover, and mutation.</p>
     *
     * @param population The population of individuals (sequences of {@code Order} objects) to be displayed.
     * @param message    A custom message to be displayed before the population, usually indicating the context or the
     *                   generation number.
     */
    private static void displayPopulation(ArrayList<ArrayList<Order>> population, String message) {
        // Print the custom message header
        System.out.println("---------------------------" + message + "---------------------------");

        // Iterate over each individual in the population
        for (ArrayList<Order> individual : population) {
            // Print each order 's ID in the individual
            for (Order order : individual) {
                System.out.print(order.id() + " ");
            }
            // Print the number of discounts and total delivery time for the individual
            System.out.println(", Number of discount: " + Order.numberOfDiscount(individual) +
                    ", Total delivery time: " + Order.totalDeliveryTime(individual));
        }
    }

}