package helpers;

import utils.LogUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

public class PropertiesHelper {
    private static Properties properties;
    private static final String relPropertiesFilePathDefault = "src/main/resources/config.properties";

    public static synchronized Properties loadAllFiles(){
        if (properties != null && !properties.isEmpty()) {
            return properties;
        }

        String environment = System.getProperty("env");
        if (environment == null || environment.isEmpty()) {
            environment = "dev"; // Default if not specified
            LogUtils.info("No environment specified. Using default: dev");
        }

        LinkedList<String> files = new LinkedList<>();
        files.add("src/main/resources/config.properties");
        files.add("src/main/resources/env/" + environment + ".properties");
        files.add("src/main/java/objects/event_hub.properties");

        Properties merged = new Properties();
        for (String filePath : files) {
            Properties tempProp = new Properties();
            String linkFile = SystemHelper.getCurrentDir() + filePath;
            try (FileInputStream input = new FileInputStream(linkFile)) {
                tempProp.load(input);
                LogUtils.info("Loaded: " + filePath);
            } catch (IOException e) {
                LogUtils.warn("File not found or error loading: " + filePath);
            }
            merged.putAll(tempProp);
        }
        properties = merged;

        // ========== Debugging info ==========
        LogUtils.info("Total properties loaded: " + properties.size());
        if (properties.getProperty("ENV") != null) {
            LogUtils.info("Environment found: " + properties.getProperty("ENV"));
        } else {
            LogUtils.error("ENV property not found! Setting default to 'dev'");
            properties.setProperty("ENV", "dev");
        }

        LogUtils.info("Properties loaded successfully!");
        return merged;
    }

    public static synchronized void setFile(String relPropertiesFilePath){
        properties = new Properties();
        try {
            String linkFile = SystemHelper.getCurrentDir() + relPropertiesFilePath;
            try (FileInputStream input = new FileInputStream(linkFile)) {
                properties.load(input);
                LogUtils.info("Loaded properties from: " + relPropertiesFilePath);
            }
        } catch (Exception e){
            LogUtils.error("Failed to load properties file: ("+relPropertiesFilePath+"): "+ e.getMessage());
        }
    }

    public static synchronized void setDefaultFile(){
        properties = new Properties();
        try {
            String linkFile = SystemHelper.getCurrentDir() + relPropertiesFilePathDefault;
            try (FileInputStream input = new FileInputStream(linkFile)) {
                properties.load(input);
            }
        } catch (Exception e){
            LogUtils.error("Failed to load default properties file: ("+relPropertiesFilePathDefault+"): "+ e.getMessage());
        }
    }

    public static synchronized String getValue(String key){
        if (properties == null || properties.isEmpty()) {
            loadAllFiles();
        }
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue.trim();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            LogUtils.warn("Property key '" + key + "' is NULL or not found!");
        }
        return value != null ? value.trim() : null;
    }

    public static synchronized void setValue(String key, String keyValue){
        try {
            if (properties == null || properties.isEmpty()) {
                setDefaultFile();
            }

            String linkFile = SystemHelper.getCurrentDir() + relPropertiesFilePathDefault;
            LogUtils.info(linkFile);
            properties.setProperty(key, keyValue);
            try (FileOutputStream out = new FileOutputStream(linkFile)) {
                properties.store(out, null);
            }
        }catch (Exception e){
            LogUtils.error("Failed to set value in properties file for key: ("+key+"): "+ e.getMessage());
        }
    }
}
