package api.services;

import base.BaseApiClient;
import io.restassured.response.Response;
import managers.EndpointManager;

import java.util.Map;

/**
 * Event API service.
 * Endpoints defined in EndpointManager, not here.
 */
public class EventService extends BaseApiClient {

    // Event CRUD operations

    public Response getAllEvents(String token) {
        return get(EndpointManager.getEndpoint("EVENTS"), token);
    }

    public Response getAllEvents() {
        return get(EndpointManager.getEndpoint("EVENTS"));
    }

    public Response getEventById(int eventId, String token) {
        return get(EndpointManager.getEndpoint("GET_EVENT"), token);
    }

    public Response getEventById(int eventId) {
        return get(EndpointManager.getEndpoint("GET_EVENT"), Map.of("id", eventId));
    }

    public Response createEvent(Map<String, Object> eventData, String token) {
        return request(token).body(eventData).post(EndpointManager.getEndpoint("CREATE_EVENT"));
    }

    public Response createEvent(Map<String, Object> eventData) {
        return request().body(eventData).post(EndpointManager.getEndpoint("CREATE_EVENT"));
    }

    public Response updateEvent(int eventId, Map<String, Object> eventData, String token) {
        return requestWithPathParams(Map.of("id", eventId))
                .body(eventData)
                .put(EndpointManager.getEndpoint("UPDATE_EVENT"));
    }

    public Response updateEvent(int eventId, Map<String, Object> eventData) {
        return requestWithPathParams(Map.of("id", eventId))
                .body(eventData)
                .put(EndpointManager.getEndpoint("UPDATE_EVENT"));
    }

    public Response deleteEvent(int eventId, String token) {
        return requestWithPathParams(Map.of("id", eventId))
                .delete(EndpointManager.getEndpoint("DELETE_EVENT"));
    }

    public Response deleteEvent(int eventId) {
        return requestWithPathParams(Map.of("id", eventId))
                .delete(EndpointManager.getEndpoint("DELETE_EVENT"));
    }

    // With query parameters (filtering, pagination)

    public Response getEvents(Map<String, Object> queryParams) {
        return get(EndpointManager.getEndpoint("EVENTS"), queryParams);
    }

    public Response getEvents(String token, Map<String, Object> queryParams) {
        return get(EndpointManager.getEndpoint("EVENTS"), token, queryParams);
    }

    // Extract event ID from response
    public Integer extractEventId(Response response) {
        return getInt(response, "id");
    }

    // Extract event name from response
    public String extractEventName(Response response) {
        return getString(response, "name");
    }
}
