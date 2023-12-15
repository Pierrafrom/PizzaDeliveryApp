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
import java.nio.file.Path;
import java.nio.file.Paths;

public class GPS {

    private static final Path FILE_PATH = Paths.get("src/main/resources/data/memoization_time_cache.txt");
    private static final SaveableHashMap<String, Double> memoizationCacheTime = new SaveableHashMap<>(FILE_PATH);
    private static final Object fileLock = new Object();

    private final double latitude;
    private final double longitude;

    public GPS(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

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

    private String callOpenRouteServiceApi(GPS source, GPS destination) throws Exception {
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
        if (conn.getResponseCode() == 429) {
            throw new RateLimitExceededException("API rate limit exceeded. Waiting before retrying...");
        }

        return response.toString();
    }

    public double timeTravel(GPS otherGPS) throws RateLimitExceededException {
        String key = this + "|" + otherGPS;

        synchronized (fileLock) {
            if (memoizationCacheTime.containsKey(key)) {
                return memoizationCacheTime.get(key);
            }
        }

        System.out.println("timeTravel");

        try {
            // Call the API and parse the response
            String response = callOpenRouteServiceApi(this, otherGPS);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray routes = jsonResponse.getJSONArray("routes");

            // Extract duration from the first route in the response
            if (!routes.isEmpty()) {
                JSONObject firstRoute = routes.getJSONObject(0);
                JSONObject summary = firstRoute.getJSONObject("summary");

                double duration = summary.getDouble("duration");

                synchronized (fileLock) {
                    memoizationCacheTime.put(key, duration);
                }

                return duration;
            }
        } catch (RateLimitExceededException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            // Log exceptions
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("Exception occurred", e);
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("GPS{latitude=%.6f, longitude=%.6f}", latitude, longitude);
    }
}
