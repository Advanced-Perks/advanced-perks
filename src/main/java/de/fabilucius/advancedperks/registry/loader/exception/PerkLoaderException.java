package de.fabilucius.advancedperks.registry.loader.exception;

import de.fabilucius.advancedperks.exception.AdvancedPerksException;

public class PerkLoaderException extends AdvancedPerksException {
    public PerkLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public PerkLoaderException(String message) {
        super(message);
    }
}
