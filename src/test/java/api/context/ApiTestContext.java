package api.context;

import io.restassured.response.Response;

import java.util.Map;

/**
 * Shared test context for API tests.
 * Singleton pattern ensures all steps share the same data.
 */
public class ApiTestContext {
    private static final ThreadLocal<Map<String, Object>> requestPayload = new ThreadLocal<>();
    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<String> token = new ThreadLocal<>();
    private static final ThreadLocal<Integer> userId = new ThreadLocal<>();
    private static final ThreadLocal<Integer> eventId = new ThreadLocal<>();
    private static final ThreadLocal<Integer> bookingId = new ThreadLocal<>();
    private static final ThreadLocal<String> bookingReferenceCode = new ThreadLocal<>();
    private static final ThreadLocal<Integer> bookingIdFromStore = new ThreadLocal<>();

    private static volatile ApiTestContext instance;

    private ApiTestContext() {
        // Private constructor for singleton
    }

    public static ApiTestContext getInstance() {
        if (instance == null) {
            synchronized (ApiTestContext.class) {
                if (instance == null) {
                    instance = new ApiTestContext();
                }
            }
        }
        return instance;
    }

    public Map<String, Object> getRequestPayload() {
        return requestPayload.get();
    }

    public void setRequestPayload(Map<String, Object> payload) {
        requestPayload.set(payload);
    }

    public Response getResponse() {
        return response.get();
    }

    public void setResponse(Response apiResponse) {
        response.set(apiResponse);
    }

    public String getToken() {
        return token.get();
    }

    public void setToken(String accessToken) {
        token.set(accessToken);
    }

    public Integer getUserId() {
        return userId.get();
    }

    public void setUserId(Integer id) {
        userId.set(id);
    }

    public Integer getEventId() {
        return eventId.get();
    }

    public void setEventId(Integer id) {
        eventId.set(id);
    }

    public Integer getBookingId() {
        return bookingId.get();
    }

    public void setBookingId(Integer id) {
        bookingId.set(id);
    }

    public Integer getBookingIdFromStore() {
        return bookingIdFromStore.get();
    }

    public void setBookingIdFromStore(Integer id) {
        bookingIdFromStore.set(id);
    }

    public String getBookingReferenceCode() {
        return bookingReferenceCode.get();
    }

    public void setBookingReferenceCode(String referenceCode) {
        bookingReferenceCode.set(referenceCode);
    }

    public static void reset() {
        requestPayload.remove();
        response.remove();
        token.remove();
        userId.remove();
        eventId.remove();
        bookingId.remove();
        bookingIdFromStore.remove();
        bookingReferenceCode.remove();
    }
}
