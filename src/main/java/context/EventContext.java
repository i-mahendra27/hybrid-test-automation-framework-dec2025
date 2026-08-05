package context;

/**
 * Domain-specific context for event operations.
 * Thread-safe for parallel execution.
 */
public class EventContext {

    private static final ThreadLocal<String> selectedEventName = new ThreadLocal<>();
    private static final ThreadLocal<Integer> selectedEventPrice = new ThreadLocal<>();
    private static final ThreadLocal<Integer> selectedEventAvailableSeats = new ThreadLocal<>();
    private static final ThreadLocal<String> notedBookedEventName = new ThreadLocal<>();

    // Set selected event name
    public void setSelectedEventName(String eventName) {
        selectedEventName.set(eventName);
    }

    // Get selected event name
    public String getSelectedEventName() {
        return selectedEventName.get();
    }

    // Set selected event price
    public void setSelectedEventPrice(Integer price) {
        selectedEventPrice.set(price);
    }

    // Get selected event price
    public Integer getSelectedEventPrice() {
        return selectedEventPrice.get();
    }

    // Set available seats for selected event
    public void setSelectedEventAvailableSeats(Integer seats) {
        selectedEventAvailableSeats.set(seats);
    }

    // Get available seats for selected event
    public Integer getSelectedEventAvailableSeats() {
        return selectedEventAvailableSeats.get();
    }

    // Set noted booked event name (for verification)
    public void setNotedBookedEventName(String eventName) {
        notedBookedEventName.set(eventName);
    }

    // Get noted booked event name
    public String getNotedBookedEventName() {
        return notedBookedEventName.get();
    }

    // Clear all event context
    public void clear() {
        selectedEventName.remove();
        selectedEventPrice.remove();
        selectedEventAvailableSeats.remove();
        notedBookedEventName.remove();
    }

    // Check if has selected event
    public boolean hasSelectedEvent() {
        return selectedEventName.get() != null && !selectedEventName.get().isBlank();
    }

    // Check if has event price
    public boolean hasEventPrice() {
        return selectedEventPrice.get() != null;
    }

    // Store selected event data as a record
    public void setSelectedEvent(String name, Integer price, Integer availableSeats) {
        setSelectedEventName(name);
        setSelectedEventPrice(price);
        setSelectedEventAvailableSeats(availableSeats);
    }
}
