package managers;

import java.util.Locale;

public class EndpointManager {
    private EndpointManager() {
        // Utility class
    }

    public static String getEndpoint(String endpointName) {
        String normalizedEndpointName = normalizeEndpointName(endpointName);
        return switch (normalizedEndpointName) {
            case "LOGIN", "AUTH_LOGIN" -> "/auth/login";
            case "REGISTER", "AUTH_REGISTER" -> "/auth/register";
            case "AUTH", "AUTH_ME", "VALIDATE", "ME" -> "/auth/me";
            case "EVENTS", "LIST_EVENTS" -> "/events";
            case "CREATE_EVENT", "CREATE_NEW_EVENT" -> "/events";
            case "GET_EVENT", "GET_EVENT_BY_ID" -> "/events/{id}";
            case "UPDATE_EVENT", "UPDATE_EVENT_BY_ID" -> "/events/{id}";
            case "DELETE_EVENT", "DELETE_EVENT_BY_ID" -> "/events/{id}";
            case "BOOKINGS", "LIST_BOOKINGS" -> "/bookings";
            case "CREATE_BOOKING", "CREATE_NEW_BOOKING" -> "/bookings";
            case "GET_BOOKING", "GET_BOOKING_BY_ID" -> "/bookings/{id}";
            case "GET_BOOKING_BY_REF", "GET_BOOKING_BY_REF_CODE", "GET_BOOKING_BY_REFERENCE_CODE" -> "/bookings/ref/{ref}";
            case "DELETE_BOOKING", "DELETE_BOOKING_BY_ID", "CANCEL_BOOKING" -> "/bookings/{id}";
            default -> throw new IllegalArgumentException("Unknown endpoint: " + endpointName);
        };
    }

    private static String normalizeEndpointName(String endpointName) {
        if (endpointName == null || endpointName.isBlank()) {
            throw new IllegalArgumentException("Endpoint name must not be blank");
        }
        return endpointName
                .trim()
                .replace("-", "_")
                .replace(" ", "_")
                .toUpperCase(Locale.ROOT);
    }
}
