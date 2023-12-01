package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.annotation.FilePathInJar;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationFileNotInClasspathException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationOperationException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import jakarta.inject.Named;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration extends YamlConfiguration {

    @Inject
    @Named("configurationDirectory")
    private File configDir;

    /* This field hold the path to the configuration inside the plugin's jar and the path inside the plugins folder */
    private final String configurationPath;

    public Configuration() {
        FilePathInJar filePathInJar = this.getClass().getAnnotation(FilePathInJar.class);
        if (filePathInJar == null) {
            throw new IllegalArgumentException("The configuration %s doesn't have a @FilePathInJar annotation present.");
        }
        this.configurationPath = filePathInJar.value();
    }

    public void extractConfigurationFileFromJar() throws ConfigurationFileNotInClasspathException, ConfigurationInitializationException {
        File file = new File(this.configDir, this.configurationPath);
        URL configurationResourceUrl = this.getClass().getClassLoader().getResource(this.configurationPath);
        if (configurationResourceUrl == null) {
            throw new ConfigurationFileNotInClasspathException(this.configurationPath);
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                Files.copy(configurationResourceUrl.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new ConfigurationInitializationException("An io exception was thrown during the configuration extraction process of %s.".formatted(this.getClass().getName()), exception);
            }
        }
    }

    public void loadConfigurationFile() throws ConfigurationInitializationException {
        File file = new File(this.configDir, this.configurationPath);
        try {
            this.load(file);
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
