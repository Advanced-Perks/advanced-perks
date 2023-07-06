package de.fabilucius.advancedperks.commons.configuration.value;

import de.fabilucius.advancedperks.commons.configuration.Config;

public class AbstractValue<T> implements Value<T> {

    protected final Config config;
    protected final String configurationKey;
    protected final String description;
    protected final Class<T> typeClass;

    public AbstractValue(Config config, String configurationKey, String description, Class<T> typeClass) {
        this.config = config;
        this.configurationKey = configurationKey;
        this.description = description;
        this.typeClass = typeClass;
        this.config.getValues().put(configurationKey, this);
    }

    /* The getter and setter of this class */

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public String getConfigurationKey() {
        return configurationKey;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Class<T> getTypeClass() {
        return typeClass;
    }
}
