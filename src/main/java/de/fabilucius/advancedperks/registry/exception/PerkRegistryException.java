package de.fabilucius.advancedperks.registry.exception;

import de.fabilucius.advancedperks.exception.AdvancedPerksException;

public class PerkRegistryException extends AdvancedPerksException {
    public PerkRegistryException(String message) {
        super(message);
    }

    public PerkRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

}
