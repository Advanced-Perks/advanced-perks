package de.fabilucius.advancedperks.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.logging.APLogger;

@Singleton
public class ConfigurationProvider {

    @Inject
    private APLogger logger;
    private final Cache<Class<? extends Configuration>, Configuration> configurationCache = CacheBuilder.newBuilder()
            .build();

    public <T extends Configuration> T getConfigurationAndLoad(Class<T> configurationClass, ConfigurationProperties configurationProperties) throws ConfigurationInitializationException {
        try {
            T configuration = configurationClass.cast(this.configurationCache.get(configurationClass,
                    () -> this.createConfigurationInstance(configurationClass, configurationProperties)));
            configuration.extractConfigurationFileFromJar();
            configuration.loadConfigurationFile();
            return configuration;
        } catch (Exception exception) {
            throw new ConfigurationInitializationException("An error occurred during the providing process for the configuration %s.".formatted(configurationClass.getName()), exception);
        }
    }


    private <T extends Configuration> T createConfigurationInstance(Class<T> configurationClass, ConfigurationProperties configurationProperties) throws Exception {
        return configurationClass.getConstructor(ConfigurationProperties.class).newInstance(configurationProperties);
    }

}
