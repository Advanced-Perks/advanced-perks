package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationFileNotInClasspathException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationOperationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import de.fabilucius.advancedperks.core.logging.APLogger;
import jakarta.inject.Named;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Configuration extends YamlConfiguration {

    @Inject
    @Named("configurationDirectory")
    private File configDir;

    @Inject
    private APLogger logger;

    /* This field hold the path to the configuration inside the plugin's jar and the path inside the plugins folder */
    private final String configurationPath;
    private final YamlConfiguration defaultsConfiguration = new YamlConfiguration();
    private boolean loaded = false;

    public Configuration() {
        FilePathInJar filePathInJar = this.getClass().getAnnotation(FilePathInJar.class);
        if (filePathInJar == null) {
            throw new IllegalArgumentException("The configuration %s doesn't have a @FilePathInJar annotation present.");
        }
        this.configurationPath = filePathInJar.value();
    }

    @Nullable
    @Override
    public Object get(@NotNull String path, @Nullable Object def) {
        Object result = super.get(path, null);
        if (!this.loaded) {
            return def;
        }
        if (result == null) {
            Object defaults = this.defaultsConfiguration.get(path, def);
            this.logger.info("The key %s from the configuration %s wasn't found in the file in the plugin directory the plugin will set its default value in the configuration.".formatted(path, this.getClass().getName()));
            this.set(path, defaults);
            try {
                this.saveConfiguration();
            } catch (ConfigurationOperationException e) {
                this.logger.log(Level.SEVERE, "Unable to save the configuration after adding a missing key with defaults from inside the plugin.", e);
            }
            return defaults;
        }
        return result;
    }

    public void extractConfigurationFileFromJar() throws ConfigurationFileNotInClasspathException, ConfigurationInitializationException {
        File file = new File(this.configDir, this.configurationPath);
        URL configurationResourceUrl = this.getClass().getClassLoader().getResource(this.configurationPath);
        if (configurationResourceUrl == null) {
            throw new ConfigurationFileNotInClasspathException(this.configurationPath);
        }
        try (InputStream inputStream = configurationResourceUrl.openStream()) {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            this.defaultsConfiguration.load(new InputStreamReader(inputStream));
        } catch (IOException exception) {
            throw new ConfigurationInitializationException("An io exception was thrown during the configuration extraction process of %s.".formatted(this.getClass().getName()), exception);
        } catch (InvalidConfigurationException exception) {
            throw new ConfigurationInitializationException("An exception was thrown during the defaults configuration loading process of %s.".formatted(this.getClass().getName()), exception);
        }
    }

    public void loadConfigurationFile() throws ConfigurationInitializationException {
        File file = new File(this.configDir, this.configurationPath);
        try {
            this.load(file);
            this.loaded = true;
        } catch (IOException ioException) {
            throw new ConfigurationInitializationException("There was an io exception while loading the data from the file %s to the config %s.".formatted(file.getAbsolutePath(), this.getClass().getName()), ioException);
        } catch (InvalidConfigurationException invalidConfigurationException) {
            throw new ConfigurationInitializationException("There was an error while loading the data from the file %s to the config %s, the configuration seems to have an invalid format.".formatted(file.getAbsolutePath(), this.getClass().getName()), invalidConfigurationException);
        }
    }

    public void saveConfiguration() throws ConfigurationOperationException {
        try {
            File file = new File(this.configDir, this.configurationPath);
            this.save(file);
        } catch (IOException exception) {
            throw new ConfigurationOperationException("There was an io exception while saving the configuration data of the configuration %s from memory to its file.".formatted(this.getClass().getName()), exception);
        }
    }

    public File getFile() {
        return new File(this.configDir, this.configurationPath);
    }

}
