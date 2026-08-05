package api.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.util.Map;

public class AuthAssertions {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String SCHEMA_DIR = "src/test/java/api/schemas/auth/";

    private static final File LOGIN_REQUEST_SCHEMA = schema("login-request.schema.json");
    private static final File REGISTER_REQUEST_SCHEMA = schema("register-request.schema.json");
    private static final File REGISTER_RESPONSE_201_SCHEMA = schema("register-response-201.schema.json");
    private static final File REGISTER_RESPONSE_400_SCHEMA = schema("register-response-400.schema.json");
    private static final File LOGIN_RESPONSE_200_SCHEMA = schema("login-response-200.schema.json");
    private static final File LOGIN_RESPONSE_400_SCHEMA = schema("login-response-400.schema.json");
    private static final File AUTH_RESPONSE_200_SCHEMA = schema("get-auth-response-200.schema.json");
    private static final File AUTH_RESPONSE_401_SCHEMA = schema("get-auth-response-401.schema.json");

    public void assertLoginRequestSchema(Map<String, Object> payload) {
        assertRequestMatchesSchema(payload, LOGIN_REQUEST_SCHEMA, "Login request payload should match schema");
    }

    public void assertRegisterRequestSchema(Map<String, Object> payload) {
        assertRequestMatchesSchema(payload, REGISTER_REQUEST_SCHEMA, "Register request payload should match schema");
    }

    public void assertLoginRequestDoesNotMatchSchema(Map<String, Object> payload) {
        Assertions.assertThat(LOGIN_REQUEST_SCHEMA).exists();
        Assertions.assertThat(JsonSchemaValidator.matchesJsonSchema(LOGIN_REQUEST_SCHEMA).matches(toJson(payload)))
                .as("Login request payload should not match schema")
                .isFalse();
    }

    public boolean supportsApi(String apiName) {
        String normalizedApiName = normalize(apiName);
        return normalizedApiName.contains("login")
                || normalizedApiName.contains("register")
                || normalizedApiName.contains("auth")
                || normalizedApiName.equals("me");
    }

    public void assertStatusCode(Response response, int expectedStatusCode) {
        Assertions.assertThat(response)
                .as("API response should not be null")
                .isNotNull();
        Assertions.assertThat(response.statusCode())
                .as("API response status code")
                .isEqualTo(expectedStatusCode);
    }

    public void assertSuccessfulLoginResponse(Response response, String expectedEmail) {
        assertLoginResponseSchema200(response);
        assertAccessToken(response);
        assertUserObject(response, expectedEmail);
        Assertions.assertThat(response.jsonPath().getBoolean("success")).isTrue();
    }

    public void assertSuccessfulRegisterResponse(Response response, String expectedEmail) {
        assertRegisterResponseSchema201(response);
        assertAccessToken(response);
        assertUserObject(response, expectedEmail);
        Assertions.assertThat(response.jsonPath().getBoolean("success")).isTrue();
    }

    public void assertUnauthorizedLoginResponse(Response response) {
        assertResponseSchema(response, response.statusCode());
        Assertions.assertThat(response.jsonPath().getBoolean("success")).isFalse();
        Assertions.assertThat(response.jsonPath().getString("error")).isNotBlank();
    }

    public void assertAuthApiResponseSchema(String apiName, String schemaType, Response response, String expectedEmail) {
        String normalizedApiName = normalize(apiName);
        int statusCode = response.statusCode();

        if (normalizedApiName.contains("login")) {
            assertLoginResponseSchema(response, statusCode, expectedEmail);
            return;
        }

        if (normalizedApiName.contains("register")) {
            assertRegisterResponseSchema(response, statusCode, expectedEmail);
            return;
        }

        if (normalizedApiName.contains("auth") || normalizedApiName.equals("me")) {
            assertAuthMeResponseSchema(response, statusCode);
            return;
        }

        throw unsupportedAuthSchema(apiName, schemaType, statusCode);
    }

    public void assertLoginResponseSchema200(Response response) {
        assertResponseMatchesSchema(response, LOGIN_RESPONSE_200_SCHEMA);
    }

