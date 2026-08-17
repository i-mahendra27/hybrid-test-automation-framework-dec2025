package steps.ui;

import hooks.TestContext;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pages.EventPage;
import utils.LogUtils;

import java.util.List;

public class StepsSearchEvent {
    private final TestContext testContext;
    private final EventPage eventPage;

    public StepsSearchEvent(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    @Then("I should see event related to {string}")
    public void i_should_see_event_related_to(String keyword) {
        for (WebElement event : eventPage.getEvents()) {
            try {
                String actualEventName = event.findElement(By.tagName("h3")).getText();

                if (actualEventName.equalsIgnoreCase(keyword)) {
                    eventPage.verifyEquals(actualEventName, keyword);
                    return;
                }
            } catch (StaleElementReferenceException e) {
                // Re-fetch event list when DOM refreshes during iteration
                LogUtils.warn("Stale element detected, re-fetching event list...");
                event = eventPage.getEvents()
                        .stream()
                        .filter(el -> {
                            try { return el.findElement(By.tagName("h3")).getText().equalsIgnoreCase(keyword); }
                            catch (StaleElementReferenceException ex) { return false; }
                            catch (Exception ex) { return false; }
                        })
                        .findFirst()
                        .orElseThrow(() -> new AssertionError("Event '" + keyword + "' not found after search"));
                eventPage.verifyEquals(actualEventName(event), keyword);
                return;
            }
        }
    }

    private String actualEventName(WebElement event) {
        return event.findElement(By.tagName("h3")).getText();
    }
}
