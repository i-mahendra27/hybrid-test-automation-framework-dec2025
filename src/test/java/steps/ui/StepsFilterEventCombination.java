package steps.ui;

import factory.DriverManager;
import hooks.TestContext;
import io.cucumber.java.en.Then;
import managers.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.EventPage;

import java.time.Duration;
import java.util.List;

public class StepsFilterEventCombination {
    private final TestContext testContext;
    private final EventPage eventPage;

    public StepsFilterEventCombination(TestContext testContext) {
        this.testContext = testContext;
        this.eventPage = new EventPage();
    }

    @Then("I should see {string} events in {string}")
    public void i_should_see_events_in(String expectedCategory, String expectedCity) {
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(ConfigManager.getExplicitWaitTimeout()))
                .ignoring(StaleElementReferenceException.class)
                .until(driver -> {
                    List<WebElement> eventList = eventPage.getEvents();

                    for (WebElement event : eventList) {
                        String actualCategory = event.findElement(By.cssSelector("span.inline-flex")).getText().trim();
                        String actualLocation = event.findElement(By.xpath(".//*[contains(text(),'" + expectedCity + "')]")).getText().trim();

                        if (!actualCategory.equals(expectedCategory) || !actualLocation.contains(expectedCity)) {
                            return false;
                        }
                    }

                    return !eventList.isEmpty();
                });

        List<WebElement> eventList = eventPage.getEvents();

        for (WebElement event : eventList) {
            String actualCategory = event.findElement(By.cssSelector("span.inline-flex")).getText().trim();
            String actualLocation = event.findElement(By.xpath(".//*[contains(text(),'" + expectedCity + "')]")).getText().trim();

            eventPage.verifyEquals(actualCategory, expectedCategory);
            eventPage.verifyTrue(actualLocation.contains(expectedCity),
                    "Expected event location to contain city " + expectedCity + ", but was: " + actualLocation);
        }
    }
}
