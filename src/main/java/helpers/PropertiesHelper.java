package helpers;

import managers.ConfigManager;
import utils.LogUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

public class PropertiesHelper {
    private static Properties properties;
    private static volatile boolean initialized = false;

    // Use ConfigManager paths for consistency
    private static final String FRAMEWORK_CONFIG = "src/main/resources/framework/config.properties";
    private static final String PROJECT_CONFIG_DIR = "src/main/resources/projects/";
    private static final String OBJECTS_CONFIG = "src/main/java/objects/event_hub.properties";

    public static synchronized Properties loadAllFiles(){
        if (initialized && properties != null && !properties.isEmpty()) {
            return properties;
        }

        // Get environment from ConfigManager (uses system property or default)
        String environment = ConfigManager.getEnvironment();
        if (environment == null || environment.isEmpty()) {
            environment = "dev";
        }

        LinkedList<String> files = new LinkedList<>();
        files.add(FRAMEWORK_CONFIG);
        files.add(PROJECT_CONFIG_DIR + getProjectName() + "/config.properties");
        files.add(PROJECT_CONFIG_DIR + getProjectName() + "/env/" + environment + ".properties");
        files.add(OBJECTS_CONFIG);

        Properties merged = new Properties();
        for (String filePath : files) {
            Properties tempProp = new Properties();
            String fullPath = SystemHelper.getCurrentDir() + filePath;
            try (FileInputStream input = new FileInputStream(fullPath)) {
                tempProp.load(input);
                LogUtils.info("Loaded: " + filePath);
            } catch (IOException e) {
                // Only warn for non-critical files
                if (filePath.contains("event_hub.properties")) {
                    LogUtils.warn("File not found or error loading: " + filePath);
                }
            }
            merged.putAll(tempProp);
        }
        properties = merged;
        initialized = true;

        // ========== Debugging info ==========
        LogUtils.info("Total properties loaded: " + properties.size());
        LogUtils.info("Properties loaded successfully!");
        return merged;
    }

    private static String getProjectName() {
        String project = System.getProperty("project", "EventHub").toLowerCase();
        return project;
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
            String linkFile = SystemHelper.getCurrentDir() + FRAMEWORK_CONFIG;
            try (FileInputStream input = new FileInputStream(linkFile)) {
                properties.load(input);
            }
        } catch (Exception e){
            LogUtils.error("Failed to load default properties file: ("+FRAMEWORK_CONFIG+"): "+ e.getMessage());
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
        // Try ConfigManager first for standard keys
        String configValue = ConfigManager.getProperty(key);
        if (configValue != null) {
            return configValue.trim();
        }
        // Fallback to properties file
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

            String linkFile = SystemHelper.getCurrentDir() + FRAMEWORK_CONFIG;
            LogUtils.info(linkFile);
            properties.setProperty(key, keyValue);
            try (FileOutputStream out = new FileOutputStream(linkFile)) {
                properties.store(out, null);
            }
        }catch (Exception e){
            LogUtils.error("Failed to set value in properties file for key: ("+key+"): "+ e.getMessage());
        }
    }

    /**
     * Reset the properties cache. Call this when environment changes.
     */
    public static synchronized void reset() {
        properties = null;
        initialized = false;
    }
}
