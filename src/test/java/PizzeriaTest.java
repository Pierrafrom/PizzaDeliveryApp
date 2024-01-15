import com.pizzadelivery.model.Pizzeria;
import org.junit.jupiter.api.Test;


public class PizzeriaTest extends AlgorithmTest {

    @Test
    public void testRunFiveOrders() {
        // Create a Pizzeria instance
        Pizzeria pizzeria = new Pizzeria();

        pizzeria.setOrders(bruteForceTestSuite);

        // Call the run method
        pizzeria.run();
    }

    @Test
    public void testRunFifteenOrders() {
        // Create a Pizzeria instance
        Pizzeria pizzeria = new Pizzeria();

        pizzeria.setOrders(dynamicTestSuite);

        // Call the run method
        pizzeria.run();
    }

    @Test
    public void testRunThirtyFiveOrders() {
        // Create a Pizzeria instance
        Pizzeria pizzeria = new Pizzeria();

        pizzeria.setOrders(greedyTestSuite);

        // Call the run method
        pizzeria.run();
    }

    @Test
    public void testRunSeventyOrders() {
        // Create a Pizzeria instance
        Pizzeria pizzeria = new Pizzeria();

        pizzeria.setOrders(geneticTestSuite);

        // Call the run method
        pizzeria.run();
    }
}
