package steps;

import pages.*;

import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Dynamic action dispatcher - delegates button clicks AND menu navigation to appropriate page objects.
 * Adding new button/menu = add to page class, not here.
 */
public class ButtonDispatcher {

    private static final Map<String, Runnable> MENU_ACTIONS = Map.of(
            "events", () -> {
                EventPage page = new EventPage();
                page.sleep(1);
                page.goToEventPage();
            },
            "my bookings", () -> new MyBookingPage().goToMyBookingPage()
    );

    /**
     * Click a button by name. Delegates to the appropriate page based on button name.
     * @param buttonName The button name (case-insensitive)
     * @throws IllegalArgumentException if button not found in any page
     */
    public static void clickButton(String buttonName) {
        String normalized = buttonName.trim().toLowerCase(Locale.ROOT);

        // Check LoginPage buttons
        Map<String, Consumer<LoginPage>> loginButtons = LoginPage.getButtonActions();
        if (loginButtons.containsKey(normalized)) {
            LoginPage page = new LoginPage();
            loginButtons.get(normalized).accept(page);
            if (normalized.equals("sign in") || normalized.equals("login")) {
                page.sleep(0.3);
            }
            return;
        }

        // Check RegisterPage buttons
        Map<String, Consumer<RegisterPage>> registerButtons = RegisterPage.getButtonActions();
        if (registerButtons.containsKey(normalized)) {
            registerButtons.get(normalized).accept(new RegisterPage());
            return;
        }

        // Check EventPage buttons
        Map<String, Consumer<EventPage>> eventButtons = EventPage.getButtonActions();
        if (eventButtons.containsKey(normalized)) {
            eventButtons.get(normalized).accept(new EventPage());
            return;
        }

        // Check MyBookingPage buttons
        Map<String, Consumer<MyBookingPage>> bookingButtons = MyBookingPage.getButtonActions();
        if (bookingButtons.containsKey(normalized)) {
            bookingButtons.get(normalized).accept(new MyBookingPage());
            return;
        }

        throw new IllegalArgumentException("Unsupported button in dispatcher: " + buttonName);
    }

    /**
     * Navigate to a menu by name. Delegates to the appropriate page.
     * @param menuName The menu name (case-insensitive)
     * @throws IllegalArgumentException if menu not found
     */
    public static void navigateToMenu(String menuName) {
        String normalized = menuName.trim().toLowerCase(Locale.ROOT);
        Runnable action = MENU_ACTIONS.get(normalized);

        if (action == null) {
            throw new IllegalArgumentException("Unsupported menu in dispatcher: " + menuName);
        }

        action.run();
    }
}
