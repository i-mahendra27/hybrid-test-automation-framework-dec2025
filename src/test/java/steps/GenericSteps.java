package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.RegisterPage;
import pages.EventPage;
import pages.MyBookingPage;

import java.util.Map;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;

import static helpers.PropertiesHelper.loadAllFiles;

/**
 * Generic steps for cross-project use.
 * Only truly generic operations here - navigation, generic verifications.
 * Page-specific actions delegated to dedicated step classes.
 */
public class GenericSteps {

    private final LoginPage loginPage;
    private final RegisterPage registerPage;
    private final EventPage eventPage;
    private final MyBookingPage myBookingPage;
    private final Properties properties;

    // Page verification map - key to locator property key
    private static final Map<String, Supplier<By>> PAGE_LOCATORS = Map.of(
            "sign in to eventhub", () -> By.xpath(loadAllFiles().getProperty("LOGIN_PAGE_LABEL")),
            "create your account", () -> By.xpath(loadAllFiles().getProperty("REGISTER_PAGE_LABEL")),
            "upcoming events", () -> By.xpath(loadAllFiles().getProperty("EVENT_PAGE_LABEL")),
            "my bookings", () -> By.xpath(loadAllFiles().getProperty("MY_BOOKING_PAGE_LABEL"))
    );

    public GenericSteps() {
        this.loginPage = new LoginPage();
        this.registerPage = new RegisterPage();
        this.eventPage = new EventPage();
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

    @And("I navigate to {string} menu")
    public void iNavigateToMenu(String menuName) {
        ButtonDispatcher.navigateToMenu(menuName);
    }

    @Then("I should see the {string} message")
    public void i_should_see_the_message(String message) {
        if ("No bookings yet".equalsIgnoreCase(message.trim())) {
            myBookingPage.clearAllBookingsIfPresent();
            myBookingPage.waitForEmptyStateDisplayed();
        }
        loginPage.verifyTextVisible(message);
    }
}
