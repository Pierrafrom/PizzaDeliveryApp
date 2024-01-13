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

    public static ArrayList<Order> dynamicDistance(ArrayList<Order> orders, Order mandatoryOrder) {
        if (orders.size() < 5) {
            throw new IllegalArgumentException("Le nombre de commandes doit être au moins 5");
        }

        // Initialiser la table de mémoire
        Map<Set<Order>, Double> memoTable = new HashMap<>();
        Map<Set<Order>, Order> lastOrderInSet = new HashMap<>();

        Set<Order> initialSet = new HashSet<>();
        initialSet.add(mandatoryOrder);
        memoTable.put(initialSet, 0.0);
        lastOrderInSet.put(initialSet, mandatoryOrder);

        for (int size = 2; size <= 5; size++) {
            for (Set<Order> subset : allSubsetsOfSize(orders, size, mandatoryOrder)) {
                double minDistance = Double.MAX_VALUE;
                Order lastOrder = null;

                for (Order order : subset) {
                    if (order.equals(mandatoryOrder) && subset.size() != 1) {
                        continue;
                    }

                    Set<Order> remaining = new HashSet<>(subset);
                    remaining.remove(order);
                    double distance = memoTable.get(remaining) +
                            Order.calculateDeliveryDistance(lastOrderInSet.get(remaining), order);

                    if (distance < minDistance) {
                        minDistance = distance;
                        lastOrder = order;
                    }
                }

                memoTable.put(subset, minDistance);
                lastOrderInSet.put(subset, lastOrder);
            }
        }

        // Construire le chemin de retour
        Set<Order> currentSet = findOptimalSet(memoTable, mandatoryOrder);
        ArrayList<Order> path = new ArrayList<>();
        while (currentSet != null && !currentSet.isEmpty()) {
            Order last = lastOrderInSet.get(currentSet);
            path.add(last);
            currentSet.remove(last);
        }
        Collections.reverse(path);
        return path;
    }

    // Générer tous les sous-ensembles de taille spécifique
    private static Set<Set<Order>> allSubsetsOfSize(ArrayList<Order> orders, int size, Order mandatoryOrder) {
        // Appel initial
        Set<Set<Order>> allSubsets = new HashSet<>();
        generateSubsets(orders, size, 0, new HashSet<>(Collections.singletonList(mandatoryOrder)), allSubsets, mandatoryOrder);
        System.out.println("Nombre de sous-ensembles de taille " + size + ": " + allSubsets.size()); // Pour le débogage
        return allSubsets;
    }

    private static void generateSubsets(ArrayList<Order> orders, int size, int start, Set<Order> current, Set<Set<Order>> allSubsets, Order mandatoryOrder) {
        if (current.size() == size) {
            if (current.contains(mandatoryOrder)) {
                allSubsets.add(new HashSet<>(current));
            }
            return;
        }

        for (int i = start; i < orders.size(); i++) {
            current.add(orders.get(i));
            generateSubsets(orders, size, i + 1, current, allSubsets, mandatoryOrder);
            current.remove(orders.get(i));
        }
    }

    // Trouver l'ensemble optimal
    private static Set<Order> findOptimalSet(Map<Set<Order>, Double> memoTable,
                                             Order mandatoryOrder) {
        Set<Order> optimalSet = null;
        double minDistance = Double.MAX_VALUE;
        for (Set<Order> subset : memoTable.keySet()) {
            if (subset.size() == 5 && subset.contains(mandatoryOrder)) {
                double distance = memoTable.get(subset);
                if (distance < minDistance) {
                    minDistance = distance;
                    optimalSet = subset;
                }
            }
        }
        return optimalSet;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Genetic algorithm with discount criterion
    // -----------------------------------------------------------------------------------------------------------------
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