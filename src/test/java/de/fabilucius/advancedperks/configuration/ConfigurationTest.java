package de.fabilucius.advancedperks.configuration;

import de.fabilucius.advancedperks.configuration.exception.ConfigurationFileNotInClasspathException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationOperationException;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

public class ConfigurationTest {

    @TempDir
    File testDataFolder;

    @Test
    void testConfigurationFileExtraction() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/test_config.yml", this.testDataFolder);

        Configuration configuration = Mockito.spy(new Configuration(configurationProperties));
        configuration.extractConfigurationFileFromJar();

        Assertions.assertTrue(configuration.getFile().exists());
    }

    @Test
    void testExceptionWhenConfigurationFileDoesntExistOnClasspath() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/file_doesnt_exist.yml", this.testDataFolder);

        Configuration configuration = Mockito.spy(new Configuration(configurationProperties));

        Assertions.assertThrows(ConfigurationFileNotInClasspathException.class, configuration::extractConfigurationFileFromJar);
    }

    @Test
    void testConfigurationLoadingCorrect() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/test_config.yml", this.testDataFolder);

        Configuration configuration = Mockito.spy(new Configuration(configurationProperties));
        configuration.extractConfigurationFileFromJar();
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "test");
        Assertions.assertEquals(configuration.getDouble("test_double"), 2.0);
        Assertions.assertEquals(configuration.getInt("test_int"), 2);
        Assertions.assertTrue(configuration.getBoolean("test_boolean"));
    }

    @Test
    void testExceptionWhenConfigurationInvalid() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/invalid_test_config.yml", this.testDataFolder);

        Configuration configuration = Mockito.spy(new Configuration(configurationProperties));
        configuration.extractConfigurationFileFromJar();

        Assertions.assertThrows(ConfigurationInitializationException.class, configuration::loadConfigurationFile);
    }

    @Test
    void testConfigurationSaveProcess() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/test_config.yml", this.testDataFolder);

        Configuration configuration = Mockito.spy(new Configuration(configurationProperties));
        configuration.extractConfigurationFileFromJar();
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "test");

        configuration.set("test_string", "another_string");
        configuration.saveConfiguration();

        configuration = Mockito.spy(new Configuration(configurationProperties));
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "another_string");
    }

    @Test
    void testConfigurationSaveProcessHandleIOException() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/test_config.yml", this.testDataFolder);

        Configuration configuration = Mockito.spy(new Configuration(configurationProperties));
        configuration.extractConfigurationFileFromJar();

        configuration.getFile().setReadOnly();

        Assertions.assertThrows(ConfigurationOperationException.class, configuration::saveConfiguration);
    }

}
