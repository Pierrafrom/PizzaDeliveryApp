package com.pizzadelivery.model;

import com.pizzadelivery.utils.List;

import java.time.LocalDateTime;

public class Delivery_Person {

    public enum SortCriteria {
        SHORTEST_GREEDY,
        SHORTEST_BRUTE_FORCE,
    }

    private final int id;
    private final String name;
    private final String firstName;
    private boolean available;
    private List<Order> orders;
    public final static int MAX_ORDERS = 5;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.71177037935438, 2.1705388889827137); // IUT ORSAY

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
        this.sortOrders(SortCriteria.values()[bestCriteria], this.getOrders());
    }

    private int sortOrders(SortCriteria criteria) {
        return switch (criteria) {
            case SHORTEST_BRUTE_FORCE -> AlgoPierre.sortOrdersByShortestBruteForce(this.getOrders().clone());
            case SHORTEST_GREEDY -> AlgoPierre.sortOrdersByShortestGreedy(this.getOrders().clone());
        };
    }

    private void sortOrders(SortCriteria criteria, List<Order> orders) {
        switch (criteria) {
            case SHORTEST_BRUTE_FORCE -> AlgoPierre.sortOrdersByShortestBruteForce(orders);
            case SHORTEST_GREEDY -> AlgoPierre.sortOrdersByShortestGreedy(orders);
        }
    }

    public static void main(String[] args) {
        // Création d'une personne de livraison
        Delivery_Person deliveryPerson = new Delivery_Person(1, "Doe", "John");

        // Ajout de quelques commandes
        deliveryPerson.addOrder(new Order(1, new GPS(48.715, 2.174), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(5, new GPS(48.713, 2.172), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(2, new GPS(48.712, 2.171), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(3, new GPS(48.714, 2.173), LocalDateTime.now()));
        deliveryPerson.addOrder(new Order(4, new GPS(48.716, 2.175), LocalDateTime.now()));


        // Tri des commandes en utilisant le meilleur critère
        //deliveryPerson.sortWithBestCriteria();
        AlgoPierre.sortOrdersByShortestBruteForce(deliveryPerson.getOrders());

    }
}
