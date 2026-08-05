package steps.ui;

import hooks.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import pages.MyBookingPage;
import pages.dto.EventBookDetailDataObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepsViewBookedEventDetails {
    private final TestContext testContext;
    private final MyBookingPage myBookingPage;
    private EventBookDetailDataObject bookingData;

    public StepsViewBookedEventDetails(TestContext testContext) {
        this.testContext = testContext;
        this.myBookingPage = new MyBookingPage();
        this.bookingData = new EventBookDetailDataObject();
    }

    @Then("I should be redirected to booking detail page")
    public void i_should_be_redirected_to_booking_detail_page(){
        String selectedEventName = testContext.events().getSelectedEventName();
        if (selectedEventName == null) {
            throw new IllegalStateException("Selected event name is not available.");
        }

        myBookingPage.verifyTextVisible(selectedEventName);

        String currentUrl = myBookingPage.getCurrentURL();
        myBookingPage.verifyTrue(currentUrl.matches(".*/bookings/\\d+$"),
                "Expected URL to contain /bookings, but current URL is: " + currentUrl);
    }

    @Then("I should see customer information:")
    public void i_should_see_customer_information(DataTable dataTable) {
        List<String> fields = dataTable.asList();
        bookingData = testContext.booking().getBookingData();

        if (bookingData == null) {
            throw new IllegalStateException("Booking data is not available. Create a booking before verifying customer information.");
        }

        Map<String, String> actualBookingInfo = myBookingPage.getCustomerInformation(fields);

        Map<String, String> expectedBookingInfo = new HashMap<>();
        expectedBookingInfo.put("Name", bookingData.getFullName());
        expectedBookingInfo.put("Email", bookingData.getEmail());
        expectedBookingInfo.put("Phone", bookingData.getPhoneNumber());
        expectedBookingInfo.put("Tickets", String.valueOf(bookingData.getNumOfTickets()));

        expectedBookingInfo.forEach((field, expectedValue) -> {
            String actualValue = actualBookingInfo.get(field);
            myBookingPage.verifyEquals(actualValue, expectedValue);
        });
    }

    @Then("the total paid amount should be calculated correctly")
    public void the_total_paid_amount_should_be_calculated_correctly() {
        bookingData = testContext.booking().getBookingData();
        Integer selectedEventPrice = testContext.events().getSelectedEventPrice();

        if (bookingData == null){
            throw new IllegalStateException("Booking data is not available");
        }

        if (selectedEventPrice == null){
            throw new IllegalStateException("Selected event price is not available");
        }

        int expectedTotalPaidAmount = bookingData.getNumOfTickets() * selectedEventPrice;
        int actualTotalPaidAmount = myBookingPage.getCurrentTotalPaidAmount();

        myBookingPage.verifyEquals(actualTotalPaidAmount, expectedTotalPaidAmount);
    }
}
