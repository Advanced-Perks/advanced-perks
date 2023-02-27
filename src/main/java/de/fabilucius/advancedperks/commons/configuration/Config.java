package de.fabilucius.advancedperks.commons.configuration;

import de.fabilucius.advancedperks.commons.configuration.exception.ConfigOperationException;
import de.fabilucius.advancedperks.commons.configuration.value.Value;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.Optional;

public interface Config {

    Map<String, Value<?>> getValues();

    Optional<Value<?>> getValue(String configurationKey);

    Value<?> getValueOrDefault(String configurationKey, Value<?> defaultValue);

    YamlConfiguration getConfig();

    File getFile();

    void saveConfig() throws ConfigOperationException;

    void loadConfig() throws ConfigOperationException;

}
