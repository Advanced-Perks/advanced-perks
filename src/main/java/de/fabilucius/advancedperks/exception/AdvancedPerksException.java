package de.fabilucius.advancedperks.exception;

public abstract class AdvancedPerksException extends Exception {

    public AdvancedPerksException(String message) {
        super(message);
    }

    public AdvancedPerksException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdvancedPerksException(Throwable cause) {
        super(cause);
    }

    public AdvancedPerksException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
