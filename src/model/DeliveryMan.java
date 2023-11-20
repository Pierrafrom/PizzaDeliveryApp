package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DeliveryMan {
    private int idDM;
    private String nomDM;
    private String prenomDM;
    private boolean available;
    private final int STORAGE_DM = 5;
    private final int AVGspeed = 45;

    public DeliveryMan(int id, String nom, String prenom, boolean available) {
        this.idDM = id;
        this.nomDM = nom;
        this.prenomDM = prenom;
        this.available = available;
    }
    public int getIdDM(){
        return this.idDM;
    }
    public String getNom(){
        return this.nomDM;
    }
    public String getPrenom(){
        return this.prenomDM;
    }
    public boolean getAvailable(){
        return this.available;
    }
    public void changeAvailable(){
        this.available = !this.available;
    }
    public String toString() {
        return "DeliveryMan [idDM=" + idDM + ", nomDM=" + nomDM + ", prenomDM=" + prenomDM + ", available=" + available + "]";
    }

    public ArrayList<GPS> bestDelivery(ArrayList<Pizza> pizzas, GPS position) {
        // Créer une copie de la liste des pizzas pour ne pas modifier l'original
        ArrayList<Pizza> pizzasCopy = new ArrayList<>(pizzas);

        // Calculer la distance entre la position actuelle du livreur et toutes les coordonnées GPS des pizzas
        pizzasCopy.sort(Comparator.comparingDouble(pizza -> position.distanceKM(pizza.getGPS())));

        // Sélectionner les pizzas en fonction de la distance et du temps restant
        ArrayList<GPS> sortedGPS = new ArrayList<>();
        double remainingTime = 0;

        for (Pizza pizza : pizzasCopy) {
            double distance = position.distanceKM(pizza.getGPS());
            double deliveryTime = distance / AVGspeed * 60; // Temps de livraison en minutes
            remainingTime += pizza.getRestTime();

            // Vérifier si la pizza peut être livrée avant que son temps restant n'atteigne zéro
            if (remainingTime <= deliveryTime) {
                sortedGPS.add(pizza.getGPS());
            } else {
                break; // Sortir de la boucle si la pizza ne peut pas être livrée à temps
            }
        }

        return sortedGPS;
    }
}
