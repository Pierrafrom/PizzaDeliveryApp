package com.pizzadelivery.model;

public class AlgoRemi {
    public int bestDelivery(ArrayList<Order> orders, GPS position) {
        if(orders.size() > MAX_STORAGE_DM){
            System.out.println("You have to much orders !");
            return -1;
        }
        ArrayList<Integer> note = new ArrayList<>();
        note.add(5);
        ArrayList<GPS> result = bestDeliveryRecursive(new ArrayList<>(orders), position, 0, note);
        ArrayList<Order> temps = new ArrayList<>();
        for(int i = 0; i < result.size(); i++){
            for(int j = 0; j < orders.size(); j++){
                if(result.get(i).equals(orders.get(j).getGPS())){
                    temps.add(orders.get(j));
                }
            }
        }
        orders.clear();
        orders.addAll(temps);
        return (int) ((note.get(0)/5.0)*10);
    }

    private ArrayList<GPS> bestDeliveryRecursive(List<Order> orders, GPS position, double elapsedTime, ArrayList<Integer> note) {
        if (orders.isEmpty()) {
            return new ArrayList<>(); // no more pizzas to deliver
        }
        orders.sort(Comparator.comparingDouble(order -> position.flightDistanceKM(order.getGPS())));
        Order nextOrder = orders.get(0);
        double distance = position.flightDistanceKM(nextOrder.getGPS());
        double deliveryTime = distance / AVGspeed * 60; // Temps de livraison en minutes
        double remainingTime = nextOrder.getRestTime() - elapsedTime;
        /* DEBUG
        System.out.println("Temps restant: " + formatDouble(remainingTime, 2));
        System.out.println("Temps de livraison: " + formatDouble(deliveryTime, 2));
        */
        ArrayList<GPS> result = new ArrayList<>();
        if (remainingTime > 0) {
            // the pizza can't be delivered on time
            result.add(nextOrder.getGPS());
            // create a new list of pizzas without the first one
            List<Order> remainingOrders = new ArrayList<>(orders.subList(1, orders.size()));

            // recution to iterate on the remaining pizzas
            ArrayList<GPS> remainingDelivery = bestDeliveryRecursive(remainingOrders, nextOrder.getGPS(), elapsedTime + deliveryTime, note);
            result.addAll(remainingDelivery);
        } else {
            // the pizza cannot be delivered on time
            int n = note.get(0);
            n--;
            note.set(0,n);
            result.addAll(bestDeliveryRecursive(orders.subList(1, orders.size()), position, elapsedTime,note));
            result.add(nextOrder.getGPS());
        }
        return result;
    }
}
