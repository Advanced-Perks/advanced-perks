package de.fabilucius.advancedperks.commons.configuration;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import de.fabilucius.advancedperks.commons.configuration.exception.ConfigInitializationException;
import de.fabilucius.advancedperks.commons.configuration.exception.ConfigOperationException;
import de.fabilucius.advancedperks.commons.configuration.value.Value;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractConfig implements Config {

    private final YamlConfiguration config = new YamlConfiguration();
    private final Map<String, Value<?>> values = Maps.newHashMap();
    private final File file;

    public AbstractConfig(@NotNull Plugin plugin, @NotNull String fileName) {
        Preconditions.checkNotNull(plugin, "plugin cannot be null");
        Preconditions.checkState(!Strings.isNullOrEmpty(fileName), "fileName cannot be null or empty");
        this.file = new File(plugin.getDataFolder(), fileName);
        this.extractConfigFile(fileName);
        this.loadConfig();
    }

    private void extractConfigFile(String fileName) throws ConfigInitializationException {
        URL resourceUrl = this.getClass().getClassLoader().getResource(fileName);
        if (resourceUrl == null) {
            throw new ConfigInitializationException(String.format("Resource url is null (the path %s seems to not exist in the jar file).", fileName));
        }
        if (!this.file.exists()) {
            if (this.file.getParentFile().exists() || this.file.getParentFile().mkdirs()) {
                try {
                    Files.copy(resourceUrl.openStream(), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioException) {
                    throw new ConfigInitializationException(String.format("An IO exception was thrown while extracting the config file from the jar: %s", ioException.getMessage()), ioException);
                }
            }
        }
    }

    @Override
    public void saveConfig() throws ConfigOperationException {
        try {
            this.config.save(this.file);
        } catch (IOException ioException) {
            throw new ConfigOperationException(String.format("There was an error while saving the config data to the file %s: %s", this.file.getAbsolutePath(), ioException.getMessage()), ioException);
        }
    }

    @Override
    public void loadConfig() throws ConfigOperationException {
        try {
            this.config.load(this.file);
        } catch (IOException ioException) {
            throw new ConfigOperationException(String.format("There was an I/O exception while loading the data from the file %s to the config %s: %s", this.file.getAbsolutePath(), this.getClass().getName(), ioException.getMessage()), ioException);
        } catch (InvalidConfigurationException invalidConfigurationException) {
            throw new ConfigOperationException(String.format("There was an error while loading the data from the file %s to the config %s, the configuration seems to have an invalid format: %s", this.file.getAbsolutePath(), this.getClass().getName(), invalidConfigurationException.getMessage()), invalidConfigurationException);
        }
    }

    /* The getter and setter of this class */

    @Override
    public Map<String, Value<?>> getValues() {
        return values;
    }

    @Override
    public Optional<Value<?>> getValue(String configurationKey) {
        return this.values.containsKey(configurationKey) ?
                Optional.of(this.values.get(configurationKey)) : Optional.empty();
    }

    @Override
    public Value<?> getValueOrDefault(String configurationKey, Value<?> defaultValue) {
        Optional<Value<?>> value = this.getValue(configurationKey);
        if (value.isPresent()) {
            return value.get();
        }
        this.values.put(configurationKey, defaultValue);
        return defaultValue;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public YamlConfiguration getConfig() {
        return config;
    }
}
