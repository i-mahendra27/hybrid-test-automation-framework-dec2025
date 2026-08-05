package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import managers.ConfigManager;
import utils.LogUtils;

import java.util.Map;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.LogConfig.logConfig;

/**
 * Abstract base API client wrapping RestAssured operations.
 * Services extend this with: class AuthService extends BaseApiClient
 */
public abstract class BaseApiClient {

    private static final String DEFAULT_TIMEOUT_SECONDS = "20";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String HIDDEN_VALUE = "HIDDEN";

    // Request Specification

    protected RequestSpecification request() {
        return RestAssured.given().spec(baseRequestSpec());
    }

    protected RequestSpecification request(String token) {
        RequestSpecification req = request();
        if (token != null && !token.isBlank()) {
            req.auth().oauth2(token);
        }
        return req;
    }

    protected RequestSpecification requestWithBasicAuth(String username, String password) {
        return request().auth().basic(username, password);
    }

    protected RequestSpecification requestWithBearerToken(String token) {
        RequestSpecification req = request();
        if (token != null && !token.isBlank()) {
            req.header(AUTHORIZATION_HEADER, "Bearer " + token);
        }
        return req;
    }

    protected RequestSpecification requestWithHeaders(Map<String, String> headers) {
        RequestSpecification req = request();
        if (headers != null) {
            headers.forEach(req::header);
        }
        return req;
    }

    protected RequestSpecification requestWithPathParams(Map<String, Object> pathParams) {
        RequestSpecification req = request();
        if (pathParams != null) {
            pathParams.forEach((key, value) -> req.pathParam(key, value));
        }
        return req;
    }

    protected RequestSpecification requestWithQueryParams(Map<String, Object> queryParams) {
        RequestSpecification req = request();
        if (queryParams != null) {
            queryParams.forEach(req::queryParam);
        }
        return req;
    }

    protected RequestSpecification requestWithFormParams(Map<String, String> formParams) {
        RequestSpecification req = request().contentType(ContentType.URLENC);
        if (formParams != null) {
            formParams.forEach(req::formParam);
        }
        return req;
    }

    // HTTP Methods

    protected Response get(String endpoint) {
        return request().get(endpoint);
    }

    protected Response get(String endpoint, String token) {
        return request(token).get(endpoint);
    }

    protected Response get(String endpoint, Map<String, Object> queryParams) {
        return request().queryParams(queryParams).get(endpoint);
    }

    protected Response get(String endpoint, String token, Map<String, Object> queryParams) {
        return request(token).queryParams(queryParams).get(endpoint);
    }

    protected Response post(String endpoint, Object body) {
        return request().body(body).post(endpoint);
    }

    protected Response post(String endpoint, String token, Object body) {
        return request(token).body(body).post(endpoint);
    }

    protected Response post(String endpoint, Object body, Map<String, Object> queryParams) {
        return request().queryParams(queryParams).body(body).post(endpoint);
    }

    protected Response post(String endpoint, String token, Object body, Map<String, Object> queryParams) {
        return request(token).queryParams(queryParams).body(body).post(endpoint);
    }

    protected Response put(String endpoint, Object body) {
        return request().body(body).put(endpoint);
    }

    protected Response put(String endpoint, String token, Object body) {
        return request(token).body(body).put(endpoint);
    }

    protected Response put(String endpoint, Object body, Map<String, Object> queryParams) {
        return request().queryParams(queryParams).body(body).put(endpoint);
    }

    protected Response patch(String endpoint, Object body) {
        return request().body(body).patch(endpoint);
    }

    protected Response patch(String endpoint, String token, Object body) {
        return request(token).body(body).patch(endpoint);
    }

    protected Response patch(String endpoint, Object body, Map<String, Object> queryParams) {
        return request().queryParams(queryParams).body(body).patch(endpoint);
    }

    protected Response delete(String endpoint) {
        return request().delete(endpoint);
    }

    protected Response delete(String endpoint, String token) {
        return request(token).delete(endpoint);
    }

    protected Response delete(String endpoint, Map<String, Object> pathParams) {
        RequestSpecification req = request();
        if (pathParams != null) {
            pathParams.forEach((key, value) -> req.pathParam(key, value));
        }
        return req.delete(endpoint);
    }

