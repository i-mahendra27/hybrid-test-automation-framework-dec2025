package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    private static final Logger log = LogManager.getLogger(LogUtils.class);

    public static void info(String message){
        String normalized = normalize(message);
        if (normalized != null) {
            log.info(normalized);
        }
    }

    public static void info(Object object){
        info(normalize(object));
    }

    public static void warn(String message){
        String normalized = normalize(message);
        if (normalized != null) {
            log.warn(normalized);
        }
    }

    public static void warn(Object object){
        warn(normalize(object));
    }

    public static void error(String message){
        String normalized = normalize(message);
        if (normalized != null) {
            log.error(normalized);
        }
    }

    public static void error(Object object){
        error(normalize(object));
    }

    public static void fatal(String message){
        String normalized = normalize(message);
        if (normalized != null) {
            log.fatal(normalized);
        }
    }

    public static void fatal(Object object){
        fatal(normalize(object));
    }

    public static void debug(String message){
        String normalized = normalize(message);
        if (normalized != null) {
            log.debug(normalized);
        }
    }

    public static void debug(Object object){
        debug(normalize(object));
    }

    private static String normalize(Object value) {
        if (value == null) {
            return null;
        }

        String message = value.toString();
        if (message.isBlank()) {
            return null;
        }

        return message.strip();
    }
}
