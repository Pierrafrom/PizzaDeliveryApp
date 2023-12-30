package com.pizzadelivery.model;

import java.util.ArrayList;
import com.pizzadelivery.model.GPS;

public class Pizzeria {
    private ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(-45.26,42.64);
    public Pizzeria(){ // We will have to change the constructor, it is just an example for now
        orders = new ArrayList<>();
    }

    public void run() {
        //implement the method
        System.out.println("ouais");
    }
}
