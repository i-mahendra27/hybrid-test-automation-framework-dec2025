package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.Then;
import pages.EventPage;

public class StepsEventSearchNotFound {
    private final TestContext testContext;
    private final EventPage eventPage;

    public StepsEventSearchNotFound(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    @Then("I should see {string} search message")
    public void i_should_see_message(String expectedMessage) {
        eventPage.verifyEquals(eventPage.searchNotFound(), expectedMessage);
    }
}
