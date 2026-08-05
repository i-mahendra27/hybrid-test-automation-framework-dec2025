package builders;

import pages.dto.CredentialsDataObject;
import pages.dto.EventBookDetailDataObject;
import pages.dto.NewEventDataObject;

/**
 * Main entry point for test data generation with fluent Builder pattern.
 *
 * Provides convenient access to all builders with method chaining support.
 *
 * Usage:
 *   TestData.credentials().valid().build();
 *   TestData.booking().eligible().build();
 *   TestData.event().randomTitle().withPrice(500).build();
 */
public class TestData {

    private TestData() {
        // Utility class - prevent instantiation
    }

    // Credentials

    // Get credentials builder

    public static CredentialsBuilder credentials() {
        return CredentialsBuilder.create();
    }

    // Create valid credentials
    public static CredentialsDataObject validCredentials() {
        return CredentialsBuilder.valid().build();
    }

    // Create invalid credentials
    public static CredentialsDataObject invalidCredentials() {
        return CredentialsBuilder.invalid().build();
    }

    // Booking

    // Get booking builder
    public static BookingBuilder booking() {
        return BookingBuilder.create();
    }

    // Create eligible booking (1 ticket)
    public static EventBookDetailDataObject eligibleBooking() {
        return BookingBuilder.eligible().build();
    }

    // Create non-eligible booking (>1 tickets)
    public static EventBookDetailDataObject nonEligibleBooking() {
        return BookingBuilder.nonEligible().build();
    }

    // Create booking with specific ticket count
    public static EventBookDetailDataObject bookingWithTickets(int tickets) {
        return BookingBuilder.of(tickets).build();
    }

    // Event

    // Get event builder
    public static EventBuilder event() {
        return EventBuilder.create();
    }

    // Create valid event
    public static NewEventDataObject validEvent() {
        return EventBuilder.valid().build();
    }

    // Create event with random title
    public static NewEventDataObject randomEvent() {
        return EventBuilder.create()
                .withRandomTitle()
                .withValidDescription()
                .withValidCategory()
                .withValidCity()
                .withValidVenue()
                .withValidDate()
                .withValidPrice()
                .withValidSeats()
                .withValidImageUrl()
                .build();
    }

    // Variants

    // Create multiple variations
    public static java.util.List<CredentialsDataObject> credentialsVariations() {
        return java.util.Arrays.asList(
                CredentialsBuilder.valid().build(),
                CredentialsBuilder.create().withEmptyEmail().withValidPassword().build(),
                CredentialsBuilder.create().withValidEmail().withEmptyPassword().build(),
                CredentialsBuilder.create().withInvalidEmail().withInvalidPassword().build()
        );
    }

    public static java.util.List<EventBookDetailDataObject> bookingVariations() {
        return java.util.Arrays.asList(
                BookingBuilder.eligible().build(),
                BookingBuilder.nonEligible().build(),
                BookingBuilder.create().withTickets(5).withRandomFullName().withRandomEmail().withValidPhone().build(),
                BookingBuilder.create().withTickets(10).withRandomFullName().withRandomEmail().withValidPhone().build()
        );
    }

    public static java.util.List<NewEventDataObject> eventVariations() {
        return java.util.Arrays.asList(
                EventBuilder.valid().build(),
                EventBuilder.create().withEmptyTitle().withValidDescription().withValidCategory().withValidCity().build(),
                EventBuilder.create().withValidTitle().withEmptyCategory().withValidCity().build(),
                EventBuilder.create().withZeroSeats().withValidTitle().withValidCategory().build()
        );
    }

    // Edge Cases

    // Create edge case data
    public static class EdgeCases {

        private EdgeCases() {}

        public static CredentialsDataObject emptyEmail() {
            return CredentialsBuilder.create().withEmail("").withValidPassword().build();
        }

        public static CredentialsDataObject emptyPassword() {
            return CredentialsBuilder.create().withValidEmail().withPassword("").build();
        }

        public static CredentialsDataObject bothEmpty() {
            return CredentialsBuilder.create().withEmail("").withPassword("").build();
        }

        public static EventBookDetailDataObject zeroTickets() {
            return BookingBuilder.create().withTickets(0).withRandomFullName().withRandomEmail().withValidPhone().build();
        }

        public static EventBookDetailDataObject negativeTickets() {
            return BookingBuilder.create().withTickets(-1).withRandomFullName().withRandomEmail().withValidPhone().build();
        }

        public static EventBookDetailDataObject maxTickets() {
            return BookingBuilder.create().withTickets(10).withRandomFullName().withRandomEmail().withValidPhone().build();
        }

        public static NewEventDataObject emptyTitle() {
            return EventBuilder.create().withTitle("").withValidCategory().withValidCity().build();
        }

        public static NewEventDataObject emptyCategory() {
            return EventBuilder.create().withValidTitle().withCategory("").withValidCity().build();
        }

        public static NewEventDataObject zeroSeats() {
            return EventBuilder.create().withValidTitle().withValidCategory().withSeats(0).build();
        }

        public static NewEventDataObject negativeSeats() {
            return EventBuilder.create().withValidTitle().withValidCategory().withSeats(-10).build();
        }

        public static NewEventDataObject freeEvent() {
            return EventBuilder.create()
                    .withValidTitle()
                    .withValidDescription()
                    .withValidCategory()
                    .withValidCity()
                    .withValidVenue()
                    .withValidDate()
                    .withPrice(0)
                    .withValidSeats()
                    .withValidImageUrl()
                    .build();
        }
    }
}
