package steps.api.bookings;

import api.context.ApiTestContext;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.assertj.core.api.Assertions;

import java.util.Map;

public class StepsGetBookingByReferenceCode {
    private final ApiTestContext context = ApiTestContext.getInstance();

    @Given("^I set reference code path parameter to (.+)$")
    public void i_set_reference_code_path_parameter_to(String referenceCode) {
        String trimmedReferenceCode = referenceCode.trim();
        context.setBookingReferenceCode(trimmedReferenceCode);
        ApiReportHelper.attachQueryParamsEvidence("Booking Reference Path Parameter", Map.of("ref", trimmedReferenceCode));
    }

    @And("I store booking reference code from API response")
    public void i_store_booking_reference_code_from_api_response() {
        String bookingReferenceCode = context.getResponse().jsonPath().getString("data.bookingRef");
        Assertions.assertThat(bookingReferenceCode)
                .as("Booking reference code from API response")
                .isNotBlank();
        context.setBookingReferenceCode(bookingReferenceCode);
        ApiReportHelper.attachQueryParamsEvidence("Stored Booking Reference Code", Map.of("ref", bookingReferenceCode));
    }
}
