package helper;

import com.pizzadelivery.model.Order;
import com.pizzadelivery.model.GPS;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Generates a test suite of orders for different algorithms in the pizza delivery system.
 * This class handles the creation, serialization, and updating of test orders with varying GPS coordinates.
 */
public class TestSuiteGenerator {

    private final static String TEST_SUITE_PATH = "src/main/resources/test_suite/";
    private final static double MIN_LAT = 48.6199;
    private final static double MAX_LAT = 48.80406;
    private final static double MIN_LON = 1.98761;
    private final static double MAX_LON = 2.255694;

    /**
     * Enum to define the number of orders for each type of algorithm.
     */
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

    /**
     * Generates a list of orders based on the specified algorithm type.
     * If a saved test suite exists, it is loaded; otherwise, new orders are generated.
     *
     * @param numberOfOrders the type of algorithm indicating the number of orders to generate
     * @return an ArrayList of generated or loaded orders
     */
    public static ArrayList<Order> generateOrders(NUMBER_OF_ORDERS numberOfOrders) {
        String fileName = TEST_SUITE_PATH + numberOfOrders.name() + ".ser";
        File file = new File(fileName);

        ArrayList<Order> orders;
        // Check if file exists and has content, load from it
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                orders = (ArrayList<Order>) ois.readObject();
                updateOrderDates(orders);
                return orders;
            } catch (IOException | ClassNotFoundException e) {
                Logger logger = Logger.getLogger(TestSuiteGenerator.class.getName());
                logger.warning("Could not load test suite from file" + e.getMessage());
            }
        }

        // Generate new orders if file does not exist
        ArrayList<GPS> validGPS = loadOrCreateValidGPSList();
        orders = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < numberOfOrders.getValue(); i++) {
            GPS location = validGPS.get(i);

            int minutesAgo = random.nextInt(15);
            LocalDateTime time = LocalDateTime.now().minusMinutes(minutesAgo);

            Order order = new Order(i, location, time);
            orders.add(order);
        }

        saveOrdersToFile(orders, fileName);
        return orders;
    }

    /**
     * Saves the list of orders to a file for future use.
     *
     * @param orders   the list of orders to save
     * @param fileName the name of the file to save the orders in
     */
    private static void saveOrdersToFile(ArrayList<Order> orders, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(orders);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(TestSuiteGenerator.class.getName());
            logger.warning("Could not save test suite to file" + e.getMessage());
        }
    }

    /**
     * Updates the order dates to a recent time frame.
     * This method is used when loading orders from a file to ensure the dates are current.
     *
     * @param orders the list of orders whose dates need to be updated
     */
    private static void updateOrderDates(ArrayList<Order> orders) {
        Random random = new Random();
        for (Order order : orders) {
            int minutesAgo = random.nextInt(15);
            LocalDateTime newOrderDate = LocalDateTime.now().minusMinutes(minutesAgo);
            order.setTime(newOrderDate); // Update order date to a recent time
        }
    }

    /**
     * Generates a valid GPS coordinate within predefined boundaries.
     * This method continues to generate GPS coordinates until a valid one is found.
     *
     * @return a valid GPS object
     * @throws IOException if an error occurs during GPS validation
     */
    private static GPS generateValidGPS() throws IOException {
        Random random = new Random();
        while (true) {
            double lat = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
            double lon = MIN_LON + (MAX_LON - MIN_LON) * random.nextDouble();
            GPS location = new GPS(lat, lon);

            if (isValidGPS(location)) {
                return location; // Return the GPS location if it's valid
            }
        }
    }

    /**
     * Validates a GPS coordinate by checking the response from an external API.
     *
     * @param gps the GPS object to validate
     * @return true if the GPS location is valid, false otherwise
     */
    private static boolean isValidGPS(GPS gps) {
        try {
            System.out.println(gps);
            GPS fixedDestination = new GPS(48.711729, 2.165678);
            String response = GPS.callOpenRouteServiceApi(gps, fixedDestination);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray routes = jsonResponse.getJSONArray("routes");
            return !routes.isEmpty();
        } catch (Exception e) {
            Logger logger = Logger.getLogger(TestSuiteGenerator.class.getName());
            logger.warning("Error calling OpenRouteService API: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads or creates a list of valid GPS coordinates.
     * If a saved list exists, it is loaded; otherwise, new GPS coordinates are generated.
     *
     * @return an ArrayList of valid GPS coordinates
     */
    private static ArrayList<GPS> loadOrCreateValidGPSList() {
        File gpsFile = new File(TEST_SUITE_PATH + "correctGPS.ser");
        ArrayList<GPS> validGpsList = new ArrayList<>();

        // Load GPS list from file if it exists
        if (gpsFile.exists() && gpsFile.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(gpsFile))) {
                validGpsList = (ArrayList<GPS>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Logger logger = Logger.getLogger(TestSuiteGenerator.class.getName());
                logger.warning("Could not load GPS list from file: " + e.getMessage());
            }
        }

        // Generate new GPS coordinates if needed until reaching the required list size
        while (validGpsList.size() < 70) { // The number 70 here should ideally be a defined constant or a parameter
            try {
                GPS validGps = generateValidGPS();
                validGpsList.add(validGps);
                saveGPSListToFile(validGpsList, gpsFile.getAbsolutePath());
            } catch (IOException e) {
                Logger logger = Logger.getLogger(TestSuiteGenerator.class.getName());
                logger.warning("Error generating valid GPS: " + e.getMessage());
            }
        }
        return validGpsList;
    }

    /**
     * Saves the list of valid GPS coordinates to a file.
     *
     * @param gpsList  the list of GPS coordinates to save
     * @param fileName the name of the file to save the GPS coordinates in
     */
    private static void saveGPSListToFile(ArrayList<GPS> gpsList, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(gpsList);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(TestSuiteGenerator.class.getName());
            logger.warning("Could not save GPS list to file: " + e.getMessage());
        }
    }
}
