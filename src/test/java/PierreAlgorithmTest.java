import com.pizzadelivery.model.GPS;
import com.pizzadelivery.model.Order;
import com.pizzadelivery.model.PierreAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PierreAlgorithmTest extends AlgorithmTest {

    // -----------------------------------------------------------------------------------------------------------------
    // Brute force algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void testBruteForceTimeCorrectSelection() {
        // Test to ensure that the brute force algorithm selects the correct orders
        ArrayList<Order> orders = bruteForceTestSuite;
        ArrayList<Order> selectedOrders = PierreAlgorithm.bruteForceTime(orders);
        for (Order order : selectedOrders) {
            System.out.println(order.id());
        }
        System.out.println("Time: " + Order.totalDeliveryTime(selectedOrders));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // greedy algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void testGreedyTimeCorrectSelection() {
        // Test to ensure that the greedy algorithm selects the correct orders
        ArrayList<Order> orders = greedyTestSuite;
        Order mandatoryOrder = orders.remove(0);
        ArrayList<Order> selectedOrders = PierreAlgorithm.greedyTime(orders, mandatoryOrder);
        for (Order order : selectedOrders) {
            System.out.println(order.id());
        }
        System.out.println("Time: " + Order.totalDeliveryTime(selectedOrders));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Dynamic programming algorithm with distance criterion
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void testDynamicDistanceCorrectSelection() {
        // Test to ensure that the dynamic programming algorithm selects the correct orders
        ArrayList<Order> orders = dynamicTestSuite;
        Order mandatoryOrder = orders.remove(0);
        ArrayList<Order> selectedOrders = PierreAlgorithm.dynamicDistance(orders, mandatoryOrder);
        for (Order order : selectedOrders) {
            System.out.println(order.id());
        }
        System.out.println("Distance: " + Order.totalDeliveryDistance(selectedOrders));
    }

    // ------------------------------------------------------------------------------------------------------------------
    // Genetic algorithm with discount criterion
    // ------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGeneticDiscountCorrectSelection() {
        // Test to ensure that the genetic algorithm selects the correct orders
        ArrayList<Order> orders = geneticTestSuite;
        Order mandatoryOrder = orders.remove(0);
        ArrayList<Order> selectedOrders = PierreAlgorithm.geneticDiscount(orders, mandatoryOrder, true);
    }
}

