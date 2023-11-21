package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigurationProviderTest extends AbstractConfigurationTest {

    @Inject
    ConfigurationProvider configurationProvider;

    @Test
    void testConfigurationExtractionAndLoading() throws Exception {
        TestConfiguration testConfiguration = this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class);
        Assertions.assertEquals(testConfiguration.getTestString(), "test");
    }

    @Test
    void testConfigurationProviderReturnsTheSameConfigurationTwice() throws Exception {
        int hashCode = this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class).hashCode();
        int otherHashCode = this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class).hashCode();

        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    void testExceptionWhenConfigurationPropertiesAreWrong() {
        Assertions.assertThrows(ConfigurationInitializationException.class,
                () -> this.configurationProvider.getConfigurationAndLoad(NotExistingTestConfiguration.class));
    }

}