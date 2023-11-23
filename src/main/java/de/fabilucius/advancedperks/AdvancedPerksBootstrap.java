package de.fabilucius.advancedperks;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.fabilucius.advancedperks.command.PerksCommand;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import de.fabilucius.advancedperks.registry.PerkRegistry;

public class AdvancedPerksBootstrap {

    @Inject
    private PerkRegistry perkRegistry;

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private Injector injector;

    public void initializePlugin() throws AdvancedPerksException {
        this.perkRegistry.loadAndRegisterDefaultPerks();
        this.perkDataRepository.setupDatabase();
        this.injector.getInstance(PerksCommand.class);
    }

}
