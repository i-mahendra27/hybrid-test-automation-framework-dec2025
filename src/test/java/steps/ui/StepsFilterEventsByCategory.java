package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.EventPage;

import java.util.List;

public class StepsFilterEventsByCategory {
    private final TestContext testContext;
    private final EventPage eventPage;

    public StepsFilterEventsByCategory(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    @Then("I should see only {string} events displayed")
    public void i_should_see_only_events_displayed(String expectedCategory) {
        List<WebElement> eventList = eventPage.getEvents();
        for (WebElement event : eventList) {
            String actualCategory = event.findElement(By.cssSelector("span.inline-flex")).getText().trim();
            eventPage.verifyEquals(actualCategory, expectedCategory);
        }
    }
}
