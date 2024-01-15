package com.pizzadelivery.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * A HashMap implementation that automatically saves its content to a file upon any modification.
 * This class extends HashMap and adds functionality to persist the map's state to a file.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author Team
 */
public class SaveableHashMap<K, V> extends HashMap<K, V> {
    private final Lock fileLock;
    private final Path filePath;

    /**
     * Constructs a SaveableHashMap instance that stores data to the specified file path.
     *
     * @param filePath the file path where the map data will be saved
     */
    public SaveableHashMap(Path filePath) {
        super();
        this.filePath = filePath;
        this.fileLock = new ReentrantLock();
        this.putAll(loadFromFile(this.filePath)); // Load existing data from the file
    }

    /**
     * Overrides the put method to save changes to the file after inserting a new key-value pair.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with the key, or null if there was no mapping for the key
     */
    @Override
    public V put(K key, V value) {
        V result = super.put(key, value);
        saveToFile(); // Save changes to file
        return result;
    }

    /**
     * Overrides the putAll method to save changes to the file after inserting multiple key-value pairs.
     *
     * @param m mappings to be stored in this map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
        saveToFile(); // Save changes to file
    }

    /**
     * Overrides the remove method to save changes to the file after removing a key-value pair.
     *
     * @param key the key whose mapping is to be removed from the map
     * @return the previous value associated with the key, or null if there was no mapping for the key
     */
    @Override
    public V remove(Object key) {
        V result = super.remove(key);
        saveToFile(); // Save changes to file
        return result;
    }

    /**
     * Overrides the clear method to save changes to the file after clearing all key-value pairs from the map.
     */
    @Override
    public void clear() {
        super.clear();
        saveToFile(); // Save changes to file
    }

    /**
     * Loads the map data from the specified file path.
     * This method acquires a lock before reading to ensure thread safety.
     *
     * @param filePath the path of the file from which the map data is to be loaded
     * @return a map containing the loaded data
     */
    private Map<K, V> loadFromFile(Path filePath) {
        try {
            fileLock.lock(); // Acquire lock before reading from file
            Map<K, V> loadedData = new HashMap<>();
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    K key = (K) parts[0]; // Assuming the key is a String
                    V value = convertValue(parts[1]); // Convert the value to the appropriate type
                    loadedData.put(key, value);
                }
            }
            return loadedData;
        } catch (Exception e) {
            // Log or handle the exception as needed
            Logger logger = Logger.getLogger(SaveableHashMap.class.getName());
            logger.warning("Exception occurred while loading data from file");
            return new HashMap<>();
        } finally {
            fileLock.unlock(); // Release lock after reading from file
        }
    }

    /**
     * Converts a string value to the appropriate type for the map.
     * Override this method if a different conversion is required.
     *
     * @param value the string value to convert
     * @return the converted value
     */
    private V convertValue(String value) {
        try {
            // Simple conversion to Double, modify based on actual value type requirements
            return (V) Double.valueOf(value);
        } catch (NumberFormatException e) {
            Logger logger = Logger.getLogger(SaveableHashMap.class.getName());
            logger.warning("Exception occurred while converting value: " + e.getMessage());
            return null; // Return null or a default value as appropriate
        }
    }

    /**
     * Saves the current state of the map to the file.
     * This method acquires a lock before writing to ensure thread safety.
     */
    private void saveToFile() {
        try {
            fileLock.lock(); // Acquire lock before writing to file
            StringBuilder content = new StringBuilder();
            for (Map.Entry<K, V> entry : entrySet()) {
                content.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }

            Files.writeString(filePath, content.toString(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(SaveableHashMap.class.getName());
            logger.warning("Exception occurred while saving data to file: " + e.getMessage());
        } finally {
            fileLock.unlock(); // Release lock after writing to file
        }
    }
}