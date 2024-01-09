import com.pizzadelivery.helper.TestHelper;
import com.pizzadelivery.model.GPS;
import com.pizzadelivery.model.Order;
import com.pizzadelivery.model.PierreAlgorithm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PierreAlgorithmTest {

    @Test
    public void testBruteForceTimeSameSize() {
        ArrayList<Order> orders = TestHelper.generateOrders(TestHelper.NUMBER_OF_ORDERS.BRUTE_FORCE);
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(orders);
        assertEquals(orders.size(), optimizedOrders.size(), "List sizes should be identical");
    }

    @Test
    public void testBruteForceTimeSameElements() {
        ArrayList<Order> orders = TestHelper.generateOrders(TestHelper.NUMBER_OF_ORDERS.BRUTE_FORCE);
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(orders);
        Set<Order> originalSet = new HashSet<>(orders);
        Set<Order> optimizedSet = new HashSet<>(optimizedOrders);
        assertEquals(originalSet, optimizedSet, "Elements in both lists should be identical");
    }

    @Test
    public void testBruteForceTimeOrderChange() {
        ArrayList<Order> orders = TestHelper.generateOrders(TestHelper.NUMBER_OF_ORDERS.BRUTE_FORCE);
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(orders);
        assertNotEquals(orders, optimizedOrders, "Order of elements should be optimized");
    }

    @Test
    public void testBruteForceTimeEmptyList() {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(orders);
        assertTrue(optimizedOrders.isEmpty(), "Optimized list should be empty for an empty input list");
    }

    @Test
    public void testBruteForceTimeSingleElement() {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(0, new GPS(48.6199, 1.98761), LocalDateTime.now()));
        ArrayList<Order> optimizedOrders = PierreAlgorithm.bruteForceTime(orders);
        assertEquals(1, optimizedOrders.size(), "Optimized list should contain one element for a " +
                "single-element input list");
        assertEquals(orders.get(0), optimizedOrders.get(0), "Element in the optimized list should be" +
                " identical to the input element");
    }
}
