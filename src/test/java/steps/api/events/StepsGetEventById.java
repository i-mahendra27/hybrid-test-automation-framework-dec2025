package steps.api.events;

import api.context.ApiTestContext;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.assertj.core.api.Assertions;

import java.util.Map;

public class StepsGetEventById {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I set event id path parameter to {int}")
    public void i_set_event_id_path_parameter_to(int eventId) {
        context.setEventId(eventId);
        ApiReportHelper.attachQueryParamsEvidence("Event ID Path Parameter", Map.of("id", eventId));
    }

    @And("I store event id from API response")
    public void i_store_event_id_from_api_response() {
        Integer eventId = context.getResponse().jsonPath().getInt("data.id");
        Assertions.assertThat(eventId)
                .as("Event id from API response").isNotNull().isPositive();
        context.setEventId(eventId);
        ApiReportHelper.attachQueryParamsEvidence("Stored Event ID", Map.of("id", eventId));
    }
}
