package exceptions;

public class InvalidPathForAllureReportFileException extends RuntimeException {
    public InvalidPathForAllureReportFileException(String message) {
        super(message);
    }

    public InvalidPathForAllureReportFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
