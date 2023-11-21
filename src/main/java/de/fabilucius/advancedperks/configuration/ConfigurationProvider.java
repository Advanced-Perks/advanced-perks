package de.fabilucius.advancedperks.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;

@Singleton
public class ConfigurationProvider {

    @Inject
    private Injector injector;

    private final Cache<Class<? extends Configuration>, Configuration> configurationCache = CacheBuilder.newBuilder()
            .build();

    public <T extends Configuration> T getConfigurationAndLoad(Class<T> configurationClass) throws ConfigurationInitializationException {
        try {
            T configuration = configurationClass.cast(this.configurationCache.get(configurationClass,
                    () -> this.injector.getInstance(configurationClass)));
            configuration.extractConfigurationFileFromJar();
            configuration.loadConfigurationFile();
            return configuration;
        } catch (Exception exception) {
            throw new ConfigurationInitializationException("An error occurred during the providing process for the configuration %s.".formatted(configurationClass.getName()), exception);
        }
    }
}
