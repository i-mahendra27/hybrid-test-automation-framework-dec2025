package utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private DateUtils() {
        super();
    }

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Get current date/time in default format: dd/MM/yyyy HH:mm:ss
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

    /**
     * Get current date/time formatted for filenames: yyyyMMdd_HHmmss
     */
    public static String getTimestampForFile() {
        return LocalDateTime.now().format(FILE_FORMATTER);
    }

    /**
     * Get current date only: yyyyMMdd
     */
    public static String getCurrentDateOnly() {
        return LocalDateTime.now().format(DATE_ONLY_FORMATTER);
    }

    /**
     * Get current time only: HHmmss
     */
    public static String getCurrentTimeOnly() {
        return LocalDateTime.now().format(TIME_ONLY_FORMATTER);
    }

    /**
     * Get current date/time in ISO format
     */
    public static String getCurrentDateTimeISO() {
        return LocalDateTime.now().format(ISO_FORMATTER);
    }

    /**
     * Get epoch milliseconds for current time
     */
    public static long getCurrentEpochMillis() {
        return Instant.now().toEpochMilli();
    }

    /**
     * Get epoch seconds for current time
     */
    public static long getCurrentEpochSeconds() {
        return Instant.now().getEpochSecond();
    }

    /**
     * Custom format with specified separator character
     */
    public static String getCurrentDateTimeCustom(String separator) {
        if (separator == null || separator.isBlank()) {
            LogUtils.warn("Invalid separator provided, using default format");
            return getCurrentDateTime();
        }
        String formatted = LocalDateTime.now().format(DEFAULT_FORMATTER);
        return formatted
                .replace("/", separator)
                .replace(" ", separator)
                .replace(":", separator);
    }

    /**
     * Format Date object with custom pattern
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            LogUtils.warn("Date is null, returning empty string");
            return "";
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            return ldt.format(formatter);
        } catch (Exception e) {
            LogUtils.error("Failed to format date with pattern: " + pattern);
            return "";
        }
    }

    /**
     * Convert epoch milliseconds to formatted date string
     */
    public static String epochMillisToDateTime(long epochMillis, String pattern) {
        try {
            Instant instant = Instant.ofEpochMilli(epochMillis);
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return ldt.format(formatter);
        } catch (Exception e) {
            LogUtils.error("Failed to convert epoch millis: " + epochMillis);
            return "";
        }
    }

    /**
     * Parse date string to epoch milliseconds
     */
    public static long dateTimeToEpochMillis(String dateTimeStr, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime ldt = LocalDateTime.parse(dateTimeStr, formatter);
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        } catch (Exception e) {
            LogUtils.error("Failed to parse date: " + dateTimeStr);
            return 0;
        }
    }

    /**
     * Add days to current date and return formatted string
     */
    public static String getDateWithOffset(int daysOffset, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime result = LocalDateTime.now().plusDays(daysOffset);
            return result.format(formatter);
        } catch (Exception e) {
            LogUtils.error("Failed to calculate date with offset: " + daysOffset);
            return "";
        }
    }

    /**
     * Get date relative to today (negative for past, positive for future)
     */
    public static String getRelativeDate(int daysOffset) {
        LocalDateTime result = LocalDateTime.now().plusDays(daysOffset);
        return result.format(DEFAULT_FORMATTER);
    }
}
