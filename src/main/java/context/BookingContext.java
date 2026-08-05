package context;

import pages.dto.EventBookDetailDataObject;

/**
 * Domain-specific context for booking operations.
 * Thread-safe for parallel execution.
 */
public class BookingContext {

    private static final ThreadLocal<EventBookDetailDataObject> bookingData = new ThreadLocal<>();
    private static final ThreadLocal<String> notedBookingCardText = new ThreadLocal<>();

    // Set booking data
    public void setBookingData(EventBookDetailDataObject data) {
        bookingData.set(data);
    }

    // Get booking data
    public EventBookDetailDataObject getBookingData() {
        return bookingData.get();
    }

    // Set noted booking card text (for cancellation verification)
    public void setNotedBookingCardText(String cardText) {
        notedBookingCardText.set(cardText);
    }

    // Get noted booking card text
    public String getNotedBookingCardText() {
        return notedBookingCardText.get();
    }

    // Clear all booking context
    public void clear() {
        bookingData.remove();
        notedBookingCardText.remove();
    }

    // Check if has booking data
    public boolean hasBookingData() {
        return bookingData.get() != null;
    }

    // Check if has noted booking text
    public boolean hasNotedBookingCardText() {
        return notedBookingCardText.get() != null && !notedBookingCardText.get().isBlank();
    }
}
