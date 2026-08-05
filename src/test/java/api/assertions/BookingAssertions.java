package api.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.io.File;
import java.util.List;
import java.util.Map;

public class BookingAssertions {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String SCHEMA_DIR = "src/test/java/api/schemas/bookings/";

    private static final File LIST_BOOKING_RESPONSE_200_SCHEMA = schema("get-booking-response-200.schema.json");
    private static final File CREATE_BOOKING_REQUEST_SCHEMA = schema("create-booking-request.schema.json");
    private static final File CREATE_BOOKING_RESPONSE_201_SCHEMA = schema("create-booking-response-201.schema.json");
    private static final File CREATE_BOOKING_RESPONSE_400_VALIDATION_SCHEMA = schema("create-booking-response-400_validation.schema.json");
    private static final File CREATE_BOOKING_RESPONSE_400_INSUFFICIENT_SCHEMA = schema("create-booking-response-400_inssuficient.schema.json");
    private static final File CREATE_BOOKING_RESPONSE_404_SCHEMA = schema("create-booking-response-404.schema.json");
    private static final File CREATE_BOOKING_RESPONSE_500_SCHEMA = schema("create-booking-response-500.schema.json");
    private static final File GET_BOOKING_REF_RESPONSE_200_SCHEMA = schema("reference-booking-response-200.schema.json");
    private static final File GET_BOOKING_REF_RESPONSE_404_SCHEMA = schema("reference-booking-response-404.schema.json");
    private static final File GET_BOOKING_REF_RESPONSE_500_SCHEMA = schema("reference-booking-response-500.schema.json");
    private static final File GET_BOOKING_ID_RESPONSE_200_SCHEMA = schema("get-booking-id-response-200.schema.json");
    private static final File GET_BOOKING_ID_RESPONSE_404_SCHEMA = schema("get-booking-id-response-404.schema.json");
    private static final File CANCEL_BOOKING_RESPONSE_200_SCHEMA = schema("delete-booking-response-200.schema.json");
    private static final File CANCEL_BOOKING_RESPONSE_404_SCHEMA = schema("delete-booking-response-404.schema.json");
    private static final File CANCEL_BOOKING_RESPONSE_500_SCHEMA = schema("delete-booking-response-500.schema.json");

    public boolean supportsApi(String apiName) {
        return normalize(apiName).contains("booking");
    }

    public void assertCreateBookingRequestSchema(Map<String, Object> payload) {
        Assertions.assertThat(CREATE_BOOKING_REQUEST_SCHEMA).exists();
        Assertions.assertThat(JsonSchemaValidator.matchesJsonSchema(CREATE_BOOKING_REQUEST_SCHEMA).matches(toJson(payload)))
                .as("Create booking request payload should match schema")
                .isTrue();
    }

    public void assertBookingApiResponseSchema(String apiName, String schemaType, Response response) {
        String normalizedApiName = normalize(apiName);
        String normalizedSchemaType = normalize(schemaType);
        int statusCode = response.statusCode();

        if (isListBooking(normalizedApiName)) {
            assertResponseMatchesSchema(response, LIST_BOOKING_RESPONSE_200_SCHEMA);
            return;
        }

        if (normalizedApiName.contains("create")) {
            assertCreateBookingResponseSchema(response, normalizedSchemaType, statusCode);
            return;
        }

        if (normalizedApiName.contains("reference") || normalizedApiName.contains("ref")) {
            assertGetBookingByReferenceResponseSchema(response, statusCode);
            return;
        }

        if (normalizedApiName.contains("get")) {
            assertGetBookingByIdResponseSchema(response, statusCode);
            return;
        }

        if (normalizedApiName.contains("cancel") || normalizedApiName.contains("delete")) {
            assertCancelBookingResponseSchema(response, statusCode);
            return;
        }

        throw unsupportedBookingSchema(apiName, schemaType, statusCode);
    }

    public void assertValidationErrorDetails(Response response) {
        assertResponseMatchesSchema(response, CREATE_BOOKING_RESPONSE_400_VALIDATION_SCHEMA);

        List<Map<String, Object>> details = response.jsonPath().getList("details");
        Assertions.assertThat(details)
                .as("Create booking validation error details")
                .anySatisfy(detail -> Assertions.assertThat(detail)
                        .containsEntry("field", "quantity")
                        .containsEntry("message", "Quantity must be an integer between 1 and 10"));
    }

    public void assertInsufficientSeatsError(Response response) {
        assertResponseMatchesSchema(response, CREATE_BOOKING_RESPONSE_400_INSUFFICIENT_SCHEMA);
        Assertions.assertThat(response.jsonPath().getString("error"))
                .as("Create booking insufficient seats error")
                .matches("^Only \\d+ seat\\(s\\) available, but \\d+ requested$");
    }

    private void assertCreateBookingResponseSchema(Response response, String schemaType, int statusCode) {
        switch (statusCode) {
            case 201 -> assertResponseMatchesSchema(response, CREATE_BOOKING_RESPONSE_201_SCHEMA);
            case 400 -> {
                File schema = schemaType.contains("insufficient") ? CREATE_BOOKING_RESPONSE_400_INSUFFICIENT_SCHEMA : CREATE_BOOKING_RESPONSE_400_VALIDATION_SCHEMA;
                assertResponseMatchesSchema(response, schema);
            }
            case 404 -> assertResponseMatchesSchema(response, CREATE_BOOKING_RESPONSE_404_SCHEMA);
            case 500 -> assertResponseMatchesSchema(response, CREATE_BOOKING_RESPONSE_500_SCHEMA);
            default -> throw unsupportedBookingSchema("create booking", schemaType, statusCode);
        }
    }

    private void assertGetBookingByReferenceResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, GET_BOOKING_REF_RESPONSE_200_SCHEMA);
            case 404 -> assertResponseMatchesSchema(response, GET_BOOKING_REF_RESPONSE_404_SCHEMA);
            case 500 -> assertResponseMatchesSchema(response, GET_BOOKING_REF_RESPONSE_500_SCHEMA);
            default -> throw unsupportedBookingSchema("get booking by reference code", "response", statusCode);
        }
    }

    private void assertGetBookingByIdResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, GET_BOOKING_ID_RESPONSE_200_SCHEMA);
            case 404 -> assertResponseMatchesSchema(response, GET_BOOKING_ID_RESPONSE_404_SCHEMA);
            default -> throw unsupportedBookingSchema("get booking by id", "response", statusCode);
        }
    }

    private void assertCancelBookingResponseSchema(Response response, int statusCode) {
        switch (statusCode) {
            case 200 -> assertResponseMatchesSchema(response, CANCEL_BOOKING_RESPONSE_200_SCHEMA);
            case 404 -> assertResponseMatchesSchema(response, CANCEL_BOOKING_RESPONSE_404_SCHEMA);
            case 500 -> assertResponseMatchesSchema(response, CANCEL_BOOKING_RESPONSE_500_SCHEMA);
            default -> throw unsupportedBookingSchema("cancel booking", "response", statusCode);
        }
    }

    // helper method
    private boolean isListBooking(String normalizedApiName) {
        return "bookings".equals(normalizedApiName) || normalizedApiName.contains("list");
    }

    private IllegalArgumentException unsupportedBookingSchema(String apiName, String schemaType, int statusCode) {
        return new IllegalArgumentException(
                "Unsupported booking API response schema: " + apiName + " " + schemaType + " " + statusCode
        );
    }

    private String normalize(String value) {
        return value.trim().replace("-", " ").replace("_", " ").toLowerCase();
    }

    private String toJson(Map<String, Object> payload) {
        try {
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize booking payload for schema validation", e);
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
