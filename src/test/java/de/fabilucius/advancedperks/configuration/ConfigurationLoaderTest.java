package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.core.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.configuration.exception.ConfigurationInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConfigurationLoaderTest extends AbstractConfigurationTest {

    @Inject
    ConfigurationLoader configurationLoader;

    @Test
    void testConfigurationExtractionAndLoading() throws Exception {
        TestConfiguration testConfiguration = this.configurationLoader.getConfigurationAndLoad(TestConfiguration.class);
        Assertions.assertEquals("test", testConfiguration.getTestString());
    }

    @Test
    void testConfigurationProviderReturnsTheSameConfigurationTwice() throws Exception {
        int hashCode = this.configurationLoader.getConfigurationAndLoad(TestConfiguration.class).hashCode();
        int otherHashCode = this.configurationLoader.getConfigurationAndLoad(TestConfiguration.class).hashCode();

        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    void testExceptionWhenConfigurationPropertiesAreWrong() {
        Assertions.assertThrows(ConfigurationInitializationException.class,
                () -> this.configurationLoader.getConfigurationAndLoad(NotExistingTestConfiguration.class));
    }

}