package api.payloads;

import helpers.DataFakerHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventPayloads {
    private static final String[] CATEGORIES = {
            "Conference",
            "Concert",
            "Sports",
            "Workshop",
            "Festival"
    };

    private EventPayloads() {
        // Utility class
    }

    public static Map<String, Object> getListEventsQueryParams(String category, String city, String search, String page, String limit){
        Map<String, Object> params = new LinkedHashMap<>();
        putIfPresent(params, "category", category);
        putIfPresent(params, "city", city);
        putIfPresent(params, "search", search);
        putIfPresent(params, "page", page);
        putIfPresent(params, "limit", limit);
        return params;
    }

    public static Map<String, Object> createNewEventPayload() {
        LocalDate date = LocalDate.now().plusDays(DataFakerHelper.getRandomNumber(1, 31));

        LocalTime time = LocalTime.of(DataFakerHelper.getRandomNumber(8, 20), 0);

        String eventDate = LocalDateTime.of(date, time).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", DataFakerHelper.getFaker().company().buzzword());
        payload.put("description", DataFakerHelper.getFaker().lorem().sentence());
        payload.put("category", getRandomCategory());
        payload.put("venue", DataFakerHelper.getFaker().company().name() + " Convention Center");
        payload.put("city", DataFakerHelper.getFaker().address().city());
        payload.put("eventDate", eventDate);
        payload.put("price", String.valueOf(DataFakerHelper.getFaker().number().numberBetween(100, 1000)));
        payload.put("totalSeats", DataFakerHelper.getFaker().number().numberBetween(1, 100));
        payload.put("imageUrl", "https://example.com/images/" + DataFakerHelper.getFaker().internet().slug() + ".jpg");
        return payload;
    }

    public static Map<String, Object> createNewEventPayloadWithLimitedSeats() {
        LocalDate date = LocalDate.now().plusDays(DataFakerHelper.getRandomNumber(1, 31));

        LocalTime time = LocalTime.of(DataFakerHelper.getRandomNumber(8, 20), 0);

        String eventDate = LocalDateTime.of(date, time).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", DataFakerHelper.getFaker().company().buzzword());
        payload.put("description", DataFakerHelper.getFaker().lorem().sentence());
        payload.put("category", getRandomCategory());
        payload.put("venue", DataFakerHelper.getFaker().company().name() + " Convention Center");
        payload.put("city", DataFakerHelper.getFaker().address().city());
        payload.put("eventDate", eventDate);
        payload.put("price", String.valueOf(DataFakerHelper.getFaker().number().numberBetween(100, 1000)));
        payload.put("totalSeats", 2);  // Limited seats for insufficient seats test
        payload.put("imageUrl", "https://example.com/images/" + DataFakerHelper.getFaker().internet().slug() + ".jpg");
        return payload;
    }

    public static Map<String, Object> invalidCreateNewEventPayload() {
        Map<String, Object> payload = createNewEventPayload();
        payload.remove("title");
        return payload;
    }

    public static Map<String, Object> updateEventPayload() {
        LocalDate date = LocalDate.now().plusDays(DataFakerHelper.getRandomNumber(1, 31));

        LocalTime time = LocalTime.of(DataFakerHelper.getRandomNumber(8, 20), 0);

        String eventDate = LocalDateTime.of(date, time).atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", DataFakerHelper.getFaker().company().buzzword());
        payload.put("description", DataFakerHelper.getFaker().lorem().sentence());
        payload.put("category", getRandomCategory());
        payload.put("venue", DataFakerHelper.getFaker().company().name() + " International Center");
        payload.put("city", DataFakerHelper.getFaker().address().city());
        payload.put("eventDate", eventDate);
        payload.put("price", String.valueOf(DataFakerHelper.getFaker().number().numberBetween(100, 1000)));
        payload.put("totalSeats", DataFakerHelper.getFaker().number().numberBetween(1, 100));
        payload.put("imageUrl", "https://example.com/images/" + DataFakerHelper.getFaker().internet().slug() + ".jpg");
        return payload;
    }

    public static Map<String, Object> invalidUpdateEventPayload() {
        Map<String, Object> payload = updateEventPayload();
        payload.remove("title");
        return payload;
    }

    // Helper method
    public static void putIfPresent(Map<String, Object> params, String key, String value){
        if (value != null && !value.isBlank()
        && !"-".equals(value.trim())){
            params.put(key, value.trim());
        }
    }

    public static String getRandomCategory(){
        return CATEGORIES[DataFakerHelper.getRandomNumber(0, CATEGORIES.length)];
    }
}
