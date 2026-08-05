package api.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.util.Map;

public class EventAssertions {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String SCHEMA_DIR = "src/test/java/api/schemas/events/";

    private static final File LIST_EVENTS_RESPONSE_200_SCHEMA = schema("list-event-response-200.schema.json");
    private static final File CREATE_NEW_EVENT_SCHEMA = schema("create-new-event-request.schema.json");
    private static final File CREATE_NEW_EVENT_RESPONSE_201_SCHEMA = schema("create-new-event-response-201.schema.json");
    private static final File CREATE_NEW_EVENT_RESPONSE_400_SCHEMA = schema("create-new-event-response-400.schema.json");
    private static final File GET_EVENT_BY_ID_RESPONSE_200_SCHEMA = schema("get-event-response-200.schema.json");
    private static final File GET_EVENT_BY_ID_RESPONSE_404_SCHEMA = schema("get-event-response-404.schema.json");
    private static final File GET_EVENT_BY_ID_RESPONSE_500_SCHEMA = schema("get-event-response-500.schema.json");
    private static final File UPDATE_EVENT_REQUEST_SCHEMA = schema("update-event-request.schema.json");
    private static final File UPDATE_EVENT_RESPONSE_200_SCHEMA = schema("update-event-response-200.schema.json");
    private static final File UPDATE_EVENT_RESPONSE_400_SCHEMA = schema("update-event-response-400.schema.json");
    private static final File UPDATE_EVENT_RESPONSE_404_SCHEMA = schema("update-event-response-404.schema.json");
    private static final File DELETE_EVENT_RESPONSE_200_SCHEMA = schema("delete-event-response-200.schema.json");
    private static final File DELETE_EVENT_RESPONSE_404_SCHEMA = schema("delete-event-response-404.schema.json");
    private static final File DELETE_EVENT_RESPONSE_500_SCHEMA = schema("delete-event-response-500.schema.json");

    public void assertListEventsResponseSchema200(Response response) {
        assertResponseMatchesSchema(response, LIST_EVENTS_RESPONSE_200_SCHEMA);
    }

    public void assertCreateNewEventRequestSchema(Map<String, Object> payload) {
        assertRequestMatchesSchema(payload, CREATE_NEW_EVENT_SCHEMA, "Create new event request payload should match schema");
    }

    public void assertUpdateEventRequestSchema(Map<String, Object> payload) {
        assertRequestMatchesSchema(payload, UPDATE_EVENT_REQUEST_SCHEMA, "Update event request payload should match schema");
    }

    public boolean supportsApi(String apiName) {
        String normalizedApiName = normalize(apiName);
        return normalizedApiName.contains("event");
    }

    public void assertEventApiResponseSchema(String apiName, String schemaType, Response response) {
        String normalizedApiName = normalize(apiName);
        String normalizedSchemaType = normalize(schemaType);
        int statusCode = response.statusCode();

        if (isListEvent(normalizedApiName)) {
            assertListEventsResponseSchema200(response);
            return;
        }

        if (normalizedApiName.contains("create")) {
            assertCreateEventResponseSchema(response, statusCode);
            return;
        }

        if (normalizedApiName.contains("get")) {
            assertGetEventByIdResponseSchema(response, statusCode);
            return;
        }

        if (normalizedApiName.contains("update")) {
            assertUpdateEventResponseSchema(response, statusCode);
            return;
        }

        if (normalizedApiName.contains("delete")) {
            assertDeleteEventResponseSchema(response, statusCode);
            return;
        }

        if ("success".equals(normalizedSchemaType) && statusCode == 200) {
            assertListEventsResponseSchema200(response);
            return;
        }

        throw unsupportedEventSchema(apiName, schemaType, statusCode);
    }

    private void assertCreateEventResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 201 -> assertResponseMatchesSchema(response, CREATE_NEW_EVENT_RESPONSE_201_SCHEMA);
            case 400 -> assertResponseMatchesSchema(response, CREATE_NEW_EVENT_RESPONSE_400_SCHEMA);
            default -> throw unsupportedEventSchema("create event", "response", statusCode);
        }
    }

    private void assertGetEventByIdResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, GET_EVENT_BY_ID_RESPONSE_200_SCHEMA);
            case 404 -> assertResponseMatchesSchema(response, GET_EVENT_BY_ID_RESPONSE_404_SCHEMA);
            case 500 -> assertResponseMatchesSchema(response, GET_EVENT_BY_ID_RESPONSE_500_SCHEMA);
            default -> throw unsupportedEventSchema("get event by id", "response", statusCode);
        }
    }

    private void assertUpdateEventResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, UPDATE_EVENT_RESPONSE_200_SCHEMA);
            case 400 -> assertResponseMatchesSchema(response, UPDATE_EVENT_RESPONSE_400_SCHEMA);
            case 404 -> assertResponseMatchesSchema(response, UPDATE_EVENT_RESPONSE_404_SCHEMA);
            default -> throw unsupportedEventSchema("update event", "response", statusCode);
        }
    }

    private void assertDeleteEventResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, DELETE_EVENT_RESPONSE_200_SCHEMA);
            case 404 -> assertResponseMatchesSchema(response, DELETE_EVENT_RESPONSE_404_SCHEMA);
            case 500 -> assertResponseMatchesSchema(response, DELETE_EVENT_RESPONSE_500_SCHEMA);
            default -> throw unsupportedEventSchema("delete event", "response", statusCode);
        }
    }

    private boolean isListEvent(String normalizedApiName) {
        return "events".equals(normalizedApiName) || normalizedApiName.contains("list");
    }

    private IllegalArgumentException unsupportedEventSchema(String apiName, String schemaType, int statusCode) {
        return new IllegalArgumentException(
                "Unsupported event API response schema: " + apiName + " " + schemaType + " " + statusCode
        );
    }

    private String normalize(String value) {
        return value.trim().replace("-", " ").replace("_", " ").toLowerCase();
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize event payload for schema validation", e);
        }
    }

    private void assertRequestMatchesSchema(Map<String, Object> payload, File schema, String assertionMessage) {
        Assertions.assertThat(schema).exists();
        Assertions.assertThat(JsonSchemaValidator.matchesJsonSchema(schema).matches(toJson(payload)))
                .as(assertionMessage)
                .isTrue();
    }

    private void assertResponseMatchesSchema(Response response, File schema) {
        Assertions.assertThat(schema).exists();
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schema));
    }

    private static File schema(String fileName) {
        return new File(SCHEMA_DIR + fileName);
    }
}
