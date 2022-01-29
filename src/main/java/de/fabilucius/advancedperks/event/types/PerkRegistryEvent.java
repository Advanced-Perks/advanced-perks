package de.fabilucius.advancedperks.event.types;

import de.fabilucius.advancedperks.event.PerkEvent;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.perks.PerkListCache;

public class PerkRegistryEvent extends PerkEvent {

    private final PerkListCache perkRegistry;

    public PerkRegistryEvent(PerkListCache perkRegistry) {
        this.perkRegistry = perkRegistry;
    }

    public void registerPerks(Class<? extends Perk>... perkClasses) {
        this.getPerkRegistry().registerPerks();
    }

    /* the getter and setter of this class */

    public PerkListCache getPerkRegistry() {
        return perkRegistry;
    }
}