    protected Response delete(String endpoint, String token, Map<String, Object> pathParams) {
        RequestSpecification req = request(token);
        if (pathParams != null) {
            pathParams.forEach((key, value) -> req.pathParam(key, value));
        }
        return req.delete(endpoint);
    }

    // Response Specification

    protected ResponseSpecification defaultResponseSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .build();
    }

    protected ResponseSpecification responseSpecWithStatus(int statusCode) {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(statusCode)
                .build();
    }

    protected void validateResponse(Response response) {
        response.then().spec(defaultResponseSpec());
    }

    protected void validateResponse(Response response, int expectedStatusCode) {
        response.then().spec(responseSpecWithStatus(expectedStatusCode));
    }

    //Base Configuration

    protected RequestSpecification baseRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(apiConfig())
                .addFilter(logOnFailure())
                .build();
    }

    protected String getBaseUrl() {
        return ConfigManager.getProperty("API_BASE_URL");
    }

    protected int getTimeoutInMilliseconds() {
        String timeout = ConfigManager.getProperty("API_TIMEOUT", DEFAULT_TIMEOUT_SECONDS).trim();
        return Integer.parseInt(timeout) * 1000;
    }

    // Configuration

    private RestAssuredConfig apiConfig() {
        int timeoutInMilliseconds = getTimeoutInMilliseconds();
        return RestAssuredConfig.config()
                .httpClient(httpClientConfig()
                        .setParam("http.connection.timeout", timeoutInMilliseconds)
                        .setParam("http.socket.timeout", timeoutInMilliseconds)
                        .setParam("http.connection-manager.timeout", (long) timeoutInMilliseconds))
                .logConfig(logConfig()
                        .blacklistDefaultSensitiveHeaders()
                        .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL));
    }

    //Logging Filters

    private Filter logOnFailure() {
        return (FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) -> {
            Response response = context.next(requestSpec, responseSpec);
            if (response.statusCode() >= 500) {
                logFailedRequest(requestSpec);
                logFailedResponse(response);
            }
            return response;
        };
    }

    private void logFailedRequest(FilterableRequestSpecification requestSpec) {
        StringBuilder logMessage = new StringBuilder()
                .append("========== API REQUEST FAILED ==========")
                .append(System.lineSeparator())
                .append("Method: ").append(requestSpec.getMethod())
                .append(System.lineSeparator())
                .append("URI: ").append(requestSpec.getURI())
                .append(System.lineSeparator())
                .append("Headers: ").append(formatHeaders(requestSpec.getHeaders()));
        Object body = requestSpec.getBody();
        if (body != null) {
            logMessage.append(System.lineSeparator()).append("Body: ").append(maskSensitiveValues(body.toString()));
        }
        LogUtils.error(logMessage.toString());
    }

    private void logFailedResponse(Response response) {
        String logMessage = "========== API RESPONSE FAILED =========="
                + System.lineSeparator()
                + "Status: " + response.statusLine()
                + System.lineSeparator()
                + "Headers: " + response.getHeaders()
                + System.lineSeparator()
                + "Body: " + maskSensitiveValues(response.asPrettyString());
        LogUtils.error(logMessage);
    }

    private String formatHeaders(Headers headers) {
        StringBuilder formattedHeaders = new StringBuilder("[");
        for (Header header : headers) {
            if (formattedHeaders.length() > 1) {
                formattedHeaders.append(", ");
            }
            formattedHeaders.append(header.getName()).append("=");
            if (AUTHORIZATION_HEADER.equalsIgnoreCase(header.getName())) {
                formattedHeaders.append(HIDDEN_VALUE);
            } else {
                formattedHeaders.append(header.getValue());
            }
        }
        return formattedHeaders.append("]").toString();
    }

    private String maskSensitiveValues(String value) {
        if (value == null) {
            return null;
        }
        return value.replaceAll("(?i)(\"(?:password|token|secret|key)\"\\s*:\\s*\")([^\"]*)(\")", "$1****$3");
    }

    // Utility Methods

    protected <T> T parseResponse(Response response, String jsonPath) {
        return response.jsonPath().getObject(jsonPath, (Class<T>) Object.class);
    }

    protected String getString(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    protected Integer getInt(Response response, String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }

    protected Boolean getBoolean(Response response, String jsonPath) {
        return response.jsonPath().getBoolean(jsonPath);
    }
}
