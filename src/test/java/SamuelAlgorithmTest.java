import com.pizzadelivery.model.Order;
import com.pizzadelivery.model.PierreAlgorithm;
import com.pizzadelivery.model.SamuelAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class SamuelAlgorithmTest extends AlgorithmTest{
    @Test
    public void testBruteForceDiscount() {
        ArrayList<Order> optimizedOrders = SamuelAlgorithm.bruteForceDiscount(bruteForceTestSuite);
        System.out.print("brute force : ");
        for (Order order : optimizedOrders) {
            System.out.print(order.id() + " ");
        }
        System.out.println();
        assertEquals(bruteForceTestSuite.size(), optimizedOrders.size(), "List sizes should be identical");
    }

    @Test
    public void testGreedyDistance() {
        // Test to ensure that the greedy algorithm selects the correct orders
        int mandatoryOrderIndex = 0;
        ArrayList<Order> orders = greedyTestSuite;
        Order mandatoryOrder = orders.remove(mandatoryOrderIndex);
        ArrayList<Order> bestCombination = SamuelAlgorithm.greedyDistance(orders, mandatoryOrder);
        System.out.print("greedy : ");
        for (Order order : bestCombination) {
            System.out.print(order.id() + " ");
        }
        System.out.println();
        assertEquals(5, bestCombination.size());
    }

    @Test
    public void testDynamicDiscount() {
        // Test to ensure that the greedy algorithm selects the correct orders
        int mandatoryOrderIndex = 0;
        ArrayList<Order> orders = dynamicTestSuite;
        Order mandatoryOrder = orders.remove(mandatoryOrderIndex);
        ArrayList<Order> bestCombination = SamuelAlgorithm.dynamicDiscount(orders, mandatoryOrder);
        System.out.print("dynamic : ");
        for (Order order : bestCombination) {
            System.out.print(order.id() + " ");
        }
        System.out.println();
        assertEquals(5, bestCombination.size());
    }

    @Test
    public void testGeneticTime() {
        // Test to ensure that the greedy algorithm selects the correct orders
        int mandatoryOrderIndex = 0;
        ArrayList<Order> orders = geneticTestSuite;
        Order mandatoryOrder = orders.remove(mandatoryOrderIndex);
        ArrayList<Order> bestCombination = SamuelAlgorithm.geneticTime(orders, 15, 10, mandatoryOrder);
        System.out.print("genetic : ");
        for (Order order : bestCombination) {
            System.out.print(order.id() + " ");
        }
        System.out.println();
        assertEquals(5, bestCombination.size());
    }
}
