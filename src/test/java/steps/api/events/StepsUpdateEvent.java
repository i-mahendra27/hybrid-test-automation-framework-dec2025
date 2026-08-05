package steps.api.events;

import api.assertions.EventAssertions;
import api.context.ApiTestContext;
import api.payloads.EventPayloads;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class StepsUpdateEvent {
    private final ApiTestContext context = ApiTestContext.getInstance();
    private final EventAssertions eventAssertions = new EventAssertions();

    @Given("I prepare update event API payload")
    public void i_prepare_update_event_api_payload() {
        context.setRequestPayload(EventPayloads.updateEventPayload());
        ApiReportHelper.attachPayloadEvidence("Update Event API Payload", context.getRequestPayload());
    }

    @Given("I prepare invalid update event API payload")
    public void i_prepare_invalid_update_event_api_payload() {
        context.setRequestPayload(EventPayloads.invalidUpdateEventPayload());
        ApiReportHelper.attachPayloadEvidence("Invalid Update Event API Payload", context.getRequestPayload());
    }

    @And("the update event request body should match update event request schema")
    public void the_update_event_request_body_should_match_update_event_request_schema() {
        ApiReportHelper.attachRequestSchemaEvidence("update-event-request.schema.json", context.getRequestPayload());
        eventAssertions.assertUpdateEventRequestSchema(context.getRequestPayload());
    }
}
