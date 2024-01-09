import com.pizzadelivery.model.Order;
import helper.TestSuiteGenerator;

import java.util.ArrayList;

public class AlgorithmTest {
    protected ArrayList<Order> bruteForceTestSuite;
    protected ArrayList<Order> greedyTestSuite;
    protected ArrayList<Order> dynamicTestSuite;
    protected ArrayList<Order> geneticTestSuite;

    public AlgorithmTest() {
        bruteForceTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.BRUTE_FORCE);
        greedyTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.GREEDY);
        dynamicTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.DYNAMIC);
        geneticTestSuite = TestSuiteGenerator.generateOrders(TestSuiteGenerator.NUMBER_OF_ORDERS.GENETIC);
    }
}
