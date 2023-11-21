package de.fabilucius.advancedperks;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.exception.AdvancedPerksException;
import de.fabilucius.advancedperks.registry.PerkRegistry;

public class AdvancedPerksBootstrap {

    @Inject
    private PerkRegistry perkRegistry;

    public void initializePlugin() throws AdvancedPerksException {
        this.perkRegistry.loadAndRegisterDefaultPerks();
    }

}
