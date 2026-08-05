package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import pages.MyBookingPage;
import utils.LogUtils;

public class StepsDeleteBookedEvent {
    private final TestContext testContext;
    private final MyBookingPage myBookingPage;

    public StepsDeleteBookedEvent(TestContext testContext) {
        this.testContext = testContext;
        this.myBookingPage = new MyBookingPage();
    }

    @Given("I note the booked event name")
    public void i_note_the_booked_event_name() {
        String bookedEventName = testContext.events().getSelectedEventName();

        if (bookedEventName == null || bookedEventName.isBlank()) {
            bookedEventName = myBookingPage.getFirstBookedEventName();
        }

        testContext.events().setNotedBookedEventName(bookedEventName);
        LogUtils.info("Noted booked event name: " + bookedEventName);
    }

    @And("I confirm booking deletion")
    public void i_confirm_booking_deletion() {
        myBookingPage.confirmBookingDeletion();
    }
}
