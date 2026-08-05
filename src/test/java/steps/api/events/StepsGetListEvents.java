package steps.api.events;

import api.context.ApiTestContext;
import api.payloads.EventPayloads;
import helpers.ApiReportHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.Map;

public class StepsGetListEvents {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I prepare events API query params")
    public void i_prepare_events_api_query_params(DataTable params) {
        Map<String, String> data = params.asMap(String.class, String.class);

        context.setRequestPayload(EventPayloads.getListEventsQueryParams(
                data.get("category"),
                data.get("city"),
                data.get("search"),
                data.get("page"),
                data.get("limit")
        ));
        ApiReportHelper.attachQueryParamsEvidence("Events API Query Params", context.getRequestPayload());
    }
}
