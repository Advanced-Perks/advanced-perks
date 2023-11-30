package de.fabilucius.advancedperks.api.placeholderapi;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.core.logging.APLogger;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedPerksEnabledExpansion extends AdvancedPerksExpansion {

    @Inject
    private PerkRegistryImpl perkRegistryImpl;

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    public AdvancedPerksEnabledExpansion(APLogger logger, AdvancedPerks advancedPerks) {
        super(logger, advancedPerks);
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        Perk perk = this.perkRegistryImpl.getPerkByIdentifier(params);
        if (perk == null) {
            return null;
        }
        return String.valueOf(perkDataRepository.getPerkDataByPlayer(player).getEnabledPerks().contains(perk));
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedperks.enabled";
    }
}
