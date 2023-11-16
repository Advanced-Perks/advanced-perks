package de.fabilucius.advancedperks.configuration;

import de.fabilucius.advancedperks.configuration.exception.ConfigurationFileNotInClasspathException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationOperationException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration extends YamlConfiguration {

    /* This field hold the path to the configuration inside the plugin's jar and the path inside the plugins folder */
    private final String configurationPath;
    private final File file;

    public Configuration(File dataFolder, String configurationPath) {
        this.configurationPath = configurationPath;
        this.file = new File(dataFolder, configurationPath);
    }

    public void extractConfigurationFileFromJar() throws ConfigurationFileNotInClasspathException, ConfigurationInitializationException {
        URL configurationResourceUrl = this.getClass().getClassLoader().getResource(this.configurationPath);
        if (configurationResourceUrl == null) {
            throw new ConfigurationFileNotInClasspathException(this.configurationPath);
        }
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            try {
                Files.copy(configurationResourceUrl.openStream(), this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new ConfigurationInitializationException("An io exception was thrown during the configuration extraction process of %s.".formatted(this.getClass().getName()), exception);
            }
        }
    }

    public void loadConfigurationFile() throws ConfigurationInitializationException {
        try {
            this.load(this.file);
        } catch (IOException ioException) {
            throw new ConfigurationInitializationException("There was an io exception while loading the data from the file %s to the config %s.".formatted(this.file.getAbsolutePath(), this.getClass().getName()), ioException);
        } catch (InvalidConfigurationException invalidConfigurationException) {
            throw new ConfigurationInitializationException("There was an error while loading the data from the file %s to the config %s, the configuration seems to have an invalid format.".formatted(this.file.getAbsolutePath(), this.getClass().getName()), invalidConfigurationException);
        }
    }

    public void saveConfiguration() throws ConfigurationOperationException {
        try {
            this.save(this.file);
        } catch (IOException exception) {
            throw new ConfigurationOperationException("There was an io exception while saving the configuration data of the configuration %s from memory to its file.".formatted(this.getClass().getName()), exception);
        }
    }

    public File getFile() {
        return file;
    }

}
