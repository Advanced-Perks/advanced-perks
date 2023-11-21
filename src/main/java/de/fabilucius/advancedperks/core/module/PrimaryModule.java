package de.fabilucius.advancedperks.core.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.core.logging.APLogger;

import java.io.File;

public class PrimaryModule extends AbstractModule {

    private final AdvancedPerks advancedPerks;

    public PrimaryModule(AdvancedPerks advancedPerks) {
        this.advancedPerks = advancedPerks;
    }

    @Override
    protected void configure() {
        bind(AdvancedPerks.class).toInstance(this.advancedPerks);
        bind(File.class).annotatedWith(Names.named("configurationDirectory")).toInstance(this.advancedPerks.getDataFolder());
        bind(APLogger.class).asEagerSingleton();
        bind(ConfigurationProvider.class).asEagerSingleton();
    }

}
