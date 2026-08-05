package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import pages.MyBookingPage;

/**
 * Booking-specific steps. Page-specific actions that belong to MyBookingPage.
 */
public class BookingSteps {

    private final TestContext testContext;
    private final MyBookingPage myBookingPage;

    public BookingSteps(TestContext testContext) {
        this.testContext = testContext;
        this.myBookingPage = new MyBookingPage();
    }

    @And("I cancel the booking")
    public void iCancelTheBooking() {
        testContext.booking().setNotedBookingCardText(myBookingPage.getFirstBookedEventCardText());
        steps.ButtonDispatcher.clickButton("cancel booking");
    }

    @And("I confirm the cancellation")
    public void iConfirmTheCancellation() {
        steps.ButtonDispatcher.clickButton("yes, cancel it");
    }

    @Then("the cancelled booking should no longer appear in My Bookings")
    public void theCancelledBookingShouldNoLongerAppearInMyBookings() {
        String notedBookingCardText = testContext.booking().getNotedBookingCardText();

        if (notedBookingCardText != null) {
            myBookingPage.waitUntilBookedEventCardDisappears(notedBookingCardText);
            myBookingPage.verifyFalse(
                    myBookingPage.isBookedEventCardDisplayed(notedBookingCardText),
                    "Cancelled booking is still displayed."
            );
            return;
        }

        myBookingPage.waitUntilBookingsCleared();
        myBookingPage.verifyTrue(
                myBookingPage.getBookingList().isEmpty(),
                "Bookings are still displayed after clearing all bookings."
        );
    }

    @And("I check eligibility for refund")
    public void iCheckEligibilityForRefund() {
        steps.ButtonDispatcher.clickButton("check eligibility for refund?");
    }

    @Then("the refund section should be displayed")
    public void theRefundSectionShouldBeDisplayed() {
        myBookingPage.waitForRefundSectionDisplayed();
    }
}
