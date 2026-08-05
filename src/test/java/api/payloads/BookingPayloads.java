package api.payloads;

import helpers.DataFakerHelper;

import java.util.LinkedHashMap;
import java.util.Map;

public class BookingPayloads {

    private BookingPayloads() {
        // Utility class
    }

    public static Map<String, Object> getListBookingsQueryParams(String eventId, String status, String page, String limit) {
        Map<String, Object> params = new LinkedHashMap<>();
        putIfPresent(params, "eventId", eventId);
        putIfPresent(params, "status", status);
        putIfPresent(params, "page", page);
        putIfPresent(params, "limit", limit);
        return params;
    }

    public static Map<String, Object> createBookingPayload() {
        return createBookingPayload(1, 1);
    }

    public static Map<String, Object> createBookingPayloadForEvent(int eventId) {
        return createBookingPayload(eventId, 1);
    }

    public static Map<String, Object> invalidCreateBookingPayload() {
        Map<String, Object> payload = createBookingPayload();
        payload.put("quantity", 11);
        return payload;
    }

    public static Map<String, Object> insufficientSeatsBookingPayload() {
        // Note: Use overload with eventId parameter for insufficient seats test
        // This method kept for backward compatibility
        return createBookingPayload(3, 3);
    }

    public static Map<String, Object> insufficientSeatsBookingPayload(int eventId) {
        // Create booking payload that exceeds available seats (event has 2 seats, request 3)
        return createBookingPayload(eventId, 3);
    }

    public static Map<String, Object> nonExistentEventBookingPayload() {
        return createBookingPayload(999999, 2);
    }

    private static Map<String, Object> createBookingPayload(int eventId, int quantity) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("eventId", eventId);
        payload.put("customerName", DataFakerHelper.getFaker().name().firstName());
        payload.put("customerEmail", DataFakerHelper.getFaker().internet().emailAddress());
        payload.put("customerPhone", DataFakerHelper.getFaker().phoneNumber().cellPhone());
        payload.put("quantity", quantity);
        return payload;
    }

    private static void putIfPresent(Map<String, Object> params, String key, String value) {
        if (value != null && !value.isBlank() && !"-".equals(value.trim())) {
            params.put(key, value.trim());
        }
    }
}
