package hooks;

import context.AuthContext;
import context.BookingContext;
import context.EventContext;
import context.ScenarioContext;
import factory.DriverManager;
import helpers.ScreenRecorderHelper;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.RegisterPage;

/**
 * Test context that delegates to focused domain contexts.
 *
 * Architecture:
 * - TestContext (main entry point) - delegates to focused contexts
 *   ├── ScenarioContext (generic key-value storage)
 *   ├── BookingContext (booking-specific data)
 *   ├── EventContext (event-specific data)
 *   └── AuthContext (auth-specific credentials/register data)
 */
public class TestContext {

    // Focused contexts (thread-safe)
    private static final BookingContext bookingContext = new BookingContext();
    private static final EventContext eventContext = new EventContext();
    private static final ScenarioContext scenarioContext = new ScenarioContext();
    private static final AuthContext authContext = new AuthContext();

    // Non-thread-safe page instances (per scenario)
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private ScreenRecorderHelper screenRecorder;

    public TestContext() {
    }

    //  WebDriver

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    // Page Objects

    public LoginPage getLoginPage() {
        return (loginPage == null) ? loginPage = new LoginPage() : loginPage;
    }

    public RegisterPage getRegisterPage() {
        return (registerPage == null) ? registerPage = new RegisterPage() : registerPage;
    }

    // Screen Recorder

    public ScreenRecorderHelper getScreenRecorder() {
        return screenRecorder;
    }

    public void setScreenRecorder(ScreenRecorderHelper recorder) {
        this.screenRecorder = recorder;
    }

    // Focused Contexts

    /**
     * Booking-specific context
     */
    public BookingContext booking() {
        return bookingContext;
    }

    /**
     * Event-specific context
     */
    public EventContext events() {
        return eventContext;
    }

    /**
     * Auth-specific context (credentials, register data)
     */
    public AuthContext auth() {
        return authContext;
    }

    /**
     * Generic scenario context for custom data
     */
    public ScenarioContext scenario() {
        return scenarioContext;
    }

    // ===================== Reset =====================

    /**
     * Reset all thread-local contexts
     */
    public static void reset() {
        bookingContext.clear();
        eventContext.clear();
        scenarioContext.clear();
        authContext.clear();
    }
}
