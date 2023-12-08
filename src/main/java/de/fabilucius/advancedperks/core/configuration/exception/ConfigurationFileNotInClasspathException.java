package de.fabilucius.advancedperks.core.configuration.exception;

public class ConfigurationFileNotInClasspathException extends ConfigurationException {

    public ConfigurationFileNotInClasspathException(String message) {
        super("The configuration file %s wasn't found on the classpath which could indicate that its missing from the plugins jar file."
                .formatted(message));
    }

}
