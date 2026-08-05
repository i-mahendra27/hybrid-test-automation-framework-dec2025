package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.Then;
import pages.MyBookingPage;


public class StepsEligibleRefundBookedEvent {
    private final TestContext testContext;
    private final MyBookingPage myBookingPage;

    public StepsEligibleRefundBookedEvent(TestContext testContext) {
        this.testContext = testContext;
        this.myBookingPage = new MyBookingPage();
    }

    public StepsEligibleRefundBookedEvent() {
        this(new TestContext());
    }


    @Then("I should see refund section")
    public void i_should_see_refund_section() {
        myBookingPage.waitForRefundSectionDisplayed();
    }
}
