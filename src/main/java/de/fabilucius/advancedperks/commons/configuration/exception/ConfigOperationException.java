package de.fabilucius.advancedperks.commons.configuration.exception;

public class ConfigOperationException extends RuntimeException {

    public ConfigOperationException(String message) {
        super(message);
    }

    public ConfigOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
