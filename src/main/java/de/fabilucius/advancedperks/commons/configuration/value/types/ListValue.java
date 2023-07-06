package de.fabilucius.advancedperks.commons.configuration.value.types;

import de.fabilucius.advancedperks.commons.configuration.Config;
import de.fabilucius.advancedperks.commons.configuration.value.AbstractValue;

import java.util.List;
import java.util.function.Supplier;

public class ListValue<T> extends AbstractValue<T> implements Supplier<List<T>> {

    private final List<T> defaultValue;

    public ListValue(Config config, String configurationKey, String description, Class<T> typeClass, List<T> defaultValue) {
        super(config, configurationKey, description, typeClass);
        this.defaultValue = defaultValue;
    }

    @Override
    public List<T> get() {
        try {
            List<T> value = (List<T>) this.config.getConfig().getList(this.configurationKey);
            return value == null ? this.defaultValue : value;
        } catch (Exception classCastException) {
            return defaultValue;
        }
    }

    /* The getter and setter of this class */

    public List<T> getDefaultValue() {
        return defaultValue;
    }
}
