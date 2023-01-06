package edu.ubb.tableeditor.utils;

import edu.ubb.tableeditor.annotation.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class PropertiesContext {

    private final static String PROPS_FILE = "application.properties";
    private static Properties props;

    static {
        try (InputStream is = PropertiesContext.class.getResourceAsStream(PROPS_FILE)) {
            props = new Properties();
            props.load(is);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.err.println(ioe.getMessage());
        }
    }

    private PropertiesContext() {
    }

    publi

}
