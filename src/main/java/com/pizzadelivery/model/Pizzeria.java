package com.pizzadelivery.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class Pizzeria {
    private final ArrayList<DeliveryPerson> deliveryTeam;
    private final ArrayList<Order> orders;
    public static final GPS PIZZERIA_LOCATION = new GPS(48.7117294, 2.165678);
    public static final int ORDER_MAX_WAIT = 30;
    public static final int DELIVERY_TEAM_SIZE = 15;
    public static final int TIME_TO_WAIT_BEFORE_REFRESH = 5;

    public Pizzeria() {
        orders = new ArrayList<>();
        deliveryTeam = new ArrayList<>();
        for (int i = 0; i < DELIVERY_TEAM_SIZE; i++) {
            deliveryTeam.add(new DeliveryPerson(i, "DeliveryPerson " + i, "DP"));
        }
    }

    public void run() {
        while (true) {
            processOrders();
        }
    }

    public void processOrders() {
        Iterator<Order> iterator = orders.iterator();
        ArrayList<Order> toRemove = new ArrayList<>();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.isCritical()) {
                System.out.println("Order " + order.id() + " is critical and must be delivered");
                ArrayList<Order> copy = new ArrayList<>(orders);
                copy.removeAll(toRemove);
                ArrayList<Order> ordersToDeliver = SailorManAlgorithm.selectAlgorithm(copy, order);

                assignOrdersToDeliveryPerson(ordersToDeliver);

                toRemove.addAll(ordersToDeliver); // Ajoute les commandes à la liste des éléments à supprimer

                System.out.println();
                try {
                    Thread.sleep(TIME_TO_WAIT_BEFORE_REFRESH * 1000);
                } catch (InterruptedException e) {
                    Logger logger = Logger.getLogger(Pizzeria.class.getName());
                    logger.warning(e.getMessage());
                }
            }
        }

        // Suppression des éléments marqués
        orders.removeAll(toRemove);

        displayStatus();
    }


    private void assignOrdersToDeliveryPerson(ArrayList<Order> ordersToDeliver) {
        for (DeliveryPerson person : deliveryTeam) {
            if (person.isAvailable() && !ordersToDeliver.isEmpty()) {
                person.deliverOrders(new ArrayList<>(ordersToDeliver));
                return;
            }
        }
    }

    private void displayStatus() {
        System.out.println("---------------------- " + orders.size() + " orders left" + " ----------------------");
        for (Order order : orders) {
            System.out.print(order.id() + " ");
        }
        System.out.println("\nDelivery team:");
        for (DeliveryPerson person : deliveryTeam) {
            System.out.println(person.id() + " " + person.isAvailable());
        }
        System.out.println("------------------------------------------------------------");
        try {
            Thread.sleep(TIME_TO_WAIT_BEFORE_REFRESH * 1000);
        } catch (InterruptedException e) {
            Logger logger = Logger.getLogger(Pizzeria.class.getName());
            logger.warning(e.getMessage());
        }
    }


    // on doit d'abbord parcourir la liste des commandes
    // si une commande est critique
    // on affiche un message pour dire qu'elle doit etre livrée
    // on appelle la fonction SelectAlgorithm de SailorManAlgorithm qui nous renvoie une arraylist de 5 commandes a livrer
    // on parcours la liste des livreurs
    // si un livreur est disponible on lui affecte la commande
    // le livreur doit alors s'exectuer dans un nouveau thread et ne pas affecter le thread principal
    // il doit mettre a jour sa disponibilité
    // et il doit attendre le temps de livraison du trajet avant de se rendre disponible
    // pendant ce temps le thread principal doit supprimer les commandes affectées de la liste des commandes
    // puis il doit attendre 5 secondes avant de continuer a affecter des commandes
    // si aucun livreur n'est disponible on affiche un message puis on attend jusqu'a ce qu'un livreur soit disponible en rafraiichissant la liste des livreurs toutes les 5 secondes
    // on affiche la liste des commandes restantes a livrer
    // on affiche la liste des livreurs disponibles
    // on attend 5 secondes et on recommence

    public void setOrders(ArrayList<Order> orders) {
        synchronized (this.orders) {
            this.orders.addAll(orders);
        }
    }
}
