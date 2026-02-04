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
     */
    public static String get(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    /**
     * Overload with default value for convenient usage.
     */
    public static String get(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue));
    }
}