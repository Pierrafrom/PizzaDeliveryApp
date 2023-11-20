package model;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        return bestDeliveryRecursive(new ArrayList<>(pizzas), position, 0);
    }

    private ArrayList<GPS> bestDeliveryRecursive(List<Pizza> pizzas, GPS position, double elapsedTime) {
        if (pizzas.isEmpty()) {
            return new ArrayList<>(); // Aucune pizza à livrer
        }

        pizzas.sort(Comparator.comparingDouble(pizza -> position.distanceKM(pizza.getGPS())));

        Pizza nextPizza = pizzas.get(0);
        double distance = position.distanceKM(nextPizza.getGPS());
        double deliveryTime = distance / AVGspeed * 60; // Temps de livraison en minutes
        double remainingTime = nextPizza.getRestTime() - elapsedTime;

        System.out.println("restTime: " + formatDouble(remainingTime, 2));
        System.out.println("deliveryTime: " + formatDouble(deliveryTime, 2));

        ArrayList<GPS> result = new ArrayList<>();

        if (remainingTime > 0) {
            // La pizza peut être livrée à temps
            result.add(nextPizza.getGPS());

            // Créer une nouvelle liste pour les pizzas restantes
            List<Pizza> remainingPizzas = new ArrayList<>(pizzas.subList(1, pizzas.size()));

            // Récursion pour traiter les pizzas restantes
            ArrayList<GPS> remainingDelivery = bestDeliveryRecursive(remainingPizzas, nextPizza.getGPS(), elapsedTime + deliveryTime);
            result.addAll(remainingDelivery);
        } else {
            // La pizza ne peut pas être livrée à temps, la traiter après les autres
            result.addAll(bestDeliveryRecursive(pizzas.subList(1, pizzas.size()), position, elapsedTime));
            result.add(nextPizza.getGPS());
        }

        return result;
    }

    public static String formatDouble(double number, int decimalPlaces) {
        // Construire le motif en fonction du nombre de chiffres après la virgule
        StringBuilder patternBuilder = new StringBuilder("#.");
        for (int i = 0; i < decimalPlaces; i++) {
            patternBuilder.append("#");
        }
        String pattern = patternBuilder.toString();
        // Créer un objet DecimalFormat avec le motif spécifié
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        // Utiliser le format pour formater le nombre
        return decimalFormat.format(number);
    }
}
