package de.fabilucius.advancedperks.exception;

public class AdvancedPerksRuntimeException extends RuntimeException {

    public AdvancedPerksRuntimeException(String message) {
        super(message);
    }

    public AdvancedPerksRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdvancedPerksRuntimeException(Throwable cause) {
        super(cause);
    }

    public AdvancedPerksRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
