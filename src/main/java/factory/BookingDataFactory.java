package factory;

import builders.BookingBuilder;
import pages.dto.EventBookDetailDataObject;

/**
 * Factory for creating booking test data.
 * Uses BookingBuilder for fluent test data generation.
 */
public class BookingDataFactory {

    /**
     * Create booking with specific ticket count
     * Business rule: 1 ticket = eligible for refund, >1 = non-eligible
     */
    public static EventBookDetailDataObject createBooking(int tickets) {
        return BookingBuilder.create()
                .withTickets(tickets)
                .withRandomFullName()
                .withRandomEmail()
                .withValidPhone()
                .build();
    }

    /**
     * Create eligible booking (1 ticket)
     */
    public static EventBookDetailDataObject createEligibleBooking() {
        return createBooking(1);
    }

    /**
     * Create non-eligible booking (>1 tickets)
     */
    public static EventBookDetailDataObject createNonEligibleBooking() {
        return createBooking(2);
    }

    /**
     * Create booking with random tickets (1-10)
     */
    public static EventBookDetailDataObject createRandomBooking() {
        int randomTickets = helpers.DataFakerHelper.getFaker().number().numberBetween(1, 10);
        return createBooking(randomTickets);
    }
}
