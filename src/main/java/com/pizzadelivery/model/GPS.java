package com.pizzadelivery.model;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * A record that represents a geographic coordinate with a latitude and longitude.
 * This immutable data type is ideal for representing a point on the Earth's surface.
 *
 * @param latitude  The latitude of the GPS coordinate, in degrees.
 * @param longitude The longitude of the GPS coordinate, in degrees.
 */
public record GPS(double latitude, double longitude) {

    /**
     * Calculates the distance in kilometers to another GPS coordinate using
     * the Haversine formula.
     *
     * @param otherGPS The other GPS coordinate to which the distance is calculated.
     * @return The distance in kilometers.
     */
    public double flightDistanceKM(GPS otherGPS) {
        // Earth's radius in kilometers
        double earthRadius = 6371;

        // Convert latitudes and longitudes from degrees to radians
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(otherGPS.latitude);
        double lon2 = Math.toRadians(otherGPS.longitude);

        // Latitude and longitude differences
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine formula for distance
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        return earthRadius * c;
    }

    public void timeToDeliver(GPS otherGPS) {
        Client client = ClientBuilder.newClient();
        Entity<String> payload = Entity.json("{\"coordinates\":[[8.681495,49.41461],[8.686507,49.41943],[8.687872,49.420318]]}");
        Response response = client.target("https://api.openrouteservice.org/v2/directions/driving-car")
                .request()
                .header("Authorization", "5b3ce3597851110001cf62488087bfe5c98c461b8619f4d783400758")
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .post(payload);

        System.out.println("status: " + response.getStatus());
        System.out.println("headers: " + response.getHeaders());
        System.out.println("body:" + response.readEntity(String.class));
    }

    @Override
    public String toString() {
        return String.format("GPS{latitude=%.6f, longitude=%.6f}", latitude, longitude);
    }

    public static void main(String[] args) {
        GPS gps1 = new GPS(49.41461, 8.681495);
        GPS gps2 = new GPS(49.41943, 8.686507);
        gps2.timeToDeliver(gps1);
    }
}
