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


    // -----------------------------------------------------------------------------------------------------------------
    // greedy algorithm with time criterion
    // -----------------------------------------------------------------------------------------------------------------


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
        ArrayList<Order> selectedOrders = PierreAlgorithm.geneticDiscount(orders, mandatoryOrder);
        // Add assertions to check if the selected orders are the expected ones
    }
}
