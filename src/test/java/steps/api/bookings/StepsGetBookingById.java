package steps.api.bookings;

import api.assertions.BookingAssertions;
import api.context.ApiTestContext;
import api.services.BookingService;
import helpers.ApiReportHelper;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import java.util.Map;

/**
 * Step definitions for GET booking by ID feature.
 */
public class StepsGetBookingById {
    private final ApiTestContext context = ApiTestContext.getInstance();
    private final BookingAssertions bookingAssertions = new BookingAssertions();
    private final BookingService bookingService = new BookingService();

    // Note: "I set booking id path parameter to {int}" is defined in StepsCancelBooking
    // Note: "I store booking id from API response" is defined in StepsCancelBooking

    @And("I verify booking id matches stored value")
    public void i_verify_booking_id_matches_stored_value() {
        Integer storedBookingId = context.getBookingId();
        Integer responseBookingId = context.getResponse().jsonPath().getInt("data.id");
        Assertions.assertThat(responseBookingId)
                .as("Booking ID in response should match stored value")
                .isEqualTo(storedBookingId);
    }

    @And("the get booking by id response should contain valid data")
    public void the_get_booking_by_id_response_should_contain_valid_data() {
        Response response = context.getResponse();
        bookingAssertions.assertGetBookingByIdResponseData(response);
    }
}
