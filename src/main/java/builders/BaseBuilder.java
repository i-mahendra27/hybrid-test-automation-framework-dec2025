package builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * Base builder with common functionality for all test data builders.
 * Provides method chaining, partial builds, and conversion utilities.
 */
public abstract class BaseBuilder<T> {

    protected final Map<String, Object> fields = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    // Put a field value
    protected <V> BaseBuilder<T> put(String field, V value) {
        fields.put(field, value);
        return self();
    }

    // Get a field value
    @SuppressWarnings("unchecked")
    protected <V> V get(String field) {
        return (V) fields.get(field);
    }

    // Check if field exists
    protected boolean has(String field) {
        return fields.containsKey(field) && fields.get(field) != null;
    }

    // Build the object
    public abstract T build();

    // Get self for method chaining
    protected abstract BaseBuilder<T> self();

    // Convert to Map
    public Map<String, Object> toMap() {
        return new HashMap<>(fields);
    }

    // Convert to JSON string
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    // Convert to JSON string (pretty format)
    public String toPrettyJson() {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to pretty JSON", e);
        }
    }

    // Get all field names
    public String[] getFieldNames() {
        return fields.keySet().toArray(new String[0]);
    }

    // Get field count
    public int getFieldCount() {
        return fields.size();
    }

    // Clear all fields
    public BaseBuilder<T> clear() {
        fields.clear();
        return self();
    }

    // Remove a field
    public BaseBuilder<T> remove(String field) {
        fields.remove(field);
        return self();
    }

    @Override
    public String toString() {
        return toJson();
    }
}
