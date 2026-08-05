package helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.response.Response;
import managers.ConfigManager;
import reports.AllureManager;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ApiReportHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private ApiReportHelper() {
        // Utility class
    }

    public static void attachEndpointEvidence(String endpointName, String endpoint) {
        attachEvidence("API Endpoint - " + endpointName,
                "API ENDPOINT"
                        + System.lineSeparator() + "Endpoint Name: " + endpointName
                        + System.lineSeparator() + "Endpoint: " + endpoint
                        + System.lineSeparator() + "Base URL: " + ConfigManager.getProperty("API_BASE_URL")
                        + System.lineSeparator() + "Full URL: " + buildFullUrl(endpoint));
    }

    public static void attachRequestHeadersEvidence() {
        attachEvidence("API Request Headers",
                "API REQUEST HEADERS"
                        + System.lineSeparator() + "Content-Type: application/json"
                        + System.lineSeparator() + "Accept: application/json"
                        + System.lineSeparator() + "Sensitive Headers: default RestAssured blacklist enabled");
    }

    public static void attachBaseConfigurationEvidence() {
        attachEvidence("API Base Configuration",
                "API BASE CONFIGURATION"
                        + System.lineSeparator() + "Base URL: " + ConfigManager.getProperty("API_BASE_URL")
                        + System.lineSeparator() + "Timeout: " + ConfigManager.getProperty("API_TIMEOUT", "20") + " seconds");
    }

    public static void attachPayloadEvidence(String title, Map<String, Object> payload) {
        attachEvidence(title, "API PAYLOAD" + System.lineSeparator() + formatPayload(payload));
    }

    public static void attachQueryParamsEvidence(String title, Map<String, Object> queryParams) {
        attachEvidence(title,
                "API QUERY PARAMS" + System.lineSeparator() + formatPayload(queryParams));
    }

    public static void attachRequestEvidence(String method, String endpointName, String endpoint, String token, Map<String, Object> payload) {
        attachEvidence("API Request - " + method + " " + endpointName,
                "API REQUEST"
                        + System.lineSeparator() + "Method: " + method
                        + System.lineSeparator() + "Endpoint Name: " + endpointName
                        + System.lineSeparator() + "Endpoint: " + endpoint
                        + System.lineSeparator() + "Full URL: " + buildFullUrl(endpoint)
                        + System.lineSeparator() + "Headers:"
                        + System.lineSeparator() + "  Content-Type: application/json"
                        + System.lineSeparator() + "  Accept: application/json"
                        + System.lineSeparator() + "  Authorization: " + formatTokenAvailability(token)
                        + System.lineSeparator() + "Payload / Query Params:"
                        + System.lineSeparator() + formatPayload(payload));
    }

    public static void attachResponseEvidence(String method, String endpointName, Response response) {
        attachEvidence("API Response - " + method + " " + endpointName,
                "API RESPONSE"
                        + System.lineSeparator() + "Request: " + method + " " + endpointName
                        + System.lineSeparator() + "Status Code: " + response.statusCode()
                        + System.lineSeparator() + "Status Line: " + response.statusLine()
                        + System.lineSeparator() + "Headers: " + response.getHeaders()
                        + System.lineSeparator() + "Body:"
                        + System.lineSeparator() + maskSensitiveValues(response.asPrettyString()));
    }

    public static void attachRequestSchemaEvidence(String schemaName, Map<String, Object> payload) {
        attachEvidence("Request Schema Evidence - " + schemaName,
                "REQUEST SCHEMA VALIDATION"
                        + System.lineSeparator() + "Schema: " + schemaName
                        + System.lineSeparator() + "Payload:"
                        + System.lineSeparator() + formatPayload(payload));
    }

    public static void attachResponseSchemaEvidence(String apiName, String schemaType, Response response) {
        attachEvidence("Response Schema Evidence - " + apiName + " " + schemaType,
                "RESPONSE SCHEMA VALIDATION"
                        + System.lineSeparator() + "API: " + apiName
                        + System.lineSeparator() + "Schema Type: " + schemaType
                        + System.lineSeparator() + "Status Code: " + response.statusCode()
                        + System.lineSeparator() + "Body:"
                        + System.lineSeparator() + maskSensitiveValues(response.asPrettyString()));
    }

    public static void attachStatusEvidence(int expectedStatusCode, Response response) {
        attachEvidence("API Status Evidence",
                "API STATUS ASSERTION"
                        + System.lineSeparator() + "Expected Status Code: " + expectedStatusCode
                        + System.lineSeparator() + "Actual Status Code: " + response.statusCode()
                        + System.lineSeparator() + "Status Line: " + response.statusLine());
    }

    public static void attachErrorEvidence(String apiName, String expectedError, Response response) {
        attachEvidence("API Error Evidence - " + apiName,
                "API ERROR ASSERTION"
                        + System.lineSeparator() + "API: " + apiName
                        + System.lineSeparator() + "Expected Error: " + expectedError
                        + System.lineSeparator() + "Actual Error: " + response.jsonPath().getString("error")
                        + System.lineSeparator() + "Body:"
                        + System.lineSeparator() + maskSensitiveValues(response.asPrettyString()));
    }

    public static void attachDetailsEvidence(String apiName, List<Map<String, String>> expectedDetails, List<Map<String, Object>> actualDetails) {
        attachEvidence("API Details Evidence - " + apiName,
                "API DETAILS ASSERTION"
                        + System.lineSeparator() + "API: " + apiName
                        + System.lineSeparator() + "Expected Details:"
                        + System.lineSeparator() + formatObject(expectedDetails)
                        + System.lineSeparator() + "Actual Details:"
                        + System.lineSeparator() + formatObject(actualDetails));
    }

    public static void attachEmptyDetailsEvidence(String apiName, List<Map<String, Object>> actualDetails) {
        attachEvidence("API Empty Details Evidence - " + apiName,
                "API EMPTY DETAILS ASSERTION"
                        + System.lineSeparator() + "API: " + apiName
                        + System.lineSeparator() + "Actual Details:"
                        + System.lineSeparator() + formatObject(actualDetails));
    }

    private static void attachEvidence(String title, String content) {
        LogUtils.info(content);
        AllureManager.attachText(title, content);
    }

    private static String buildFullUrl(String endpoint) {
        String baseUrl = ConfigManager.getProperty("API_BASE_URL", "");
        if (baseUrl.endsWith("/") && endpoint.startsWith("/")) {
            return baseUrl.substring(0, baseUrl.length() - 1) + endpoint;
        }
        if (!baseUrl.endsWith("/") && !endpoint.startsWith("/")) {
            return baseUrl + "/" + endpoint;
        }
        return baseUrl + endpoint;
    }

    private static String formatTokenAvailability(String token) {
        return token != null && !token.isBlank() ? "Bearer ******" : "<not set>";
    }

    private static String formatPayload(Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return "<empty>";
        }
        return formatObject(maskObject(payload));
    }

    private static String formatObject(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return String.valueOf(value);
        }
    }

    @SuppressWarnings("unchecked")
    private static Object maskObject(Object value) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> masked = new LinkedHashMap<>();
            map.forEach((key, nestedValue) -> {
                String keyName = String.valueOf(key);
                if (isSensitiveKey(keyName)) {
                    masked.put(keyName, "******");
                } else {
                    masked.put(keyName, maskObject(nestedValue));
                }
            });
            return masked;
        }
        if (value instanceof List<?> list) {
            List<Object> masked = new ArrayList<>();
            list.forEach(item -> masked.add(maskObject(item)));
            return masked;
        }
        return value;
    }

    private static boolean isSensitiveKey(String key) {
        return "password".equalsIgnoreCase(key) || "token".equalsIgnoreCase(key) || "authorization".equalsIgnoreCase(key);
    }

    private static String maskSensitiveValues(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("(?i)(\"(?:password|token|authorization)\"\\s*:\\s*\")([^\"]*)(\")", "$1****$3");
    }
}
