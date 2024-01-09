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
    public void testBruteForceTimeSameSize() {
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(bruteForceTestSuite);
        assertEquals(bruteForceTestSuite.size(), optimizedOrders.size(), "List sizes should be identical");
    }

    @Test
    public void testBruteForceTimeSameElements() {
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(bruteForceTestSuite);
        Set<Order> originalSet = new HashSet<>(bruteForceTestSuite);
        Set<Order> optimizedSet = new HashSet<>(optimizedOrders);
        assertEquals(originalSet, optimizedSet, "Elements in both lists should be identical");
    }

    @Test
    public void testBruteForceTimeOrderChange() {
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(bruteForceTestSuite);
        assertNotEquals(bruteForceTestSuite, optimizedOrders, "Order of elements should be optimized");
    }

    @Test
    public void testBruteForceTimeEmptyList() {
        ArrayList<Order> emptyList = new ArrayList<>();
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(emptyList);
        assertTrue(optimizedOrders.isEmpty(), "Optimized list should be empty for an empty input list");
    }

    @Test
    public void testBruteForceTimeSingleElement() {
        ArrayList<Order> singleElementList = new ArrayList<>();
        Order singleOrder = new Order(0, new GPS(48.6199, 1.98761), LocalDateTime.now());
        singleElementList.add(singleOrder);
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(singleElementList);
        assertEquals(1, optimizedOrders.size(), "Optimized list should contain one element for a " +
                "single-element input list");
        assertEquals(singleOrder, optimizedOrders.get(0), "Element in the optimized list should be identical" +
                " to the input element");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // greedy algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void testGreedyTimeCorrectSelection() {
        // Test to ensure that the greedy algorithm selects the correct orders
        int mandatoryOrderIndex = 0;
        ArrayList<Order> selectedOrders = PierreAlgorithm.greedyTime(greedyTestSuite, mandatoryOrderIndex);
        // Add assertions to check if the selected orders are the expected ones
    }

    @Test
    public void testGreedyTimeListSize() {
        // Test to ensure that the greedy algorithm returns a list of the correct size
        int mandatoryOrderIndex = 0;
        ArrayList<Order> selectedOrders = PierreAlgorithm.greedyTime(greedyTestSuite, mandatoryOrderIndex);
        assertEquals(5, selectedOrders.size(), "List should contain 5 orders " +
                "(1 mandatory + 4 closest)");
    }

    @Test
    public void testGreedyTimeEmptyList() {
        // Test to handle empty input list
        ArrayList<Order> emptyList = new ArrayList<>();
        int mandatoryOrderIndex = 0; // This index won't be used as the list is empty
        assertThrows(IllegalArgumentException.class, () -> {
            PierreAlgorithm.greedyTime(emptyList, mandatoryOrderIndex);
        }, "An exception should be thrown for an empty input list");
    }

    @Test
    public void testGreedyTimeSingleElement() {
        // Test with a single-element list
        ArrayList<Order> singleElementList = new ArrayList<>();
        Order singleOrder = new Order(0, new GPS(48.6199, 1.98761), LocalDateTime.now());
        singleElementList.add(singleOrder);
        int mandatoryOrderIndex = 0; // Only one element in the list
        assertThrows(IllegalArgumentException.class, () -> {
            PierreAlgorithm.greedyTime(singleElementList, mandatoryOrderIndex);
        }, "An exception should be thrown for an empty input list");
    }

}
