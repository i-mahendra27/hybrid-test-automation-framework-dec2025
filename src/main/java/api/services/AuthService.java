package api.services;

import base.BaseApiClient;
import io.restassured.response.Response;
import managers.EndpointManager;

import java.util.Map;

/**
 * Authentication API service.
 * Endpoints defined in EndpointManager, not here.
 */
public class AuthService extends BaseApiClient {

    // Auth operations - endpoints from EndpointManager

    public Response login(String email, String password) {
        Map<String, Object> body = Map.of(
                "email", email,
                "password", password
        );
        return post(EndpointManager.getEndpoint("LOGIN"), body);
    }

    public Response login(Map<String, Object> credentials) {
        return post(EndpointManager.getEndpoint("LOGIN"), credentials);
    }

    public Response register(String email, String password, String confirmPassword) {
        Map<String, Object> body = Map.of(
                "email", email,
                "password", password,
                "confirmPassword", confirmPassword
        );
        return post(EndpointManager.getEndpoint("REGISTER"), body);
    }

    public Response register(Map<String, Object> registrationData) {
        return post(EndpointManager.getEndpoint("REGISTER"), registrationData);
    }

    public Response validateToken(String token) {
        return get(EndpointManager.getEndpoint("AUTH_ME"), token);
    }

    public Response getCurrentUser(String token) {
        return get(EndpointManager.getEndpoint("ME"), token);
    }

    // Login with response validation
    public Response loginAndValidate(String email, String password, int expectedStatus) {
        Response response = login(email, password);
        validateResponse(response, expectedStatus);
        return response;
    }

    // Get token from login response
    public String extractToken(Response loginResponse) {
        return getString(loginResponse, "token");
    }

    // Get user ID from login response
    public Integer extractUserId(Response loginResponse) {
        return getInt(loginResponse, "user.id");
    }

    // Public Access Methods

    /**
     * Public access to base URL
     */
    public String baseUrl() {
        return getBaseUrl();
    }

    /**
     * Public access to response validation
     */
    public void validate(Response response) {
        validateResponse(response);
    }
}
