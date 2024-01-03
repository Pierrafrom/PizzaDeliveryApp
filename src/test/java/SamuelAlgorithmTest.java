import com.pizzadelivery.model.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SamuelAlgorithmTest {

    @Test
    public void bruteForceDiscount_shouldReturnBestDiscountOrders() throws RateLimitExceededException {
        // Arrange
        GPS gps1 = new GPS(48.8080723,2.0461625); // Fontenay-le-Fleury coordinates
        GPS gps2 = new GPS(48.7110453470687, 2.1714970680132244); // Orsay coordinates
        GPS gps3 = new GPS(48.745532, 2.117367); // Toussus le Noble coordinates
        GPS gps4 = new GPS(48.8416525,2.2731171); // Paris 15th arrondissment coordinates
        GPS gps5 = new GPS(48.7370955,2.0749577); // Chateaufort coordinates

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
        ArrayList<ArrayList<Order>> allCombinations = new ArrayList<>();
        SamuelAlgorithm.generateCombinations(orders, new ArrayList<>(), allCombinations);

        ArrayList<Order> bestCombination = SamuelAlgorithm.bruteForceDiscount(allCombinations);
        System.out.println("Meilleure combinaison pour réduction : ");
        System.out.println("Nombre de tickets de réduction : " + Pizzeria.numberOfDiscount(bestCombination));


        // Assert
        // You need to adjust the assertions based on the expected behavior of your algorithm
        // In this example, we are just checking if the returned list is not null
        // and that it does not exceed the original number of orders
        assertEquals(true, bestCombination != null);
        assertEquals(true, bestCombination.size() <= orders.size());
    }
}
