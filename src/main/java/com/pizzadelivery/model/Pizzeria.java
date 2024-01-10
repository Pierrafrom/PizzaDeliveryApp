package com.pizzadelivery.model;

import java.util.ArrayList;

public class Pizzeria {
    private ArrayList<DeliveryPerson> deliveryTeam;
    private ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294, 2.165678);
    public static final int ORDER_MAX_WAIT = 30;
    public static final int DELIVERY_TEAM_SIZE = 5;

    public Pizzeria() { // We will have to change the constructor, it is just an example for now
        orders = new ArrayList<>();
        deliveryTeam = new ArrayList<>();
        for(int i = 0; i<DELIVERY_TEAM_SIZE; i++) {
            deliveryTeam.add(new DeliveryPerson(i,"kin","tama"));
        }
    }

    public void run() {
        ArrayList<Order> combination = null;
        while (true) {
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).isCritical()) {
                    combination = SailorManAlgorithm.selectAlgorithm(orders, orders.get(i));
                    SailorManAlgorithm.sortOrders(combination);
                    for (Order order : combination) {
                        System.out.print(order.id() + " ");
                    }
                    System.out.println();
                    try {
                        DeliveryPerson dpInCharge = firstAvailableDP();
                        if (dpInCharge == null) {
                            throw new RuntimeException("No available Delivery person");
                        }

                        dpInCharge.setOrders(combination);
                        dpInCharge.setAvailable(false);
                        dpInCharge.simulateDelivery();
                    } catch (RuntimeException e) {
                        System.out.println("The orders have not been taken because of the lack of Delivery person");
                    }
                }
            }
        }
    }

    private DeliveryPerson firstAvailableDP() {
        for(DeliveryPerson dp : this.deliveryTeam) {
            if (dp.isAvailable()) {
               return dp;
            }
        }
        return null;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
    public ArrayList<Order> getOrders() {
        return this.orders;
    }
}
