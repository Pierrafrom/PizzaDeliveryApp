import com.pizzadelivery.model.GPS;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GPSTest {

    @Test
    public void timeTravel_shouldReturnCorrectTime() {
        GPS gps1 = new GPS(48.8080723,2.0461625); // Fontenay-le-Fleury coordinates
        GPS gps2 = new GPS(48.7110453470687, 2.1714970680132244); // Orsay coordinates

        // Act
        double time = gps1.timeTravel(gps2);

        // Assert
        // You may need to update the expected value based on your API response
        assertEquals(28.0, time, 0.01);
    }

    @Test
    public void calculateDistance_shouldReturnCorrectDistance() {
        GPS gps1 = new GPS(48.8080723,2.0461625); // Fontenay-le-Fleury coordinates
        GPS gps2 = new GPS(48.7110453470687, 2.1714970680132244); // Orsay coordinates

        // Act
        double distance = gps1.calculateDistance(gps2);

        // Assert
        // You may need to update the expected value based on your API response
        assertEquals(26.0, distance, 0.1);

    }
}
