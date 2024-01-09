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

public class SaveableHashMap<K, V> extends HashMap<K, V> {
    private final Lock fileLock;
    private final Path filePath;

    public SaveableHashMap(Path filePath) {
        super();
        this.filePath = filePath;
        this.fileLock = new ReentrantLock();
        // Load from file only after filePath and fileLock are initialized
        this.putAll(loadFromFile(this.filePath));
    }

    @Override
    public V put(K key, V value) {
        V result = super.put(key, value);
        saveToFile();
        return result;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        super.putAll(m);
        saveToFile();
    }

    @Override
    public V remove(Object key) {
        V result = super.remove(key);
        saveToFile();
        return result;
    }

    @Override
    public void clear() {
        super.clear();
        saveToFile();
    }

    private Map<K, V> loadFromFile(Path filePath) {
        try {
            fileLock.lock(); // Acquérir le verrou avant de lire depuis le fichier
            Map<K, V> loadedData = new HashMap<>();
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    K key = (K) parts[0]; // Si les clés sont des chaînes de caractères
                    V value = convertValue(parts[1]); // Conversion appropriée
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
            fileLock.unlock(); // Libérer le verrou après la lecture depuis le fichier
        }
    }

    private V convertValue(String value) {
        try {
            return (V) Double.valueOf(value);
        } catch (NumberFormatException e) {
            // Log or handle the exception as needed
            Logger logger = Logger.getLogger(SaveableHashMap.class.getName());
            logger.warning("Exception occurred while converting value");
            return null; // Ou une valeur par défaut appropriée
        }
    }


    private void saveToFile() {
        try {
            fileLock.lock(); // Acquérir le verrou avant d'écrire dans le fichier
            StringBuilder content = new StringBuilder();
            for (Map.Entry<K, V> entry : entrySet()) {
                content.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }

            Files.writeString(filePath, content.toString(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (Exception e) {
            // Log or handle the exception as needed
            Logger logger = Logger.getLogger(SaveableHashMap.class.getName());
            logger.warning("Exception occurred while saving data to file");
        } finally {
            fileLock.unlock(); // Libérer le verrou après avoir écrit dans le fichier
        }
    }


}