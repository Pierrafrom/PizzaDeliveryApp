package test;

import java.util.ArrayList;
import model.*;

public class testMainDist {
    public static void main(String[] args) {
        // Create a list of delivery addresses.
        GPS addressSamuel = new GPS(48.795385, 2.152272);
        GPS addressPierre1 = new GPS(48.745385, 2.117144);
        GPS addressPierre2 = new GPS(48.745385, 2.117145);

        double distancePierreSamuel = addressPierre1.distanceKM(addressSamuel);
        System.out.println(distancePierreSamuel+"km between Pierre and Samuel (bird flight)");

        // Create a list of pizzas.
        ArrayList<Pizza> pizzas = new ArrayList<>();
        Pizza pizza1 = new Pizza(addressSamuel, 10);
        Pizza pizza2 = new Pizza(addressPierre1, 20);
        Pizza pizza3 = new Pizza(addressPierre2, 30);
        pizzas.add(pizza1);
        pizzas.add(pizza2);
        pizzas.add(pizza3);

        // Create a delivery man.
        DeliveryMan deliveryMan = new DeliveryMan(1, "John", "Doe", true);

        // Get the best delivery addresses.
        ArrayList<GPS> deliveredAddresses = deliveryMan.bestDelivery(pizzas, new GPS(48.739559173583984, 2.088090658187866));

        // Print the delivered addresses.
        for (GPS deliveredAddress : deliveredAddresses) {
            System.out.println(deliveredAddress);
        }
    }
}
