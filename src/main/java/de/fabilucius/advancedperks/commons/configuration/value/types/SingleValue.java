package de.fabilucius.advancedperks.commons.configuration.value.types;

import de.fabilucius.advancedperks.commons.configuration.Config;
import de.fabilucius.advancedperks.commons.configuration.value.AbstractValue;

import java.util.function.Supplier;

public class SingleValue<T> extends AbstractValue<T> implements Supplier<T> {

    private final T defaultValue;

    public SingleValue(Config config, String configurationKey, String description, Class<T> typeClass, T defaultValue) {
        super(config, configurationKey, description, typeClass);
        this.defaultValue = defaultValue;
    }

    @Override
    public T get() {
        try {
            T value = (T) this.config.getConfig().get(configurationKey);
            return value == null ? this.defaultValue : value;
        } catch (Exception exception) {
            exception.printStackTrace();
            return defaultValue;
        }
    }

    /* The getter and setter of this class */

    public T getDefaultValue() {
        return defaultValue;
    }
}
