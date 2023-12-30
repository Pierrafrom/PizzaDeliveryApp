package com.pizzadelivery.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record Order(int id, GPS location, LocalDateTime orderDate) {

    public static final int ORDER_MAX_WAIT = 30; // Exemple, veuillez ajuster selon vos besoins

    public static int numberOfDiscount(ArrayList<Order> orders) {



        return discountCount;
    }
}
