package utils;

import factory.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Collections;
import java.util.List;

public class LocalStorageUtils {
    private LocalStorageUtils() {
        super();
    }

    private static JavascriptExecutor getJsExecutor() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            LogUtils.error("WebDriver is null in LocalStorageUtils");
            throw new IllegalStateException("WebDriver not initialized");
        }
        if (!(driver instanceof JavascriptExecutor js)) {
            LogUtils.error("WebDriver does not support JavaScript execution");
            throw new UnsupportedOperationException("JavaScript not supported");
        }
        return js;
    }

    /**
     * Get item from localStorage
     */
    public static String getItem(String key) {
        if (key == null || key.isBlank()) {
            LogUtils.warn("LocalStorage getItem: key is null or empty");
            return null;
        }
        try {
            String value = (String) getJsExecutor().executeScript(
                    "return window.localStorage.getItem(arguments[0]);", key);
            LogUtils.info("LocalStorage getItem: " + key);
            return value;
        } catch (Exception e) {
            LogUtils.error("Failed to get LocalStorage item: " + key);
            return null;
        }
    }

    /**
     * Set item in localStorage
     */
    public static void setItem(String key, String value) {
        if (key == null || key.isBlank()) {
            LogUtils.warn("LocalStorage setItem: key is null or empty");
            return;
        }
        if (value == null) {
            LogUtils.warn("LocalStorage setItem: value is null for key: " + key);
            return;
        }
        try {
            getJsExecutor().executeScript(
                    "window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
            LogUtils.info("LocalStorage setItem: " + key);
        } catch (Exception e) {
            LogUtils.error("Failed to set LocalStorage item: " + key);
        }
    }

    /**
     * Remove item from localStorage
     */
    public static void removeItem(String key) {
        if (key == null || key.isBlank()) {
            LogUtils.warn("LocalStorage removeItem: key is null or empty");
            return;
        }
        try {
            getJsExecutor().executeScript(
                    "window.localStorage.removeItem(arguments[0]);", key);
            LogUtils.info("LocalStorage removeItem: " + key);
        } catch (Exception e) {
            LogUtils.error("Failed to remove LocalStorage item: " + key);
        }
    }

    /**
     * Clear all items from localStorage
     */
    public static void clear() {
        try {
            getJsExecutor().executeScript("window.localStorage.clear();");
            LogUtils.info("LocalStorage cleared");
        } catch (Exception e) {
            LogUtils.error("Failed to clear LocalStorage");
        }
    }

    /**
     * Get number of items in localStorage
     */
    public static long size() {
        try {
            Long size = (Long) getJsExecutor().executeScript(
                    "return window.localStorage.length;");
            LogUtils.info("LocalStorage size: " + size);
            return size != null ? size : 0;
        } catch (Exception e) {
            LogUtils.error("Failed to get LocalStorage size");
            return 0;
        }
    }

    /**
     * Check if key exists in localStorage
     */
    public static boolean hasItem(String key) {
        if (key == null || key.isBlank()) {
            return false;
        }
        try {
            String value = (String) getJsExecutor().executeScript(
                    "return window.localStorage.getItem(arguments[0]);", key);
            return value != null;
        } catch (Exception e) {
            LogUtils.error("Failed to check LocalStorage item: " + key);
            return false;
        }
    }

    /**
     * Get all keys from localStorage
     */
    @SuppressWarnings("unchecked")
    public static List<String> getAllKeys() {
        try {
            List<String> keys = (List<String>) getJsExecutor().executeScript(
                    "var items = []; for (var i = 0; i < window.localStorage.length; i++) { items.push(window.localStorage.key(i)); } return items;");
            LogUtils.info("LocalStorage keys retrieved: " + (keys != null ? keys.size() : 0));
            return keys != null ? keys : Collections.emptyList();
        } catch (Exception e) {
            LogUtils.error("Failed to get all LocalStorage keys");
            return Collections.emptyList();
        }
    }

    /**
     * Get value by key, return defaultValue if not found
     */
    public static String getItemOrDefault(String key, String defaultValue) {
        String value = getItem(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get item as integer, return defaultValue if not found or invalid
     */
    public static int getItemAsInt(String key, int defaultValue) {
        String value = getItem(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LogUtils.warn("LocalStorage item '" + key + "' is not a valid integer");
            return defaultValue;
        }
    }

    /**
     * Get item as boolean, return defaultValue if not found or invalid
     */
    public static boolean getItemAsBoolean(String key, boolean defaultValue) {
        String value = getItem(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
