import com.pizzadelivery.model.Pizzeria;
import org.junit.jupiter.api.Test;


public class PizzeriaTest extends AlgorithmTest {

    @Test
    public void testRun() {
        // Create a Pizzeria instance
        Pizzeria pizzeria = new Pizzeria();

        pizzeria.setOrders(bruteForceTestSuite);

        // Call the run method
        pizzeria.run();
    }
}
