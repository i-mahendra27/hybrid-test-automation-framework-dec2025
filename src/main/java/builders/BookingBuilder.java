package builders;

import pages.dto.EventBookDetailDataObject;

/**
 * Fluent builder for EventBookDetailDataObject.
 * Supports method chaining and partial builds.
 */
public class BookingBuilder extends BaseBuilder<EventBookDetailDataObject> {

    public static final String TICKETS = "numOfTickets";
    public static final String FULL_NAME = "fullName";
    public static final String EMAIL = "email";
    public static final String PHONE = "phoneNumber";

    // Static Factory Methods

    public static BookingBuilder create() {
        return new BookingBuilder();
    }

    public static BookingBuilder valid() {
        return create()
                .withValidTickets()
                .withRandomFullName()
                .withRandomEmail()
                .withValidPhone();
    }

    public static BookingBuilder eligible() {
        // Business rule: 1 ticket = eligible for refund
        return create()
                .withTickets(1)
                .withRandomFullName()
                .withRandomEmail()
                .withValidPhone();
    }

    public static BookingBuilder nonEligible() {
        // Business rule: >1 tickets = non-eligible for refund
        return create()
                .withTickets(2)
                .withRandomFullName()
                .withRandomEmail()
                .withValidPhone();
    }

    public static BookingBuilder of(int tickets) {
        return create()
                .withTickets(tickets)
                .withRandomFullName()
                .withRandomEmail()
                .withValidPhone();
    }

    // Fluent Setters

    public BookingBuilder withTickets(int numOfTickets) {
        return (BookingBuilder) put(TICKETS, numOfTickets);
    }

    public BookingBuilder withFullName(String fullName) {
        return (BookingBuilder) put(FULL_NAME, fullName);
    }

    public BookingBuilder withEmail(String email) {
        return (BookingBuilder) put(EMAIL, email);
    }

    public BookingBuilder withPhone(String phoneNumber) {
        return (BookingBuilder) put(PHONE, phoneNumber);
    }

    //  Predefined Values

    public BookingBuilder withValidTickets() {
        return withTickets(2);
    }

    public BookingBuilder withRandomTickets(int max) {
        int tickets = helpers.DataFakerHelper.getFaker().number().numberBetween(1, max);
        return withTickets(tickets);
    }

    public BookingBuilder withRandomFullName() {
        return withFullName(helpers.DataFakerHelper.getFaker().name().fullName());
    }

    public BookingBuilder withRandomEmail() {
        return withEmail(helpers.DataFakerHelper.getFaker().internet().emailAddress());
    }

    public BookingBuilder withValidPhone() {
        String phone = "+62" + helpers.DataFakerHelper.getFaker().number().digits(11);
        return withPhone(phone);
    }

    public BookingBuilder withEmptyFullName() {
        return withFullName("");
    }

    public BookingBuilder withEmptyEmail() {
        return withEmail("");
    }

    public BookingBuilder withInvalidEmail() {
        return withEmail("invalid-email");
    }

    public BookingBuilder withInvalidPhone() {
        return withPhone("abc123");
    }

    public BookingBuilder withZeroTickets() {
        return withTickets(0);
    }

    public BookingBuilder withNegativeTickets() {
        return withTickets(-1);
    }

    public BookingBuilder withTooManyTickets() {
        return withTickets(100);
    }

    // Edge Cases

    public BookingBuilder withMinTickets() {
        return withTickets(1);
    }

    public BookingBuilder withMaxTickets() {
        return withTickets(10);
    }

    public BookingBuilder withSpecialCharName() {
        return withFullName("John <Script>Doe");
    }

    public BookingBuilder withUnicodeEmail() {
        return withEmail("user@中文.com");
    }

    public BookingBuilder withLongPhone() {
        return withPhone("+62" + helpers.DataFakerHelper.getFaker().number().digits(20));
    }

    // Build

    @Override
    public EventBookDetailDataObject build() {
        return EventBookDetailDataObject.builder()
                .numOfTickets(has(TICKETS) ? (int) get(TICKETS) : 1)
                .fullName(get(FULL_NAME))
                .email(get(EMAIL))
                .phoneNumber(get(PHONE))
                .build();
    }

    @Override
    protected BaseBuilder<EventBookDetailDataObject> self() {
        return this;
    }

    // Convenience Methods

    // Build as Map for API requests
    public java.util.Map<String, Object> buildAsMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        if (has(TICKETS)) map.put("numOfTickets", get(TICKETS));
        if (has(FULL_NAME)) map.put("fullName", get(FULL_NAME));
        if (has(EMAIL)) map.put("email", get(EMAIL));
        if (has(PHONE)) map.put("phoneNumber", get(PHONE));
        return map;
    }

    // Build as JSON string
    public String buildAsJson() {
        return toJson();
    }
}