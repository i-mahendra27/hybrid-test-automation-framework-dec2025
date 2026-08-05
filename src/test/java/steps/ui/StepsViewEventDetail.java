package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.EventPage;

import java.util.List;

public class StepsViewEventDetail {
    private TestContext testContext;
    private EventPage eventPage;

    public StepsViewEventDetail(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    public StepsViewEventDetail() {
        this(new TestContext());
    }

    @When("I click on any {string} card")
    public void iClickOnAnyEventCard(String eventName) {
        List<WebElement> eventList = eventPage.getEvents();
        for (WebElement event : eventList) {
            String actualEventName = event.findElement(By.tagName("h3")).getText();

            if (actualEventName.equalsIgnoreCase(eventName)) {
                event.click();
                return;
            }
        }
    }

    @Then("I should be redirected to event detail page")
    public void iShouldBeRedirectedToEventDetailPage() {
        eventPage.isEventInformationDisplayed();
    }

    @And("I should see complete event information")
    public void iShouldSeeCompleteEventInformation() {
        eventPage.isEventDetailDisplayed();
    }
}
