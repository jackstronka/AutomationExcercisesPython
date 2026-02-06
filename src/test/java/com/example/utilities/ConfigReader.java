package com.example.utilities;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("Cannot find config.properties");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Read value with priority:
     * 1) System property (e.g. -Dbrowser=firefox)
     * 2) config.properties
     *
     * @throws IllegalArgumentException jeśli klucz nie istnieje lub wartość jest pusta
     */
    public static String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing or empty config key: " + key + ". Add to config.properties or use -D" + key + "=value"
            );
        }
        return value;
    }

    /**
     * Overload z domyślną wartością – gdy klucz nie istnieje lub wartość jest pusta, zwraca defaultValue.
     */
    public static String get(String key, String defaultValue) {
        String value = System.getProperty(key, properties.getProperty(key, defaultValue));
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}