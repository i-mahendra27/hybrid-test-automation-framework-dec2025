package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.And;
import pages.EventPage;

public class StepsViewEventsPage {
    private final TestContext testContext;
    private final EventPage eventPage;

    public StepsViewEventsPage(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    @And("I should see list of events")
    public void i_should_see_list_of_events() {
        eventPage.listOfEvents();
    }
}
