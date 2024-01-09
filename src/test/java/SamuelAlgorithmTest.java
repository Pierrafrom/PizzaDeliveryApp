import com.pizzadelivery.model.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SamuelAlgorithmTest {
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
    Order order6 = new Order(6, gps1, LocalDateTime.now());

    @Test
    public void testBruteForceDiscount() throws RateLimitExceededException {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);
        ArrayList<ArrayList<Order>> allCombinations = new ArrayList<>();
        SamuelAlgorithm.generateCombinations(orders, allCombinations);

        ArrayList<Order> bestCombination = SamuelAlgorithm.bruteForceDiscount(allCombinations);
        System.out.println("Meilleure combinaison pour réduction : ");
        for (Order order : bestCombination) {
            System.out.print(order.id()+" ");
        }
        System.out.println();
        System.out.println("Nombre de tickets de réduction : " + Pizzeria.numberOfDiscount(bestCombination));


        // Assert
        // You need to adjust the assertions based on the expected behavior of your algorithm
        // In this example, we are just checking if the returned list is not null
        // and that it does not exceed the original number of orders
        ArrayList<Order>test = new ArrayList<Order>();
        test.add(order1);
        test.add(order2);
        test.add(order3);
        test.add(order4);
        test.add(order5);

        assertEquals(true, bestCombination.get(0) == test.get(0));
        assertEquals(true, bestCombination.get(1) == test.get(1));
        assertEquals(true, bestCombination.get(2) == test.get(2));
        assertEquals(true, bestCombination.get(3) == test.get(3));
        assertEquals(true, bestCombination.get(4) == test.get(4));
        assertEquals(true, bestCombination.size() <= orders.size());
    }
    @Test
    public void testGreedyDiscount() throws RateLimitExceededException {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);
        orders.add(order6);
        orders.add(order1);
        orders.add(order6);
        orders.add(order4);
        orders.add(order2);
        orders.add(order5);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);
        orders.add(order6);
        orders.add(order3);
        orders.add(order6);
        orders.add(order4);
        orders.add(order2);
        orders.add(order5);


        ArrayList<Order> bestCombination = SamuelAlgorithm.greedyDistance(orders);
        for (Order order : bestCombination) {
            System.out.print(order.id()+" ");
        }
        System.out.println();
        // Assert
        // You need to adjust the assertions based on the expected behavior of your algorithm
        // In this example, we are just checking if the returned list is not null
        // and that it does not exceed the original number of orders

        assertEquals(true, bestCombination.size() == 5);

    }



    @Test
    public void testDynamicDiscount() throws RateLimitExceededException {
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);
        orders.add(order6);
        orders.add(order1);
        orders.add(order6);
        orders.add(order4);
        orders.add(order2);
        orders.add(order5);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);
        orders.add(order6);
        orders.add(order1);
        orders.add(order3);
        orders.add(order6);
        orders.add(order4);
        orders.add(order2);
        orders.add(order5);

        ArrayList<Order> result = SamuelAlgorithm.dynamicDiscount(orders);

        for (Order order : result) {
            System.out.print(order.id()+" ");
        }
        System.out.println();
        ArrayList<Order>test = new ArrayList<Order>();
        test.add(order1);
        test.add(order5);
        test.add(order6);
        test.add(order1);
        test.add(order6);

        assertEquals(true, result.get(0) == test.get(0));
        assertEquals(true, result.get(1) == test.get(1));
        assertEquals(true, result.get(2) == test.get(2));
        assertEquals(true, result.get(3) == test.get(3));
        assertEquals(true, result.get(4) == test.get(4));
        assertEquals(true, result.size() == 5);
        // Ajoutez d'autres assertions en fonction de vos besoins
    }

}
