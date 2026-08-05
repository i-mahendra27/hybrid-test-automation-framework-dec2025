package builders;

import pages.dto.NewEventDataObject;

/**
 * Fluent builder for NewEventDataObject.
 * Supports method chaining and partial builds.
 */
public class EventBuilder extends BaseBuilder<NewEventDataObject> {

    public static final String TITLE = "eventTitle";
    public static final String DESCRIPTION = "eventDescription";
    public static final String CATEGORY = "eventCategory";
    public static final String CITY = "eventCity";
    public static final String VENUE = "eventVenue";
    public static final String DATE = "eventStartDate";
    public static final String PRICE = "eventPrice";
    public static final String SEATS = "totalSeats";
    public static final String IMAGE_URL = "eventImageURLPath";

    // Static Factory Methods

    public static EventBuilder create() {
        return new EventBuilder();
    }

    public static EventBuilder valid() {
        return create()
                .withValidTitle()
                .withValidDescription()
                .withValidCategory()
                .withValidCity()
                .withValidVenue()
                .withValidDate()
                .withValidPrice()
                .withValidSeats()
                .withValidImageUrl();
    }

    public static EventBuilder minimal() {
        return create()
                .withMinimalTitle()
                .withValidDescription()
                .withValidCategory()
                .withValidCity()
                .withValidVenue()
                .withValidDate()
                .withValidPrice()
                .withValidSeats()
                .withValidImageUrl();
    }

    public static NewEventDataObject of(String title) {
        return create()
                .withTitle(title)
                .withValidCategory()
                .withValidCity()
                .withValidVenue()
                .withValidDate()
                .withValidPrice()
                .withValidSeats()
                .build();
    }

    // Fluent Setters

    public EventBuilder withTitle(String title) {
        return (EventBuilder) put(TITLE, title);
    }

    public EventBuilder withDescription(String description) {
        return (EventBuilder) put(DESCRIPTION, description);
    }

    public EventBuilder withCategory(String category) {
        return (EventBuilder) put(CATEGORY, category);
    }

    public EventBuilder withCity(String city) {
        return (EventBuilder) put(CITY, city);
    }

    public EventBuilder withVenue(String venue) {
        return (EventBuilder) put(VENUE, venue);
    }

    public EventBuilder withDate(String date) {
        return (EventBuilder) put(DATE, date);
    }

    public EventBuilder withPrice(String price) {
        return (EventBuilder) put(PRICE, price);
    }

    public EventBuilder withPrice(int price) {
        return (EventBuilder) put(PRICE, String.valueOf(price));
    }

    public EventBuilder withSeats(int seats) {
        return (EventBuilder) put(SEATS, seats);
    }

    public EventBuilder withImageUrl(String url) {
        return (EventBuilder) put(IMAGE_URL, url);
    }

    // Predefined Values

    public EventBuilder withValidTitle() {
        return withTitle("Amazing Concert 2024");
    }

    public EventBuilder withMinimalTitle() {
        return withTitle("A");
    }

    public EventBuilder withRandomTitle() {
        return withTitle(helpers.DataFakerHelper.getFaker().book().title() + " Event");
    }

    public EventBuilder withLongTitle() {
        return withTitle("A".repeat(200));
    }

    public EventBuilder withValidDescription() {
        return withDescription(helpers.DataFakerHelper.getFaker().lorem().paragraph());
    }

    public EventBuilder withShortDescription() {
        return withDescription("Short desc");
    }

    public EventBuilder withValidCategory() {
        return withCategory("Conference");
    }

    public EventBuilder withMusicCategory() {
        return withCategory("Music");
    }

    public EventBuilder withSportsCategory() {
        return withCategory("Sports");
    }

    public EventBuilder withValidCity() {
        return withCity(helpers.DataFakerHelper.getFaker().address().city());
    }

    public EventBuilder withValidVenue() {
        return withVenue(helpers.DataFakerHelper.getFaker().company().name() + " Hall");
    }

    public EventBuilder withValidDate() {
        return withDate("25 December 2026");
    }

    public EventBuilder withPastDate() {
        return withDate("1 January 2020");
    }

    public EventBuilder withValidPrice() {
        return withPrice(String.valueOf(helpers.DataFakerHelper.getFaker().number().numberBetween(100, 5000)));
    }

    public EventBuilder withFreePrice() {
        return withPrice("0");
    }

    public EventBuilder withNegativePrice() {
        return withPrice("-100");
    }

    public EventBuilder withValidSeats() {
        return withSeats(helpers.DataFakerHelper.getFaker().number().numberBetween(50, 1000));
    }

    public EventBuilder withZeroSeats() {
        return withSeats(0);
    }

    public EventBuilder withNegativeSeats() {
        return withSeats(-10);
    }

    public EventBuilder withValidImageUrl() {
        return withImageUrl("C:\\images\\event.png");
    }

    public EventBuilder withInvalidImageUrl() {
        return withImageUrl("not-a-valid-url");
    }

    // Edge Cases

    public EventBuilder withSpecialCharTitle() {
        return withTitle("Event <Script>Alert()");
    }

    public EventBuilder withUnicodeTitle() {
        return withTitle("Konser Musik Indonesia 2024");
    }

    public EventBuilder withEmojiTitle() {
        return withTitle("🎉 Amazing Event 🎉");
    }

    public EventBuilder withEmptyTitle() {
        return withTitle("");
    }

    public EventBuilder withEmptyCategory() {
        return withCategory("");
    }

    public EventBuilder withEmptyCity() {
        return withCity("");
    }

    public EventBuilder withEmptyVenue() {
        return withVenue("");
    }

    // Build

    @Override
    public NewEventDataObject build() {
        return NewEventDataObject.builder()
                .eventTitle(get(TITLE))
                .eventDescription(get(DESCRIPTION))
                .eventCategory(get(CATEGORY))
                .eventCity(get(CITY))
                .eventVenue(get(VENUE))
                .eventStartDate(get(DATE))
                .eventPrice(get(PRICE))
                .totalSeats(has(SEATS) ? (int) get(SEATS) : 100)
                .eventImageURLPath(get(IMAGE_URL))
                .build();
    }

    @Override
    protected BaseBuilder<NewEventDataObject> self() {
        return this;
    }

    // Convenience Methods

    // Build as Map for API requests
    public java.util.Map<String, Object> buildAsMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        if (has(TITLE)) map.put("eventTitle", get(TITLE));
        if (has(DESCRIPTION)) map.put("eventDescription", get(DESCRIPTION));
        if (has(CATEGORY)) map.put("eventCategory", get(CATEGORY));
        if (has(CITY)) map.put("eventCity", get(CITY));
        if (has(VENUE)) map.put("eventVenue", get(VENUE));
        if (has(DATE)) map.put("eventStartDate", get(DATE));
        if (has(PRICE)) map.put("eventPrice", get(PRICE));
        if (has(SEATS)) map.put("totalSeats", get(SEATS));
        if (has(IMAGE_URL)) map.put("eventImageURLPath", get(IMAGE_URL));
        return map;
    }

    // Build as JSON string
    public String buildAsJson() {
        return toJson();
    }
}
