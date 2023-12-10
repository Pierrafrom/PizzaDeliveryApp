package com.pizzadelivery.model;

import com.pizzadelivery.utils.List;

import java.time.LocalDateTime;

public class Delivery_Person {

    public enum SortCriteria {
        DISTANCE,
        TIME,
        CLOSEST,
        NO_DISCOUNT,
        SHORTEST,

    }

    private final int id;
    private final String name;
    private final String firstName;
    private boolean available;
    private List<Order> orders;
    private final static int MAX_ORDERS = 5;
    private final GPS PIZZERIA_LOCATION = new GPS(48.71177037935438, 2.1705388889827137); // IUT ORSAY

    public Delivery_Person(int id, String name, String firstName) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.available = true;
        this.orders = new List<>(null, null, MAX_ORDERS);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }

    public String toString() {
        StringBuilder ordersString = new StringBuilder("[");
        for (int i = 0; i < orders.length(); i++) {
            ordersString.append(orders.get(i).toString());
            if (i < orders.length() - 1) {
                ordersString.append(", ");
            }
        }
        ordersString.append("]");

        return "{\n\"Delivery_Person\": {" +
                "\n\"id\": " + id +
                ", \n\"name\": \"" + name + "\"" +
                ", \n\"firstName\": \"" + firstName + "\"" +
                ", \n\"available\": " + available +
                ", \n\"orders\": " + ordersString +
                "}\n}";
    }

    public void sortWithBestCriteria() {
        int bestCriteria = -1;
        int bestCriteriaMark = -1;
        for (SortCriteria criteria : SortCriteria.values()) {
            int mark = this.sortOrders(criteria);
            if (mark > bestCriteriaMark) {
                bestCriteria = criteria.ordinal();
                bestCriteriaMark = mark;
            }
        }
        this.sortOrders(SortCriteria.values()[bestCriteria]);
    }

    public int sortOrders(SortCriteria criteria) {
        return switch (criteria) {
            case DISTANCE -> this.sortOrdersByDistance();
            case TIME -> this.sortOrdersByTime();
            case CLOSEST -> this.sortOrdersByClosest();
            case NO_DISCOUNT -> this.sortOrdersByNoDiscount();
            case SHORTEST -> this.sortOrdersByShortest();
        };
    }

    private int sortOrdersByDistance() {
        return 0;
    }

    private int sortOrdersByTime() {
        return 0;
    }

    private int sortOrdersByClosest() {
        return 0;
    }

    private int sortOrdersByNoDiscount() {
        return 0;
    }

    private int[] findShortest(GPS location, List<Order> orders) {
        // Check if the order list is empty or null
        if (orders == null) {
            // If the list is empty or null, it's not possible to find the shortest order
            throw new IllegalArgumentException("Order list cannot be empty.");
        }

        int length = orders.length();

        if (length == 0) {
            // If the list is empty, it's not possible to find the shortest order
            throw new IllegalArgumentException("Order list cannot be empty.");
        } else if (orders.length() == 1) {
            // If the list contains only one element, it's the shortest order
            return new int[]{0, (int) Math.round(location.timeTravel(orders.get(0).location()))};
        }

        int shortestIndex = 0;
        int shortestTimeTravel;

        try {
            // Initialize shortestTimeTravel with the first element in the list
            shortestTimeTravel = (int) Math.round(location.timeTravel(orders.get(0).location()));

            for (int i = 1; i < length; i++) {
                double timeTravel = location.timeTravel(orders.get(i).location());
                if (timeTravel < shortestTimeTravel) {
                    // If a shorter time travel is found, update the shortest index and time
                    shortestIndex = i;
                    shortestTimeTravel = (int) Math.round(timeTravel);
                }
            }
        } catch (NullPointerException ex) {
            // Handle the case where getHead() returns null
            // This should not happen if the list is non-empty, so it's an unexpected error
            throw new IllegalStateException("Unexpected error accessing list elements", ex);
        }

        // Return the shortest index and its corresponding time travel as an array
        return new int[]{shortestIndex, shortestTimeTravel};
    }


    private int sortOrdersByShortestRec(GPS currentLocation, List<Order> orders, List<Order> sortedOrders, int timeTravel) {
        if (orders.length() == 0) {
            return timeTravel;
        }

        int[] res = this.findShortest(currentLocation, orders);
        int shortestIndex = res[0];
        int shortestTimeTravel = res[1];
        Order shortestOrder = orders.get(shortestIndex);
        orders.remove(shortestIndex);
        sortedOrders.add(shortestOrder);
        timeTravel += shortestTimeTravel;

        return this.sortOrdersByShortestRec(shortestOrder.location(), orders, sortedOrders, timeTravel);
    }

    private int sortOrdersByShortest() {
        List<Order> sortedOrders = new List<>(null, null, MAX_ORDERS);
        int timeTravel = 0;
        timeTravel = this.sortOrdersByShortestRec(PIZZERIA_LOCATION, this.getOrders(), sortedOrders, timeTravel);
        this.setOrders(sortedOrders);

        if (timeTravel <= DeliveryRating.NOTE_10_MAX_TIME) {
            return 10;
        } else if (timeTravel <= DeliveryRating.NOTE_9_MAX_TIME) {
            return 9;
        } else if (timeTravel <= DeliveryRating.NOTE_8_MAX_TIME) {
            return 8;
        } else if (timeTravel <= DeliveryRating.NOTE_7_MAX_TIME) {
            return 7;
        } else if (timeTravel <= DeliveryRating.NOTE_6_MAX_TIME) {
            return 6;
        } else if (timeTravel <= DeliveryRating.NOTE_5_MAX_TIME) {
            return 5;
        } else if (timeTravel <= DeliveryRating.NOTE_4_MAX_TIME) {
            return 4;
        } else if (timeTravel <= DeliveryRating.NOTE_3_MAX_TIME) {
            return 3;
        } else if (timeTravel <= DeliveryRating.NOTE_2_MAX_TIME) {
            return 2;
        } else if (timeTravel <= DeliveryRating.NOTE_1_MAX_TIME) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        // Création d'une personne de livraison
        Delivery_Person deliveryPerson = new Delivery_Person(1, "Doe", "John");

        // Ajout de quelques commandes
        deliveryPerson.addOrder(new Order(1, new GPS(48.712, 2.171), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(2, new GPS(48.713, 2.172), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(3, new GPS(48.714, 2.173), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(4, new GPS(48.715, 2.174), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(5, new GPS(48.716, 2.175), LocalDateTime.now()));

        System.out.println(deliveryPerson);

        // Tri des commandes en utilisant le meilleur critère
        deliveryPerson.sortWithBestCriteria();

        // Affichage des commandes triées
        List<Order> sortedOrders = deliveryPerson.getOrders();
        for (int i = 0; i < sortedOrders.length(); i++) {
            System.out.println(sortedOrders.get(i));
        }
    }
}
