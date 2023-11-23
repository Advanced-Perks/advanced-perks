package de.fabilucius.advancedperks.core.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
import de.fabilucius.advancedperks.core.database.Database;
import de.fabilucius.advancedperks.core.database.DatabaseProvider;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.registry.PerkRegistry;
import de.fabilucius.advancedperks.registry.loader.PerkYmlLoader;

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
        bind(PerkRegistry.class).asEagerSingleton();
        bind(PerkYmlLoader.class).asEagerSingleton();
        bind(PerkDataRepository.class).asEagerSingleton();
        bind(Database.class).toProvider(DatabaseProvider.class);
        bind(PerkStateController.class).asEagerSingleton();
    }

}
