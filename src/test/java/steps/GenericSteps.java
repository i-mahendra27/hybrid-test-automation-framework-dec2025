package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import pages.LoginPage;
import pages.RegisterPage;
import pages.EventPage;
import pages.MyBookingPage;

import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;

import static helpers.PropertiesHelper.loadAllFiles;

/**
 * Generic steps for cross-project use.
 * Only truly generic operations here - navigation, generic verifications.
 * Page-specific actions delegated to dedicated step classes.
 *
 * NOTE: Common/generic steps (I launch browser, navigate, verify visibility, click button)
 * are defined in CommonSteps.
 */
public class GenericSteps {

    private final RegisterPage registerPage;
    private final EventPage eventPage;
    private final MyBookingPage myBookingPage;
    private final Properties properties;

    public GenericSteps() {
        this.registerPage = new RegisterPage();
        this.eventPage = new EventPage();
        this.myBookingPage = new MyBookingPage();
        this.properties = loadAllFiles();
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
        LoginPage loginPage = new LoginPage();
        loginPage.verifyTextVisible(message);
    }
}
