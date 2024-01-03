package com.pizzadelivery.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.pizzadelivery.model.GPS;

public class Pizzeria {
    private ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294,2.165678);
    public static final int ORDER_MAX_WAIT = 30;

    public Pizzeria(){ // We will have to change the constructor, it is just an example for now
        orders = new ArrayList<>();
    }

    public void run() {
        //implement the method
        System.out.println("ouais");
    }

    public static int numberOfDiscount(ArrayList<Order> orders) throws RateLimitExceededException {
        int discountCount = 0;
        double deliveryTime = PIZZERIA_LOCATION.timeTravel(orders.get(0).location());
        System.out.println("order "+1+" delivered in "+deliveryTime);


        if (deliveryTime>ORDER_MAX_WAIT){
            discountCount++;
        }
        for (int i = 0; i < orders.size() - 1; i++) {
                Order previousOrder = orders.get(i);
                Order currentOrder = orders.get(i + 1);

                deliveryTime = previousOrder.location().timeTravel(currentOrder.location());
                System.out.println("order "+currentOrder.id()+" delivered in "+deliveryTime);

                if (deliveryTime>=ORDER_MAX_WAIT){
                    System.out.println("discount for "+currentOrder.id());
                    discountCount++;
                }


        }
        return discountCount;
    }
}
