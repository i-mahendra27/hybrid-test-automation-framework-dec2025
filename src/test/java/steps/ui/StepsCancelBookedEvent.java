package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.And;
import pages.MyBookingPage;

public class StepsCancelBookedEvent {
    private final TestContext testContext;
    private final MyBookingPage myBookingPage;

    public StepsCancelBookedEvent(TestContext testContext) {
        this.testContext = testContext;
        this.myBookingPage = new MyBookingPage();
    }

    public StepsCancelBookedEvent() {
        this(new TestContext());
    }

    @And("I confirm booking cancellation")
    public void i_confirm_booking_cancellation() {
        myBookingPage.confirmBookingDeletion();
    }
}