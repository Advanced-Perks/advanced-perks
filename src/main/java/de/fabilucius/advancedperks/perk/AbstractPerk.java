package de.fabilucius.advancedperks.perk;

import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractPerk implements Perk {

    private final String identifier;
    private final String displayName;
    private final PerkDescription perkDescription;
    private final Set<String> disallowedWorlds;
    private final PerkGuiIcon perkGuiIcon;
    private final boolean enabled;
    private final Map<String, Object> flags;

    public AbstractPerk(String identifier, String displayName, PerkDescription perkDescription, Set<String> disallowedWorlds, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.perkDescription = perkDescription;
        this.disallowedWorlds = disallowedWorlds;
        this.perkGuiIcon = perkGuiIcon;
        this.enabled = enabled;
        this.flags = flags;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public PerkDescription getDescription() {
        return perkDescription;
    }

    @Override
    public Set<String> getDisallowedWorlds() {
        return disallowedWorlds;
    }

    @Override
    public PerkGuiIcon getPerkGuiIcon() {
        return perkGuiIcon;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Map<String, Object> getFlags() {
        return flags;
    }

    /* Flag based properties */

    @Override
    public Optional<String> getPermission() {
        return Optional.ofNullable(this.flags.get("permission").toString());
    }

    @Override
    public Optional<Double> getPrice() {
        try {
            return Optional.of(Double.valueOf(String.valueOf(this.flags.get("price"))));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
