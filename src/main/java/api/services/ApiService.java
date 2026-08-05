package api.services;

import base.BaseApiClient;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Unified API service for generic HTTP operations from step definitions.
 * Domain-specific operations use AuthService, BookingService, EventService.
 *
 * Usage in step definitions:
 *   apiService.post(endpoint, token, body)
 *   apiService.get(endpoint, token, queryParams)
 *   apiService.put(endpoint, token, body, pathParams)
 *   apiService.patch(endpoint, token, body)
 *   apiService.delete(endpoint, token, pathParams)
 */
public class ApiService extends BaseApiClient {

    /**
     * Send POST request with token and body
     */
    public Response post(String endpoint, String token, Map<String, Object> body) {
        RequestSpecification spec = request();
        if (token != null && !token.isBlank()) {
            spec.auth().oauth2(token);
        }
        return spec.body(body).post(endpoint);
    }

    /**
     * Send POST request with body only
     */
    public Response post(String endpoint, Map<String, Object> body) {
        return request().body(body).post(endpoint);
    }

    /**
     * Send GET request with token and query params
     */
    public Response get(String endpoint, String token, Map<String, Object> queryParams) {
        RequestSpecification spec = request();
        if (token != null && !token.isBlank()) {
            spec.auth().oauth2(token);
        }
        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }
        return spec.get(endpoint);
    }

    /**
     * Send GET request with query params only
     */
    public Response get(String endpoint, Map<String, Object> queryParams) {
        return get(endpoint, null, queryParams);
    }

    /**
     * Send GET request with token, query params, and path params
     */
    public Response get(String endpoint, String token, Map<String, Object> queryParams, Map<String, Object> pathParams) {
        RequestSpecification spec = buildSpec(endpoint, token, pathParams);
        if (queryParams != null && !queryParams.isEmpty()) {
            spec.queryParams(queryParams);
        }
        return spec.get(endpoint);
    }

    /**
     * Send GET request with token and path params (no query params)
     */
    public Response get(String endpoint, String token, Map<String, Object> pathParams, boolean unused) {
        return get(endpoint, token, null, pathParams);
    }

    /**
     * Send PUT request with token, body, and path params
     */
    public Response put(String endpoint, String token, Map<String, Object> body, Map<String, Object> pathParams) {
        RequestSpecification spec = buildSpec(endpoint, token, pathParams);
        return spec.body(body).put(endpoint);
    }

    /**
     * Send PUT request with body only
     */
    public Response put(String endpoint, Map<String, Object> body) {
        return put(endpoint, null, body, null);
    }

    /**
     * Send PATCH request with token and body
     */
    public Response patch(String endpoint, String token, Map<String, Object> body) {
        RequestSpecification spec = request();
        if (token != null && !token.isBlank()) {
            spec.auth().oauth2(token);
        }
        return spec.body(body).patch(endpoint);
    }

    /**
     * Send PATCH request with body only
     */
    public Response patch(String endpoint, Map<String, Object> body) {
        return patch(endpoint, null, body);
    }

    /**
     * Send DELETE request with token and path params
     */
    public Response delete(String endpoint, String token, Map<String, Object> pathParams) {
        RequestSpecification spec = buildSpec(endpoint, token, pathParams);
        return spec.delete(endpoint);
    }

    /**
     * Send DELETE request with path params only
     */
    public Response delete(String endpoint, Map<String, Object> pathParams) {
        return delete(endpoint, null, pathParams);
    }

    /**
     * Validate response meets expectations
     */
    public void validate(Response response) {
        validateResponse(response);
    }

    // --- Private helpers ---

    private RequestSpecification buildSpec(String endpoint, String token, Map<String, Object> pathParams) {
        RequestSpecification spec = request();
        if (token != null && !token.isBlank()) {
            spec.auth().oauth2(token);
        }
        if (pathParams != null && !pathParams.isEmpty()) {
            pathParams.forEach((key, value) -> spec.pathParam(key, value));
        }
        return spec;
    }
}
