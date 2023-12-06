package test;

import java.util.ArrayList;
import model.*;

public class testMainDist {
    public static void main(String[] args) {
        // Create a list of delivery addresses..
        GPS Samuel = new GPS(48.795385, 2.152272);
        GPS Pierre1 = new GPS(48.745385, 2.117144);
        GPS Pierre2 = new GPS(48.745385, 2.117145);
        GPS Buc = new GPS(48.774526, 2.122807);

        //double distancePierreSamuel = Pierre1.distanceKM(Samuel);
        //System.out.println(distancePierreSamuel+"km between Pierre and Samuel (bird flight)");

        // Create a list of pizzas.
        ArrayList<Pizza> pizzas = new ArrayList<>();
        Pizza pizza1 = new Pizza(Samuel, 10);
        Pizza pizza2 = new Pizza(Pierre1, 5);
        Pizza pizza3 = new Pizza(Pierre2, 4);
        Pizza pizza4 = new Pizza(Buc, 3);
        pizzas.add(pizza1);
        pizzas.add(pizza2);
        pizzas.add(pizza3);
        pizzas.add(pizza4);


        // Create a delivery man for the test of the bestDelivery method.
        DeliveryMan deliveryMan = new DeliveryMan(1, "John", "Doe", true);
        System.out.println(" ");
        System.out.println("REDUCTIONLESS DELIVERY WITH FAST BACK");
        // Get the best delivery patern in deliveryAddresses
        ArrayList<GPS> deliveredAddresses = deliveryMan.bestDelivery(pizzas, new GPS(48.739559173583984, 2.088090658187866));
        // Print the delivered addresses.
        for (GPS deliveredAddress : deliveredAddresses) {
            System.out.println(deliveredAddress);
        }
        System.out.println(" ");
        System.out.println("------------------------------------------------");


        //Creat a delivery man for the test of the fast delivery method.
        DeliveryMan deliveryMan2 = new DeliveryMan(2, "Jane", "Doe", true);
        System.out.println(" ");
        System.out.println("FAST DELIVERY WITH FAST BACK");
        // Get the fastest delivery patern in deliveryAddresses2
        ArrayList<GPS> deliveredAddresses2 = deliveryMan2.FastDelivery(pizzas, new GPS(48.739559173583984, 2.088090658187866));
        // Print the delivered addresses.
        for (GPS deliveredAddress : deliveredAddresses2) {
            System.out.println(deliveredAddress);
        }
        System.out.println(" ");
        System.out.println("------------------------------------------------");


        //Creat a delivery man for the test of the rest_time delivery method.
        DeliveryMan deliveryMan3 = new DeliveryMan(2, "Joe", "Doe", true);
        System.out.println(" ");
        System.out.println("FAST DELIVERY WITH FAST BACK");
        // Get the best restTime delivery patern in deliveryAddresses3
        ArrayList<GPS> deliveredAddresses3 = deliveryMan2.RestTimeDelivery(pizzas, new GPS(48.739559173583984, 2.088090658187866));
        // Print the delivered addresses.
        for (GPS deliveredAddress : deliveredAddresses3) {
            System.out.println(deliveredAddress);
        }
        System.out.println(" ");
        System.out.println("------------------------------------------------");
    }
}
