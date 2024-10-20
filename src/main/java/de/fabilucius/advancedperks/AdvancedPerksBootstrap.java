package de.fabilucius.advancedperks;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.command.PerksCommand;
import de.fabilucius.advancedperks.compatabilities.CompatibilityController;
import de.fabilucius.advancedperks.core.Metrics;
import de.fabilucius.advancedperks.core.configuration.type.SettingsConfiguration;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;

@Singleton
public class AdvancedPerksBootstrap {

    @Inject
    private PerkRegistryImpl perkRegistryImpl;

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private Injector injector;

    @Inject
    private CompatibilityController compatibilityController;

    @Inject
    private AdvancedPerks advancedPerks;

    @Inject
    private APLogger logger;

    @Inject
    private SettingsConfiguration settingsConfiguration;

    public void initializePlugin() throws AdvancedPerksException {
        if (settingsConfiguration.isPluginInDebugMode()) {
            logger.debug("The plugin was started in debug mode.");
        }
        this.perkRegistryImpl.loadAndRegisterDefaultPerks();
        this.perkDataRepository.setupDatabase();
        this.perkDataRepository.loadOnlinePlayer();
        this.injector.getInstance(PerksCommand.class);
        this.compatibilityController.registerCompatibilityClasses();
        Metrics.load(this.logger, settingsConfiguration, this.advancedPerks);
    }

    public void shutdownPlugin() {
        this.perkDataRepository.handleShutdown();
    }

}
