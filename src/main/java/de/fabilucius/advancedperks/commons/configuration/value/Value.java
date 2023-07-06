package de.fabilucius.advancedperks.commons.configuration.value;

import de.fabilucius.advancedperks.commons.configuration.Config;

public interface Value<T> {

    Config getConfig();

    String getConfigurationKey();

    String getDescription();

    Class<T> getTypeClass();

}
