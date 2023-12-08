package de.fabilucius.advancedperks.core.configuration.exception;

import de.fabilucius.advancedperks.exception.AdvancedPerksException;

public class ConfigurationException extends AdvancedPerksException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
