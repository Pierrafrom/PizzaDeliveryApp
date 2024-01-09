import com.pizzadelivery.model.Order;
import helper.TestSuiteGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import java.util.ArrayList;

public class AlgorithmTest {
    protected ArrayList<Order> bruteForceTestSuite;
    protected ArrayList<Order> greedyTestSuite;
    protected ArrayList<Order> dynamicTestSuite;
    protected ArrayList<Order> geneticTestSuite;

    @BeforeEach
    public void setUp() {
        bruteForceTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.BRUTE_FORCE);
        greedyTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.GREEDY);
        dynamicTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.DYNAMIC);
        geneticTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.GENETIC);
    }

    @Test
    public void testBruteForceTestSuiteSize() {
        int expectedSize = 5;
        int actualSize = bruteForceTestSuite.size();
        assertEquals(expectedSize, actualSize, "Brute Force test suite size mismatch: Expected "
                + expectedSize + ", but got " + actualSize);
    }

    @Test
    public void testGreedyTestSuiteSize() {
        int expectedSize = 70;
        int actualSize = greedyTestSuite.size();
        assertEquals(expectedSize, actualSize, "Greedy test suite size mismatch: Expected "
                + expectedSize + ", but got " + actualSize);
    }

    @Test
    public void testDynamicTestSuiteSize() {
        int expectedSize = 15;
        int actualSize = dynamicTestSuite.size();
        assertEquals(expectedSize, actualSize, "Dynamic test suite size mismatch: Expected "
                + expectedSize + ", but got " + actualSize);
    }

    @Test
    public void testGeneticTestSuiteSize() {
        int expectedSize = 35;
        int actualSize = geneticTestSuite.size();
        assertEquals(expectedSize, actualSize, "Genetic test suite size mismatch: Expected "
                + expectedSize + ", but got " + actualSize);
    }

}
