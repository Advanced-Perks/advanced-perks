package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationFileNotInClasspathException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ConfigurationTest extends AbstractConfigurationTest {

    @Inject
    private Injector injector;

    @Test
    void testConfigurationFileExtraction() throws Exception {
        Configuration configuration = Mockito.spy(this.injector.getInstance(TestConfiguration.class));
        configuration.extractConfigurationFileFromJar();

        Assertions.assertTrue(configuration.getFile().exists());
    }

    @Test
    void testExceptionWhenConfigurationFileDoesntExistOnClasspath() {
        Configuration configuration = Mockito.spy(this.injector.getInstance(NotExistingTestConfiguration.class));

        Assertions.assertThrows(ConfigurationFileNotInClasspathException.class, configuration::extractConfigurationFileFromJar);
    }

    @Test
    void testConfigurationLoadingCorrect() throws Exception {
        Configuration configuration = Mockito.spy(this.injector.getInstance(TestConfiguration.class));
        configuration.extractConfigurationFileFromJar();
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "test");
        Assertions.assertEquals(configuration.getDouble("test_double"), 2.0);
        Assertions.assertEquals(configuration.getInt("test_int"), 2);
        Assertions.assertTrue(configuration.getBoolean("test_boolean"));
    }

    @Test
    void testExceptionWhenConfigurationInvalid() throws Exception {
        Configuration configuration = Mockito.spy(this.injector.getInstance(InvalidTestConfiguration.class));
        configuration.extractConfigurationFileFromJar();

        Assertions.assertThrows(ConfigurationInitializationException.class, configuration::loadConfigurationFile);
    }

    @Test
    void testConfigurationSaveProcess() throws Exception {
        Configuration configuration = Mockito.spy(this.injector.getInstance(TestConfiguration.class));
        configuration.extractConfigurationFileFromJar();
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "test");

        configuration.set("test_string", "another_string");
        configuration.saveConfiguration();

        configuration = Mockito.spy(this.injector.getInstance(TestConfiguration.class));
        configuration.loadConfigurationFile();

        Assertions.assertEquals(configuration.getString("test_string"), "another_string");
    }

    @Test
    void testConfigurationSaveProcessHandleIOException() throws Exception {
        Configuration configuration = Mockito.spy(this.injector.getInstance(TestConfiguration.class));
        configuration.extractConfigurationFileFromJar();

        configuration.getFile().setReadOnly();

        Assertions.assertThrows(ConfigurationOperationException.class, configuration::saveConfiguration);
    }

}
