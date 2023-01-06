package edu.ubb.tableeditor.utils;

import edu.ubb.tableeditor.annotation.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

@Singleton
public class PropertiesContext {

    private static final String PROPS_FILE = "application.properties";
    private static Properties props;

    static {
        try (InputStream is = PropertiesContext.class.getClassLoader().getResourceAsStream(PROPS_FILE)) {
            props = new Properties();
            props.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.err.println(ioe.getMessage());
        }
    }

    private PropertiesContext() {
    }

    public static int getIntProperty(String key) throws IllegalArgumentException {
        Objects.requireNonNull(key);

        final String value = props.getProperty(key);

        Objects.requireNonNull(value);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Property value cannot be casted to an integer", e);
        }
    }

    public static double getDoubleProperty(String key) throws IllegalArgumentException {
        Objects.requireNonNull(key);

        final String value = props.getProperty(key);

        Objects.requireNonNull(value);

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Property value cannot be casted to a double", e);
        }
    }

    public static String getStringProperty(String key) throws IllegalArgumentException {
        Objects.requireNonNull(key);

        final String value = props.getProperty(key);

        Objects.requireNonNull(value);

        return value;
    }

}
