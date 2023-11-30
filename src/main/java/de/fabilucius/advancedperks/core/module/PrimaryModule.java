package de.fabilucius.advancedperks.core.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.api.AdvancedPerksApi;
import de.fabilucius.advancedperks.api.AdvancedPerksApiImpl;
import de.fabilucius.advancedperks.api.placeholderapi.AdvancedPerksEnabledExpansion;
import de.fabilucius.advancedperks.api.placeholderapi.AdvancedPerksUseExpansion;
import de.fabilucius.advancedperks.compatabilities.CompatibilityController;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.core.database.Database;
import de.fabilucius.advancedperks.core.database.DatabaseProvider;
import de.fabilucius.advancedperks.core.economy.EconomyController;
import de.fabilucius.advancedperks.core.economy.interfaces.EconomyInterface;
import de.fabilucius.advancedperks.core.economy.interfaces.types.VaultEconomyInterface;
import de.fabilucius.advancedperks.core.guisystem.GuiSystemManager;
import de.fabilucius.advancedperks.core.guisystem.persistantdata.NamespacedKeyProvider;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.registry.PerkRegistry;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import de.fabilucius.advancedperks.registry.loader.PerkYmlLoader;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

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
        bind(ConfigurationLoader.class).asEagerSingleton();
        bind(PerkRegistry.class).to(PerkRegistryImpl.class);
        bind(PerkYmlLoader.class).asEagerSingleton();
        bind(PerkDataRepository.class).asEagerSingleton();
        bind(Database.class).toProvider(DatabaseProvider.class);
        bind(PerkStateController.class).asEagerSingleton();
        bind(GuiSystemManager.class).asEagerSingleton();
        bind(NamespacedKey.class).annotatedWith(Names.named("uuidKey")).toProvider(NamespacedKeyProvider.class);
        bind(AdvancedPerksApi.class).to(AdvancedPerksApiImpl.class);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            bind(AdvancedPerksUseExpansion.class).asEagerSingleton();
            bind(AdvancedPerksEnabledExpansion.class).asEagerSingleton();
        }
        bind(EconomyController.class).asEagerSingleton();
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            bind(EconomyInterface.class).to(VaultEconomyInterface.class);
        } else {
            bind(EconomyInterface.class).toProvider(Providers.of(null));
        }
        bind(CompatibilityController.class).asEagerSingleton();
    }

}
