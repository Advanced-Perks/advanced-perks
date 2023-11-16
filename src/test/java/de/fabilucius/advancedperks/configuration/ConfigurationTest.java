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
        String configurationPath = "configuration/test_config.yml";

        Configuration configuration = Mockito.spy(new Configuration(this.testDataFolder, configurationPath));
        configuration.extractConfigurationFileFromJar();

        Assertions.assertTrue(new File(this.testDataFolder, configurationPath).exists());
    }

    @Test
    void testExceptionWhenConfigurationFileDoesntExistOnClasspath() {
        String configurationPath = "file_doesnt_exist.yml";

        Configuration configuration = Mockito.spy(new Configuration(this.testDataFolder, configurationPath));

        Assertions.assertThrows(ConfigurationFileNotInClasspathException.class, configuration::extractConfigurationFileFromJar);
    }

    @Test
    void testConfigurationLoadingCorrect() throws Exception {
        String configurationPath = "configuration/test_config.yml";

        Configuration configuration = Mockito.spy(new Configuration(this.testDataFolder, configurationPath));
        configuration.extractConfigurationFileFromJar();
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "test");
        Assertions.assertEquals(configuration.getDouble("test_double"), 2.0);
        Assertions.assertEquals(configuration.getInt("test_int"), 2);
        Assertions.assertTrue(configuration.getBoolean("test_boolean"));
    }

    @Test
    void testExceptionWhenConfigurationInvalid() throws Exception {
        String configurationPath = "configuration/invalid_test_config.yml";

        Configuration configuration = Mockito.spy(new Configuration(testDataFolder, configurationPath));
        configuration.extractConfigurationFileFromJar();

        Assertions.assertThrows(ConfigurationInitializationException.class, configuration::loadConfigurationFile);
    }

    @Test
    void testConfigurationSaveProcess() throws Exception {
        String configurationPath = "configuration/test_config.yml";

        Configuration configuration = Mockito.spy(new Configuration(this.testDataFolder, configurationPath));
        configuration.extractConfigurationFileFromJar();
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "test");

        configuration.set("test_string", "another_string");
        configuration.saveConfiguration();

        configuration = Mockito.spy(new Configuration(this.testDataFolder, configurationPath));
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "another_string");
    }

    @Test
    void testConfigurationSaveProcessHandleIOException() throws Exception {
        String configurationPath = "configuration/test_config.yml";

        Configuration configuration = Mockito.spy(new Configuration(this.testDataFolder, configurationPath));
        configuration.extractConfigurationFileFromJar();

        configuration.getFile().setReadOnly();

        Assertions.assertThrows(ConfigurationOperationException.class, configuration::saveConfiguration);
    }

}
