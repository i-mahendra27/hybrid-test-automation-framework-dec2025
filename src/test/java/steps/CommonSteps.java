package steps;

import hooks.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import pages.EventPage;
import pages.LoginPage;
import pages.MyBookingPage;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static helpers.PropertiesHelper.loadAllFiles;

/**
 * Common steps - GENERIC ONLY.
 * No page-specific logic. All page-specific actions delegated to:
 * - ButtonDispatcher (button clicks)
 * - Dedicated step classes (page-specific flows)
 */
public class CommonSteps {

    private final LoginPage loginPage;
    private final MyBookingPage myBookingPage;
    private final Properties properties;

    // Page verification map - delegates to appropriate page
    private static final Map<String, Supplier<By>> PAGE_LOCATORS = Map.of(
            "sign in to eventhub", () -> By.xpath(loadAllFiles().getProperty("LOGIN_PAGE_LABEL")),
            "create your account", () -> By.xpath(loadAllFiles().getProperty("REGISTER_PAGE_LABEL")),
            "upcoming events", () -> By.xpath(loadAllFiles().getProperty("EVENT_PAGE_LABEL")),
            "my bookings", () -> By.xpath(loadAllFiles().getProperty("MY_BOOKING_PAGE_LABEL"))
    );

    public CommonSteps() {
        this.loginPage = new LoginPage();
        this.myBookingPage = new MyBookingPage();
        this.properties = loadAllFiles();
    }

    @Given("I launch the browser")
    public void iLaunchTheBrowser() {
        loginPage.verifyTrue(true, "WebDriver is not initialized. Please check CucumberHooks @Before setup.");
    }

    @When("I navigate to url {string}")
    public void iNavigateToUrl(String url) {
        loginPage.openURL(url);
    }

    @Then("I verify that {string} is visible successfully")
    public void iVerifyThatPageIsVisibleSuccessfully(String pageTitle) {
        String normalizedTitle = pageTitle.trim().toLowerCase(Locale.ROOT);
        Supplier<By> locatorSupplier = PAGE_LOCATORS.get(normalizedTitle);

        if (locatorSupplier == null) {
            throw new IllegalArgumentException("Unsupported page title in generic step: " + pageTitle);
        }

        loginPage.verifyElementVisible(locatorSupplier.get(), pageTitle + " is not visible.");
    }

    @When("I click {string} button")
    public void iClickButton(String buttonName) {
        ButtonDispatcher.clickButton(buttonName);
    }
}
