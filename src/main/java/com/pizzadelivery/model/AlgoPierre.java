package com.pizzadelivery.model;

import com.pizzadelivery.utils.List;

public class AlgoPierre {
    private static int[] findShortest(GPS location, List<Order> orders) throws RateLimitExceededException {
        if (orders == null || orders.length() == 0) {
            throw new IllegalArgumentException("Order list cannot be empty.");
        }

        int shortestIndex = 0;
        int shortestTimeTravel = Integer.MAX_VALUE;

        for (int i = 0; i < orders.length(); i++) {
            double timeTravel = 0;
            try {
                timeTravel = location.timeTravel(orders.get(i).location());
            } catch (RateLimitExceededException e) {
                throw new RuntimeException(e);
            }
            if (timeTravel < shortestTimeTravel) {
                shortestIndex = i;
                shortestTimeTravel = (int) Math.round(timeTravel);
            }
        }

        return new int[]{shortestIndex, shortestTimeTravel};
    }

    private static int sortOrdersByShortestRec(GPS currentLocation,
                                               List<Order> orders,
                                               List<Order> sortedOrders,
                                               int timeTravel) throws RateLimitExceededException {
        if (orders.length() == 0) {
            return timeTravel;
        }

        int[] res = null;
        try {
            res = findShortest(currentLocation, orders);
        } catch (RateLimitExceededException e) {
            throw new RuntimeException(e);
        }
        int shortestIndex = res[0];
        int shortestTimeTravel = res[1];
        Order shortestOrder = orders.remove(shortestIndex);

        sortedOrders.add(shortestOrder);
        timeTravel += shortestTimeTravel;

        return sortOrdersByShortestRec(shortestOrder.location(), orders, sortedOrders, timeTravel);
    }

    public static int sortOrdersByShortestGreedy(List<Order> orders) {
        List<Order> sortedOrders = new List<>(null, null, Delivery_Person.MAX_ORDERS);
        int timeTravel = 0;
        try {
            timeTravel = sortOrdersByShortestRec(Delivery_Person.PIZZERIA_LOCATION,
                    orders,
                    sortedOrders,
                    0);
        } catch (RateLimitExceededException e) {
            return -1;
        }

        orders.clear();
        orders.addAll(sortedOrders);

        int[] ratingTimes = {
                DeliveryRating.NOTE_10_MAX_TIME,
                DeliveryRating.NOTE_9_MAX_TIME,
                DeliveryRating.NOTE_8_MAX_TIME,
                DeliveryRating.NOTE_7_MAX_TIME,
                DeliveryRating.NOTE_6_MAX_TIME,
                DeliveryRating.NOTE_5_MAX_TIME,
                DeliveryRating.NOTE_4_MAX_TIME,
                DeliveryRating.NOTE_3_MAX_TIME,
                DeliveryRating.NOTE_2_MAX_TIME,
                DeliveryRating.NOTE_1_MAX_TIME
        };

        for (int i = 10; i >= 1; i--) {
            if (timeTravel <= ratingTimes[i - 1]) {
                return i;
            }
        }

        return 0;
    }

    private static double[][] getMatrix(List<Order> orders) {
        int matrixSize = orders.length() + 1; // +1 for the pizzeria
        double[][] matrix = new double[matrixSize][matrixSize];

        // Calculate travel times between each pair of points
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (i == j) {
                    matrix[i][j] = 0;
                } else {
                    GPS point1 = (i == 0) ? Delivery_Person.PIZZERIA_LOCATION : orders.get(i - 1).location();
                    GPS point2 = (j == 0) ? Delivery_Person.PIZZERIA_LOCATION : orders.get(j - 1).location();
                    try {
                        matrix[i][j] = point1.timeTravel(point2);
                    } catch (RateLimitExceededException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return matrix;
    }

    // Static method to calculate total distance
    private static double calculateTotalDistance(List<Order> route, double[][] matrix, List<Order> orders) {
        double totalDistance = 0;
        int prevIndex = 0; // Start from the pizzeria

        // Iterate through the route
        for (int i = 0; i < route.length(); i++) {
            int currentIndex = orders.indexOf(route.get(i)) + 1; // Find the actual index of the order in the original list
            totalDistance += matrix[prevIndex][currentIndex];
            prevIndex = currentIndex;
        }

        // Return to pizzeria
        totalDistance += matrix[prevIndex][0];
        return totalDistance;
    }
    // Method to find the shortest route
    private static List<Order> findShortestRoute(List<Order> orders, double[][] matrix) {
        List<List<Order>> allPermutations = new List<>();
        orders.generatePermutations(0, allPermutations);

        /*
        for (int i= 0; i < allPermutations.length(); i++) {
            for (int j = 0; j < allPermutations.get(i).length(); j++) {
                System.out.print(allPermutations.get(i).get(j).id() + " ");
            }
            System.out.print("\n");
        } */

        List<Order> shortestRoute = null;
        double shortestDistance = Double.MAX_VALUE;

        for (int i = 0; i < allPermutations.length(); i++) {
            double distance = calculateTotalDistance(allPermutations.get(i), matrix, orders);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                shortestRoute = allPermutations.get(i);
            }
        }

        return shortestRoute;
    }

    public static int sortOrdersByShortestBruteForce(List<Order> orders) {
        double[][] matrix = getMatrix(orders);
        List<Order> shortestRoute = findShortestRoute(orders, matrix);

        orders.clear();
        orders.addAll(shortestRoute);

        int[] ratingTimes = {
                DeliveryRating.NOTE_10_MAX_TIME,
                DeliveryRating.NOTE_9_MAX_TIME,
                DeliveryRating.NOTE_8_MAX_TIME,
                DeliveryRating.NOTE_7_MAX_TIME,
                DeliveryRating.NOTE_6_MAX_TIME,
                DeliveryRating.NOTE_5_MAX_TIME,
                DeliveryRating.NOTE_4_MAX_TIME,
                DeliveryRating.NOTE_3_MAX_TIME,
                DeliveryRating.NOTE_2_MAX_TIME,
                DeliveryRating.NOTE_1_MAX_TIME
        };

        double shortestDistance = calculateTotalDistance(shortestRoute, matrix, orders);

        for (int i = 10; i >= 1; i--) {
            if (shortestDistance <= ratingTimes[i - 1]) {
                return i;
            }
        }

        return 0;
    }
}
