package de.fabilucius.advancedperks.configuration;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AbstractTest;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class ConfigurationProviderTest extends AbstractTest {

    @Inject
    ConfigurationProvider configurationProvider;

    @TempDir
    File dataFolder;

    @Test
    void testConfigurationExtractionAndLoading() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/test_config.yml", this.dataFolder);
        TestConfiguration testConfiguration = this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class, configurationProperties);

        Assertions.assertEquals(testConfiguration.getTestString(), "test");
    }

    @Test
    void testConfigurationProviderReturnsTheSameConfigurationTwice() throws Exception {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/test_config.yml", this.dataFolder);
        int hashCode = this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class, configurationProperties).hashCode();
        int otherHashCode = this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class, configurationProperties).hashCode();

        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    void testExceptionWhenConfigurationPropertiesAreWrong() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties("configuration/config_that_doesnt_exist.yml", this.dataFolder);

        Assertions.assertThrows(ConfigurationInitializationException.class,
                () -> this.configurationProvider.getConfigurationAndLoad(TestConfiguration.class, configurationProperties));
    }

}

class TestConfiguration extends Configuration {

    public TestConfiguration(ConfigurationProperties configurationProperties) {
        super(configurationProperties);
    }

    public String getTestString() {
        return this.getString("test_string");
    }

}
