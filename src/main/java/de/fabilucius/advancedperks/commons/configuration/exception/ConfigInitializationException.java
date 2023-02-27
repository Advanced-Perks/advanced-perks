package de.fabilucius.advancedperks.commons.configuration.exception;

public class ConfigInitializationException extends RuntimeException {

    public ConfigInitializationException(String message) {
        super(message);
    }

    public ConfigInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
