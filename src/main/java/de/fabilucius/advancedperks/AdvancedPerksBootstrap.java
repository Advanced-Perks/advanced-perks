package de.fabilucius.advancedperks;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.command.PerksCommand;
import de.fabilucius.advancedperks.compatabilities.CompatibilityController;
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

    public void initializePlugin() throws AdvancedPerksException {
        this.perkRegistryImpl.loadAndRegisterDefaultPerks();
        this.perkDataRepository.setupDatabase();
        this.perkDataRepository.loadOnlinePlayer();
        this.injector.getInstance(PerksCommand.class);
        this.compatibilityController.registerCompatibilityClasses();
    }

    public void shutdownPlugin() {
        this.perkDataRepository.handleShutdown();
    }

}
