package steps.api.bookings;

import api.context.ApiTestContext;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.assertj.core.api.Assertions;

import java.util.Map;

public class StepsCancelBooking {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("I set booking id path parameter to {int}")
    public void i_set_booking_id_path_parameter_to(int bookingId) {
        context.setBookingId(bookingId);
        ApiReportHelper.attachQueryParamsEvidence("Booking ID Path Parameter", Map.of("id", bookingId));
    }

    @And("I store booking id from API response")
    public void i_store_booking_id_from_api_response() {
        Integer bookingId = context.getResponse().jsonPath().getInt("data.id");
        Assertions.assertThat(bookingId)
                .as("Booking id from API response")
                .isNotNull()
                .isPositive();
        context.setBookingId(bookingId);
        ApiReportHelper.attachQueryParamsEvidence("Stored Booking ID", Map.of("id", bookingId));
    }
}
