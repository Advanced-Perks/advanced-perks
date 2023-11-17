package de.fabilucius.advancedperks;

import com.google.inject.AbstractModule;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.core.logging.APLoggerProvider;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AdvancedPerks.class).toInstance(TestMocks.getAdvancedPerksMock());
        bind(APLogger.class).toProvider(APLoggerProvider.class);
        bind(ConfigurationProvider.class).asEagerSingleton();
    }
}
