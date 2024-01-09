package com.pizzadelivery.model;

import com.pizzadelivery.config.ApiConfig;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Math.round;

public record GPS(double latitude, double longitude) implements Serializable {
    private static final Path FILE_PATH = Paths.get("src/main/resources/data/");
    private static final String TIME_CACHE_FILE_NAME = "memoizationCacheTime.txt";
    private static final String DISTANCE_CACHE_FILE_NAME = "memoizationCacheDistance.txt";
    private static final SaveableHashMap<String, Double> memoizationCacheTime =
            new SaveableHashMap<>(FILE_PATH.resolve(TIME_CACHE_FILE_NAME));
    private static final SaveableHashMap<String, Double> memoizationCacheDistance =
            new SaveableHashMap<>(FILE_PATH.resolve(DISTANCE_CACHE_FILE_NAME));
    private static final Object fileLock = new Object();
    private static final double SCOOTER_SPEED_KMH = 50;
    private static boolean isApiInCooldown = false;
    private static final int API_COOLDOWN_TIME_MS = 2 * 60 * 1000; // 2 minutes in milliseconds

    private String callOpenRouteServiceApi(GPS source, GPS destination) throws Exception {
        // Check if the API is in cooldown
        if (isApiInCooldown) {
            throw new IOException("API is in cooldown");
        }
        // Set up the URL and open a connection
        HttpURLConnection conn = getHttpURLConnection();

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
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.info("Sent request to OpenRouteService API : " + jsonPayload.toString());
        }

        // Now check the response code
        int responseCode = conn.getResponseCode();
        if (responseCode == 429) {
            startApiCooldown();
            throw new IOException("API rate limit exceeded. Waiting before retrying...");
        }

        if (responseCode != HttpURLConnection.HTTP_OK) {
            // throw an exception and the message from the API
            throw new IOException("HTTP error code: " + responseCode + ", message: " + conn.getResponseMessage());
        }

        // Read and return response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    @NotNull
    private static HttpURLConnection getHttpURLConnection() throws IOException {
        URL url = new URL("https://api.openrouteservice.org/v2/directions/driving-car");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set request method and headers
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", ApiConfig.OPENROUTE_API_KEY);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        return conn;
    }

    public double timeTravel(GPS otherGPS) {
        String key = this + "|" + otherGPS;

        synchronized (fileLock) {
            if (memoizationCacheTime.containsKey(key)) {
                System.out.println("Memoized");
                return memoizationCacheTime.get(key);
            }
        }

        try {
            // Call the API and parse the response
            String response = callOpenRouteServiceApi(this, otherGPS);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray routes = jsonResponse.getJSONArray("routes");

            // Extract duration from the first route in the response
            if (!routes.isEmpty()) {
                JSONObject firstRoute = routes.getJSONObject(0);
                JSONObject summary = firstRoute.getJSONObject("summary");

                double duration = round(summary.getDouble("duration") / 60);  //divided by 60 because the APIs value is given in seconds

                synchronized (fileLock) {
                    memoizationCacheTime.put(key, duration);
                }

                return duration;
            }
        } catch (IOException ioException) {
            // Log network or I/O related exceptions if the API is not in cooldown
            if (!isApiInCooldown) {
                Logger logger = LoggerFactory.getLogger(GPS.class);
                logger.error("Network or I/O Exception occurred", ioException);
            }
            return calculateCrowTravelTime(otherGPS);
        } catch (JSONException jsonException) {
            // Log JSON parsing exceptions
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("JSON Parsing Exception occurred", jsonException);
            return calculateCrowTravelTime(otherGPS);
        } catch (Exception generalException) {
            // Log any other exceptions
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("General Exception occurred", generalException);
            return calculateCrowTravelTime(otherGPS);
        }
        return -1;


    }

    public double calculateDistance(GPS destination) {
        // Check if the distance is already memoized
        String memoizationKey = this + "|" + destination;
        synchronized (fileLock) {
            if (memoizationCacheDistance.containsKey(memoizationKey)) {
                return memoizationCacheDistance.get(memoizationKey);
            }
        }

        // If not memoized, calculate using the API
        double distance = -1;

        try {
            String response = callOpenRouteServiceApi(this, destination);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray routes = jsonResponse.getJSONArray("routes");

            if (!routes.isEmpty()) {
                JSONObject firstRoute = routes.getJSONObject(0);
                JSONObject summary = firstRoute.getJSONObject("summary");

                // Extract distance from the first route in the response
                distance = round(summary.getDouble("distance") / 1000); //divided by 1000 because the APIs value is given in meters

                // Memoize the calculated distance
                synchronized (fileLock) {
                    memoizationCacheDistance.put(memoizationKey, distance);
                }
            }
        } catch (IOException ioException) {
            // Log network or I/O related exceptions if the API is not in cooldown
            if (!isApiInCooldown) {
                Logger logger = LoggerFactory.getLogger(GPS.class);
                logger.error("Network or I/O Exception occurred", ioException);
            }
            return calculateCrowFliesDistance(destination);
        } catch (JSONException jsonException) {
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("JSON Parsing Exception occurred while calculating distance", jsonException);
            return calculateCrowFliesDistance(destination);
        } catch (Exception generalException) {
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("General Exception occurred while calculating distance", generalException);
            return calculateCrowFliesDistance(destination);
        }

        return distance;
    }


    private double calculateCrowFliesDistance(GPS destination) {
        // Earth's radius in kilometers
        double earthRadius = 6371;

        // Convert latitudes and longitudes from degrees to radians
        double lat1 = Math.toRadians(latitude());
        double lon1 = Math.toRadians(longitude());
        double lat2 = Math.toRadians(destination.latitude);
        double lon2 = Math.toRadians(destination.longitude);

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

    public double calculateCrowTravelTime(GPS destination) {
        double travelTime = -1;
        try {
            double distance = calculateCrowFliesDistance(destination);
            // Calculate travel time based on scooter speed
            travelTime = distance / SCOOTER_SPEED_KMH;
        } catch (Exception e) {
            // Log exceptions
            Logger logger = LoggerFactory.getLogger(GPS.class);
            logger.error("Exception occurred while calculating travel time", e);
        }

        return travelTime;
    }

    private static void startApiCooldown() {
        isApiInCooldown = true;

        new Thread(() -> {
            try {
                Thread.sleep(API_COOLDOWN_TIME_MS);
            } catch (InterruptedException e) {
                Logger logger = LoggerFactory.getLogger(GPS.class);
                logger.error("Exception occurred while waiting for API cooldown", e);
                isApiInCooldown = false;
                Thread.currentThread().interrupt();
            }
            isApiInCooldown = false;
        }).start();
    }

    // ToString method
    @Override
    public String toString() {
        return String.format("GPS{latitude=%.6f, longitude=%.6f}", latitude, longitude);
    }
}
