package model;

public class GPS {
    private double x;
    private double y;

    public GPS(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public double getX() { return x; }
    public double getY() { return y;}

    public double distanceKM(GPS otherGPS) {
        // Rayon de la Terre en kilomètres
        double earthRadius = 6371;

        // Conversion des latitudes et longitudes de degrés à radians
        double lat1 = Math.toRadians(this.getY());
        double lon1 = Math.toRadians(this.getX());
        double lat2 = Math.toRadians(otherGPS.getY());
        double lon2 = Math.toRadians(otherGPS.getX());

        // Calcul des différences de latitudes et longitudes
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Formule de la distance haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance en kilomètres
        double distance = earthRadius * c;

        return distance;
    }
    public String toString() {
        return "[x=" + x + ", y=" + y + "]";
    }
}
