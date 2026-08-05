package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public final class JSONUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private JSONUtils() {
        // Utility class
    }

    public static String objectToJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON string", e);
        }
    }

    public static <T> T jsonStringToObject(String jsonString, Class<T> targetClass) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, targetClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to object: " + targetClass.getName(), e);
        }
    }

    public static <T> T jsonStringToObject(String jsonString, TypeReference<T> targetType) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, targetType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to object: " + targetType.getType(), e);
        }
    }

    public static <T> T responseBodyToObject(String responseBody, Class<T> targetClass) {
        return jsonStringToObject(responseBody, targetClass);
    }

    public static <T> T responseBodyToObject(String responseBody, TypeReference<T> targetType) {
        return jsonStringToObject(responseBody, targetType);
    }

    public static Map<String, Object> readJsonFileAsMap(String filePath) {
        return readJsonFile(filePath, new TypeReference<>() {
        });
    }

    public static <T> T readJsonFile(String filePath, Class<T> targetClass) {
        try {
            return OBJECT_MAPPER.readValue(new File(filePath), targetClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file as object: " + filePath, e);
        }
    }

    public static <T> T readJsonFile(String filePath, TypeReference<T> targetType) {
        try {
            return OBJECT_MAPPER.readValue(new File(filePath), targetType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file as object: " + filePath, e);
        }
    }

    public static String toPrettyJson(Object object) {
        Object jsonValue = normalizeJsonValue(object);
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(jsonValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to pretty JSON", e);
        }
    }

    public static void writePrettyJson(String filePath, Object object) {
        Object jsonValue = normalizeJsonValue(object);
        try {
            OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), jsonValue);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write pretty JSON file: " + filePath, e);
        }
    }

    public static String updateJsonField(String jsonString, String jsonPath, Object newValue) {
        try {
            DocumentContext documentContext = JsonPath.parse(jsonString);
            documentContext.set(jsonPath, newValue);
            return toPrettyJson(documentContext.jsonString());
        } catch (PathNotFoundException e) {
            throw new RuntimeException("JSON path not found: " + jsonPath, e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update JSON field at path: " + jsonPath, e);
        }
    }

    public static void updateJsonFileField(String filePath, String jsonPath, Object newValue) {
        String updatedJson = updateJsonField(readJsonFile(filePath, JsonNode.class).toString(), jsonPath, newValue);
        writePrettyJson(filePath, updatedJson);
    }

    public static boolean isJsonPathExists(String jsonString, String jsonPath) {
        try {
            JsonPath.parse(jsonString).read(jsonPath);
            return true;
        } catch (PathNotFoundException e) {
            return false;
        }
    }

    public static void validateJsonPathExists(String jsonString, String jsonPath) {
        if (!isJsonPathExists(jsonString, jsonPath)) {
            throw new AssertionError("JSON path does not exist: " + jsonPath);
        }
    }

    public static boolean compareJson(Object actual, Object expected) {
        try {
            JsonNode actualNode = toJsonNode(actual);
            JsonNode expectedNode = toJsonNode(expected);
            return actualNode.equals(expectedNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to compare JSON values", e);
        }
    }

    public static void assertJsonEquals(Object actual, Object expected) {
        if (!compareJson(actual, expected)) {
            throw new AssertionError("JSON actual is not equal to expected."
                    + System.lineSeparator() + "Actual:" + System.lineSeparator() + toPrettyJson(actual)
                    + System.lineSeparator() + "Expected:" + System.lineSeparator() + toPrettyJson(expected));
        }
    }

    private static Object normalizeJsonValue(Object object) {
        if (object instanceof String jsonString) {
            try {
                return OBJECT_MAPPER.readTree(jsonString);
            } catch (JsonProcessingException ignored) {
                return object;
            }
        }
        return object;
    }

    private static JsonNode toJsonNode(Object object) throws JsonProcessingException {
        if (object instanceof JsonNode jsonNode) {
            return jsonNode;
        }
        if (object instanceof String jsonString) {
            return OBJECT_MAPPER.readTree(jsonString);
        }
        return OBJECT_MAPPER.valueToTree(object);
    }
}