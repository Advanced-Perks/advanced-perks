package de.fabilucius.advancedperks.commons.configuration;

import de.fabilucius.advancedperks.AdvancedPerks;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {

    private static final String FILE_EXTENSION = ".yml";
    private static final Logger LOGGER = Bukkit.getLogger();

    private final String identifier;
    private final File file;
    private final FileConfiguration config = new YamlConfiguration();

    public Configuration(String identifier) {
        this.identifier = identifier;
        this.file = new File(AdvancedPerks.getInstance().getDataFolder(), this.getIdentifier() + FILE_EXTENSION);

        try {
            if (!this.getFile().exists()) {
                this.getFile().getParentFile().mkdirs();
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(this.getIdentifier() + FILE_EXTENSION);
                if (inputStream == null) {
                    throw new IllegalStateException(String.format("Cannot create configuration %s, the embedded " +
                            "file for this configuration doesn't seem to exist inside the jar file.", this.getIdentifier()));
                }
                Files.copy(inputStream, this.getFile().toPath());
            }
            this.getConfig().load(this.getFile());
        } catch (
                Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("An error occurred " +
                    "while copying and loading the configuration %s:", this.getIdentifier()), exception);
        }

    }

    public final <T> T getValueWithDefault(String configKey, T defaultValue, Class<T> type) {
        T value = this.getValue(configKey, type);
        if (value != null) {
            return value;
        }
        LOGGER.log(Level.WARNING, String.format("The plugin tried to return a value from the configuration " +
                "%s with the key %s, but theres no value inside the config that is associated with that config " +
                "key, the plugin will resort to a chosen default value. The plugin expects a value from the type " +
                "%s for that config key.", this.getIdentifier(), configKey, type.getSimpleName()));
        return defaultValue;
    }

    public final <T> T getValue(String configKey, Class<T> type) {
        try {
            return type.cast(this.getConfig().get(configKey));
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("An error occurred while returning" +
                    " a value with the config key %s from the configuration %s:", configKey, this.getIdentifier()), exception);
        }
        return null;
    }

    public final <T> void setValueWithAutoSave(String configKey, T value) {
        this.setValue(configKey, value);
        this.saveConfig();
    }

    public final <T> void setValue(String configKey, T value) {
        this.getConfig().set(configKey, value);
    }

    public final void saveConfig() {
        try {
            this.getConfig().save(this.getFile());
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("An error occurred while saving the " +
                    "content of the configuration %s to its file:", this.getIdentifier()), exception);
        }
    }

    /* the getter and setter of this class */

    public String getIdentifier() {
        return identifier;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
