package com.pizzadelivery.helper;

import com.pizzadelivery.model.Order;
import com.pizzadelivery.model.GPS;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class TestHelper {

    private final static String TEST_SUITE_PATH = "src/main/resources/test_suite/";
    private final static double MIN_LAT = 48.6199;
    private final static double MAX_LAT = 48.80406;
    private final static double MIN_LON = 1.98761;
    private final static double MAX_LON = 2.255694;

    public enum NUMBER_OF_ORDERS {
        BRUTE_FORCE(5),
        GENETIC(35),
        GREEDY(70),
        DYNAMIC(15);

        private final int value;

        NUMBER_OF_ORDERS(final int newValue) {
            value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public static ArrayList<Order> generateOrders(NUMBER_OF_ORDERS numberOfOrders) {
        String fileName = TEST_SUITE_PATH + numberOfOrders.name() + ".ser";
        File file = new File(fileName);

        ArrayList<Order> orders;
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                orders = (ArrayList<Order>) ois.readObject();
                updateOrderDates(orders);
                return orders;
            } catch (IOException | ClassNotFoundException e) {
                Logger logger = Logger.getLogger(TestHelper.class.getName());
                logger.warning("Could not load test suite from file" + e.getMessage());
            }
        }

        orders = generateNewOrders(numberOfOrders);
        saveOrdersToFile(orders, fileName);
        return orders;
    }

    private static ArrayList<Order> generateNewOrders(NUMBER_OF_ORDERS numberOfOrders) {
        ArrayList<Order> orders = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfOrders.getValue(); i++) {
            double lat = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
            double lon = MIN_LON + (MAX_LON - MIN_LON) * random.nextDouble();
            GPS location = new GPS(lat, lon);

            int minutesAgo = random.nextInt(15);
            LocalDateTime time = LocalDateTime.now().minusMinutes(minutesAgo);

            Order order = new Order(i, location, time);
            orders.add(order);
        }
        return orders;
    }

    private static void saveOrdersToFile(ArrayList<Order> orders, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(TestHelper.class.getName());
            logger.warning("Could not save test suite to file" + e.getMessage());
        }
    }

    private static void updateOrderDates(ArrayList<Order> orders) {
        Random random = new Random();
        for (Order order : orders) {
            int minutesAgo = random.nextInt(15);
            LocalDateTime newOrderDate = LocalDateTime.now().minusMinutes(minutesAgo);
            order.setTime(newOrderDate);
        }
    }
}