    public void assertRegisterResponseSchema201(Response response) {
        assertResponseMatchesSchema(response, REGISTER_RESPONSE_201_SCHEMA);
    }

    public void assertAuthResponseSchema401(Response response) {
        assertResponseMatchesSchema(response, AUTH_RESPONSE_401_SCHEMA);
    }

    public void assertLoginResponseSchema400(Response response) {
        assertResponseMatchesSchema(response, LOGIN_RESPONSE_400_SCHEMA);
    }

    public void assertRegisterResponseSchema400(Response response) {
        assertResponseMatchesSchema(response, REGISTER_RESPONSE_400_SCHEMA);
    }

    public void assertResponseSchema(Response response, int statusCode) {
        assertLoginResponseSchema(response, statusCode, null);
    }

    public void assertLoginErrorResponse(Response response) {
        assertLoginResponseSchema400(response);
        assertErrorResponse(response);
    }

    public void assertRegisterErrorResponse(Response response) {
        assertRegisterResponseSchema400(response);
        assertErrorResponse(response);
    }

    // method helper

    public void assertUserObject(Response response, String expectedEmail) {
        Assertions.assertThat(response.jsonPath().getMap("user"))
                .as("User object")
                .isNotNull()
                .containsKey("id")
                .containsKey("email");
        Assertions.assertThat(response.jsonPath().getInt("user.id"))
                .as("User id")
                .isPositive();
        Assertions.assertThat(response.jsonPath().getString("user.email"))
                .as("User email")
                .isEqualTo(expectedEmail);
    }

    public void assertAccessToken(Response response) {
        Assertions.assertThat(response.jsonPath().getString("token"))
                .as("Access token")
                .isNotBlank()
                .contains(".");
    }

    private void assertErrorResponse(Response response) {
        Assertions.assertThat(response.jsonPath().getBoolean("success")).isFalse();
        Assertions.assertThat(response.jsonPath().getString("error")).isNotBlank();
    }

    private void assertLoginResponseSchema(Response response, int statusCode, String expectedEmail) {
        switch (statusCode) {
            case 200 -> {
                assertLoginResponseSchema200(response);
                assertAccessToken(response);
                if (expectedEmail != null) {
                    assertUserObject(response, expectedEmail);
                }
            }
            case 400 -> assertLoginErrorResponse(response);
            default -> throw unsupportedAuthSchema("login", "response", statusCode);
        }
    }

    private void assertRegisterResponseSchema(Response response, int statusCode, String expectedEmail) {
        switch (statusCode) {
            case 201 -> {
                assertRegisterResponseSchema201(response);
                assertAccessToken(response);
                if (expectedEmail != null) {
                    assertUserObject(response, expectedEmail);
                }
            }
            case 400 -> assertRegisterErrorResponse(response);
            default -> throw unsupportedAuthSchema("register", "response", statusCode);
        }
    }

    private void assertAuthMeResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, AUTH_RESPONSE_200_SCHEMA);
            case 401 -> assertAuthResponseSchema401(response);
            default -> throw unsupportedAuthSchema("auth", "response", statusCode);
        }
    }

    private void assertRequestMatchesSchema(Map<String, Object> payload, File schema, String assertionMessage) {
        Assertions.assertThat(schema).exists();
        Assertions.assertThat(JsonSchemaValidator.matchesJsonSchema(schema).matches(toJson(payload)))
                .as(assertionMessage)
                .isTrue();
    }

    private IllegalArgumentException unsupportedAuthSchema(String apiName, String schemaType, int statusCode) {
        return new IllegalArgumentException(
                "Unsupported auth API response schema: " + apiName + " " + schemaType + " " + statusCode
        );
    }

    private String normalize(String value) {
        return value.trim().replace("-", " ").replace("_", " ").toLowerCase();
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize login payload for schema validation", e);
        }
    }

    private void assertResponseMatchesSchema(Response response, File schema) {
        Assertions.assertThat(schema).exists();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schema));
    }

    private static File schema(String fileName) {
        return new File(SCHEMA_DIR + fileName);
    }
}
