package steps.api.events;

import api.assertions.EventAssertions;
import api.context.ApiTestContext;
import api.payloads.EventPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class StepsCreateNewEvent {
    private final ApiTestContext context = ApiTestContext.getInstance();
    private final EventAssertions eventAssertions = new EventAssertions();

    @Given("I prepare create new event API payload")
    public void i_prepare_create_new_event_api_payload() {
        context.setRequestPayload(EventPayloads.createNewEventPayload());
        ApiReportHelper.attachPayloadEvidence("Create New Event API Payload", context.getRequestPayload());
    }

    @Given("I prepare create new event API payload with limited seats")
    public void i_prepare_create_new_event_api_payload_with_limited_seats() {
        context.setRequestPayload(EventPayloads.createNewEventPayloadWithLimitedSeats());
        ApiReportHelper.attachPayloadEvidence("Create New Event With Limited Seats Payload", context.getRequestPayload());
    }

    @Given("I prepare invalid create new event API payload")
    public void i_prepare_invalid_create_new_event_api_payload() {
        context.setRequestPayload(EventPayloads.invalidCreateNewEventPayload());
        ApiReportHelper.attachPayloadEvidence("Invalid Create New Event API Payload", context.getRequestPayload());
    }

    @And("the create new event request body should match create new event request schema")
    public void the_create_new_event_request_body_should_match_create_new_event_request_schema() {
        ApiReportHelper.attachRequestSchemaEvidence("create-new-event-request.schema.json", context.getRequestPayload());
        eventAssertions.assertCreateNewEventRequestSchema(context.getRequestPayload());
    }
}
