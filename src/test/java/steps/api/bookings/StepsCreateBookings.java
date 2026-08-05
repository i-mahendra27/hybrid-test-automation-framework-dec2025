package steps.api.bookings;

import api.assertions.BookingAssertions;
import api.context.ApiTestContext;
import api.payloads.BookingPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.assertj.core.api.Assertions;

public class StepsCreateBookings {
    private final ApiTestContext context = ApiTestContext.getInstance();
    private final BookingAssertions bookingAssertions = new BookingAssertions();

    @Given("I prepare create new booking API payload")
    public void i_prepare_create_new_booking_api_payload() {
        context.setRequestPayload(BookingPayloads.createBookingPayload());
        ApiReportHelper.attachPayloadEvidence("Create New Booking API Payload", context.getRequestPayload());
    }

    @Given("I prepare create new booking API payload for stored event")
    public void i_prepare_create_new_booking_api_payload_for_stored_event() {
        Integer eventId = context.getEventId();
        Assertions.assertThat(eventId)
                .as("Stored event id for create booking payload")
                .isNotNull()
                .isPositive();
        context.setRequestPayload(BookingPayloads.createBookingPayloadForEvent(eventId));
        ApiReportHelper.attachPayloadEvidence("Create New Booking API Payload", context.getRequestPayload());
    }

    @Given("I prepare an invalid create booking API payload")
    public void i_prepare_an_invalid_create_booking_api_payload() {
        Integer eventId = context.getEventId();
        if (eventId != null && eventId > 0) {
            // Use stored event ID if available
            context.setRequestPayload(BookingPayloads.createBookingPayloadForEvent(eventId));
        } else {
            context.setRequestPayload(BookingPayloads.createBookingPayload());
        }
        // Make it invalid by setting quantity out of range
        context.getRequestPayload().put("quantity", 11);
        ApiReportHelper.attachPayloadEvidence("Invalid Create Booking API Payload", context.getRequestPayload());
    }

    @Given("I prepare a create booking API payload with a quantity exceeding the available seats")
    public void i_prepare_a_create_booking_api_payload_with_a_quantity_exceeding_the_available_seats() {
        Integer eventId = context.getEventId();
        Assertions.assertThat(eventId)
                .as("Stored event id for insufficient seats test")
                .isNotNull()
                .isPositive();
        context.setRequestPayload(BookingPayloads.insufficientSeatsBookingPayload(eventId));
        ApiReportHelper.attachPayloadEvidence("Insufficient Seats Booking API Payload", context.getRequestPayload());
    }

    @Given("I prepare a create booking API payload with a non-existent event ID")
    public void i_prepare_a_create_booking_api_payload_with_a_non_existent_event_id() {
        context.setRequestPayload(BookingPayloads.nonExistentEventBookingPayload());
        ApiReportHelper.attachPayloadEvidence("Non-existent Event Booking API Payload", context.getRequestPayload());
    }

    @And("the create new booking request body should match create new booking request schema")
    public void the_create_new_booking_request_body_should_match_create_new_booking_request_schema() {
        ApiReportHelper.attachRequestSchemaEvidence("create-booking-request.schema.json", context.getRequestPayload());
        bookingAssertions.assertCreateBookingRequestSchema(context.getRequestPayload());
    }

    @And("the response should contain validation error details")
    public void the_response_should_contain_validation_error_details() {
        bookingAssertions.assertValidationErrorDetails(context.getResponse());
    }

    @And("the response should contain an insufficient seats error")
    public void the_response_should_contain_an_insufficient_seats_error() {
        bookingAssertions.assertInsufficientSeatsError(context.getResponse());
    }
}
