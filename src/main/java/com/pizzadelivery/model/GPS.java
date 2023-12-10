package com.pizzadelivery.model;

import com.pizzadelivery.config.ApiConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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

    /**
     * Calls the OpenRouteService API to get the driving directions and estimated duration
     * between two GPS locations.
     *
     * @param source      The starting GPS location.
     * @param destination The destination GPS location.
     * @return A string containing the API response in JSON format.
     * @throws Exception If an error occurs during the API call.
     */
    private static String callOpenRouteServiceApi(GPS source, GPS destination) throws Exception {
        // Set up the URL and open a connection
        URL url = new URL("https://api.openrouteservice.org/v2/directions/driving-car");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set request method and headers
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", ApiConfig.OPENROUTE_API_KEY);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Create JSON payload with coordinates
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("coordinates", new double[][]{
                {source.longitude, source.latitude},
                {destination.longitude, destination.latitude}
        });

        // Send request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Read and return response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    /**
     * Calculates the estimated time to travel between two GPS locations by car.
     * The time is estimated using the OpenRouteService API.
     *
     * @param otherGPS The other GPS location to which the travel time is calculated.
     * @return The estimated duration in seconds.
     */
    public double timeTravel(GPS otherGPS) {
        try {
            // Call the API and parse the response
            String response = callOpenRouteServiceApi(this, otherGPS);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray routes = jsonResponse.getJSONArray("routes");

            // Extract duration from the first route in the response
            if (!routes.isEmpty()) {
                JSONObject firstRoute = routes.getJSONObject(0);
                JSONObject summary = firstRoute.getJSONObject("summary");

                return summary.getDouble("duration");
            }
        } catch (Exception e) {
            // Log exceptions
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("Exception occurred", e);
        }
        return 0;
    }

    /**
     * Returns a string representation of the GPS coordinate.
     *
     * @return A string representation of the GPS coordinate.
     */
    @Override
    public String toString() {
        return String.format("GPS{latitude=%.6f, longitude=%.6f}", latitude, longitude);
    }

    /**
     * A main method that demonstrates the use of the GPS class.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        GPS paris = new GPS(48.8566, 2.3522);
        GPS versailles = new GPS(48.8049, 2.1204);
        System.out.println(paris.timeTravel(versailles));
    }
}