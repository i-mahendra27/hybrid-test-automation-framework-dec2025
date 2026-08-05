package steps.api.bookings;

import api.context.ApiTestContext;
import api.payloads.BookingPayloads;
import helpers.ApiReportHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.util.Map;

public class StepsGetBookings {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I prepare bookings API query params")
    public void i_prepare_bookings_api_query_params(DataTable params) {
        Map<String, String> data = params.asMap(String.class, String.class);

        context.setRequestPayload(BookingPayloads.getListBookingsQueryParams(
                data.get("eventId"),
                data.get("status"),
                data.get("page"),
                data.get("limit")
        ));
        ApiReportHelper.attachQueryParamsEvidence("Bookings API Query Params", context.getRequestPayload());
    }
}
