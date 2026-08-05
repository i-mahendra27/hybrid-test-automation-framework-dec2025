package context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Generic reusable scenario context with thread-safe storage.
 * Can be used across any project - stores arbitrary key-value data.
 */
public class ScenarioContext {

    private static final ThreadLocal<Map<String, Object>> contextStore = ThreadLocal.withInitial(HashMap::new);

    // Store a value with key
    public <T> void set(String key, T value) {
        contextStore.get().put(key, value);
    }

    // Retrieve value by key
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) contextStore.get().get(key);
    }

    // Retrieve value with default if not found
    @SuppressWarnings("unchecked")
    public <T> T getOrDefault(String key, T defaultValue) {
        return (T) contextStore.get().getOrDefault(key, defaultValue);
    }

    // Check if key exists
    public boolean contains(String key) {
        return contextStore.get().containsKey(key);
    }

    // Remove a key
    public void remove(String key) {
        contextStore.get().remove(key);
    }

    // Clear all context data
    public void clear() {
        contextStore.remove();
    }

    // Get all keys
    public Map<String, Object> getAll() {
        return new HashMap<>(contextStore.get());
    }

    // Store string value (convenience method)
    public void setString(String key, String value) {
        set(key, value);
    }

    // Get string value (convenience method)
    public String getString(String key) {
        return get(key);
    }

    // Store integer value (convenience method)
    public void setInteger(String key, Integer value) {
        set(key, value);
    }

    // Get integer value (convenience method)
    public Integer getInteger(String key) {
        return get(key);
    }

    // Store boolean value (convenience method)
    public void setBoolean(String key, Boolean value) {
        set(key, value);
    }

    // Get boolean value (convenience method)
    public Boolean getBoolean(String key) {
        return get(key);
    }

    // Generate context key for type-safe access
    public static <T> String key(Class<T> type, String field) {
        return type.getSimpleName() + "." + field;
    }
}
