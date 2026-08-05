package api.services;

import base.BaseApiClient;
import io.restassured.response.Response;
import managers.EndpointManager;

import java.util.Map;

/**
 * Booking API service.
 * Endpoints defined in EndpointManager, not here.
 */
public class BookingService extends BaseApiClient {

    // Booking CRUD operations

    public Response getAllBookings(String token) {
        return get(EndpointManager.getEndpoint("BOOKINGS"), token);
    }

    public Response getAllBookings() {
        return get(EndpointManager.getEndpoint("BOOKINGS"));
    }

    public Response getBookingById(int bookingId, String token) {
        return get(EndpointManager.getEndpoint("GET_BOOKING"), token);
    }

    public Response getBookingById(int bookingId) {
        return get(EndpointManager.getEndpoint("GET_BOOKING"), Map.of("id", bookingId));
    }

    public Response getBookingByRefCode(String refCode, String token) {
        return get(EndpointManager.getEndpoint("GET_BOOKING_BY_REF"), token);
    }

    public Response getBookingByRefCode(String refCode) {
        return get(EndpointManager.getEndpoint("GET_BOOKING_BY_REF"), Map.of("ref", refCode));
    }

    public Response createBooking(Map<String, Object> bookingData, String token) {
        return request(token).body(bookingData).post(EndpointManager.getEndpoint("CREATE_BOOKING"));
    }

    public Response createBooking(Map<String, Object> bookingData) {
        return request().body(bookingData).post(EndpointManager.getEndpoint("CREATE_BOOKING"));
    }

    public Response cancelBooking(int bookingId, String token) {
        return requestWithPathParams(Map.of("id", bookingId))
                .delete(EndpointManager.getEndpoint("CANCEL_BOOKING"));
    }

    public Response cancelBooking(int bookingId) {
        return requestWithPathParams(Map.of("id", bookingId))
                .delete(EndpointManager.getEndpoint("CANCEL_BOOKING"));
    }

    public Response deleteBooking(int bookingId, String token) {
        return requestWithPathParams(Map.of("id", bookingId))
                .delete(EndpointManager.getEndpoint("DELETE_BOOKING"));
    }

    public Response deleteBooking(int bookingId) {
        return requestWithPathParams(Map.of("id", bookingId))
                .delete(EndpointManager.getEndpoint("DELETE_BOOKING"));
    }

    // With query parameters

    public Response getBookings(Map<String, Object> queryParams, String token) {
        return get(EndpointManager.getEndpoint("BOOKINGS"), token, queryParams);
    }

    public Response getBookings(Map<String, Object> queryParams) {
        return get(EndpointManager.getEndpoint("BOOKINGS"), queryParams);
    }

    // Extract booking ID from response
    public Integer extractBookingId(Response response) {
        return getInt(response, "id");
    }

    // Extract booking reference code from response
    public String extractBookingRefCode(Response response) {
        return getString(response, "referenceCode");
    }
}
