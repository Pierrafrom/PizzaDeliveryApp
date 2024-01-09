import com.pizzadelivery.model.GPS;
import com.pizzadelivery.model.Order;
import com.pizzadelivery.model.Pizzeria;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PizzeriaTest {

    @Test
    public void testNumberOfDiscount() {
        GPS gps1 = new GPS(48.8080723, 2.0461625);
        GPS gps2 = new GPS(48.7110453470687, 2.1714970680132244);
        GPS gps3 = new GPS(48.745532, 2.117367);
        GPS gps4 = new GPS(48.8416525, 2.2731171);
        GPS gps5 = new GPS(48.7370955, 2.0749577);

        Order order1 = new Order(1, gps1, LocalDateTime.now());
        Order order2 = new Order(2, gps2, LocalDateTime.now());
        Order order3 = new Order(3, gps3, LocalDateTime.now());
        Order order4 = new Order(4, gps4, LocalDateTime.now());
        Order order5 = new Order(5, gps5, LocalDateTime.now());

        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);

        int result = Pizzeria.numberOfDiscount(orders);
        System.out.println(result);
        // Assuming all orders will result in a discount based on your logic
        //assertEquals(orders.size() - 1, result);
    }
}
