package com.pizzadelivery.model;

import java.util.ArrayList;

public class Pizzeria {
    private ArrayList<DeliveryPerson> deliveryTeam;
    private ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294, 2.165678);
    public static final int ORDER_MAX_WAIT = 30;
    public static final int DELIVERY_TEAM_SIZE = 1000;
    public static final int TIME_TO_WAIT_BEFORE_REFRESH = 15;

    public Pizzeria() {
        orders = new ArrayList<>();
        deliveryTeam = new ArrayList<>();
        for(int i = 0; i<DELIVERY_TEAM_SIZE; i++) {
            deliveryTeam.add(new DeliveryPerson(i,"kin","tama"));
            System.out.print(" delivery man n°"+i);
        }
        System.out.println();
    }

    public void run() {
        ArrayList<Order> combination;
        while (true) {
            if (!orders.isEmpty()) {
                System.out.println("Orders in the wait-list :");
                for (Order order : orders) {
                    System.out.print(order.id() + " ");
                }
                System.out.println();
            }
            for (int i = 0; i < orders.size(); i++) {
                if (orders.get(i).isCritical()) {
                    try {
                        DeliveryPerson dpInCharge = firstAvailableDP();
                        if (dpInCharge == null) {
                            throw new RuntimeException("No available Delivery person");
                        }
                        combination = new ArrayList<>(
                                SailorManAlgorithm.selectAlgorithm(new ArrayList<>(orders), orders.get(i)));
                        SailorManAlgorithm.sortOrders(combination);
                        for (Order order : combination) {
                            orders.remove(order);
                            System.out.print(order.id() + " ");
                        }
                        System.out.println();
                        dpInCharge.simulateDelivery(combination);
                    } catch (RuntimeException e) {
                        System.out.println("The orders have not been taken because of the lack of Delivery person");
                    }
                }
            }

            try {
                // Sleep for 15 seconds after processing orders
                Thread.sleep(TIME_TO_WAIT_BEFORE_REFRESH*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private DeliveryPerson firstAvailableDP() {
        for(DeliveryPerson dp : deliveryTeam) {
            if (dp.isAvailable()) {
               return dp;
            }
        }
        return null;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

}
