package com.pizzadelivery.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GPS {
    private final double latitude;
    private final double longitude;
    private static final Path FILE_PATH = Paths.get("src/main/resources/data/memoization_time_cache.txt");
    private static final SaveableHashMap<String, Double> memoizationCacheTime = new SaveableHashMap<>(FILE_PATH.resolve("memoizationCacheTime.txt"));
    private static final SaveableHashMap<String, Double> memoizationCacheDistance = new SaveableHashMap<>(FILE_PATH.resolve("memoizationCacheDistance.txt"));
    private static final double SCOOTER_SPEED_KMH = 50;

    public GPS(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void callOpenRouteServiceAPI() {
        // Implement the method to call the OpenRouteService API
    }

    private void updateMemoizationCache(String key, Double time, Float distance) {
        memoizationCacheTime.put(key, time);
        memoizationCacheDistance.put(key, Double.valueOf(distance)); // Convert Float to Double
    }

    public double calculateTravelTime(GPS destination) {
        // Implement the method to calculate travel time between two GPS points
        return 0;
    }

    public double calculateDistance(GPS destination) {
        // Implement the method to calculate distance between two GPS points
        return 0;
    }

    private double calculateCrowFliesDistance(GPS destination) {
        // Implement the method to calculate crow flies distance
        return 0;
    }

    // Getters and setters

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static Path getFilePath() {
        return FILE_PATH;
    }

    public static SaveableHashMap<String, Double> getMemoizationCacheTime() {
        return memoizationCacheTime;
    }

    public static SaveableHashMap<String, Double> getMemoizationCacheDistance() {
        return memoizationCacheDistance;
    }

    public static double getScooterSpeedKmh() {
        return SCOOTER_SPEED_KMH;
    }
}
