package com.pizzadelivery.model;

import java.util.ArrayList;

import com.pizzadelivery.model.GPS;

public class Pizzeria {
    private ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294, 2.165678);
    public static final int ORDER_MAX_WAIT = 30;

    public Pizzeria() { // We will have to change the constructor, it is just an example for now
        orders = new ArrayList<>();
    }

    public void run() {
        //implement the method
        System.out.println("ouais");
    }

}
