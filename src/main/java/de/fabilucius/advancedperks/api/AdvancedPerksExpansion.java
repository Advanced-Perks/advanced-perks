package de.fabilucius.advancedperks.api;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdvancedPerksExpansion extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player != null) {
            Perk perk = AdvancedPerks.getPerkRegistry().getPerkByIdentifier(params);
            if (perk != null) {
                PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(player);
                return String.valueOf(perkData.isPerkActivated(perk));
            }
        }
        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "advancedperks";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(",", AdvancedPerks.getInstance().getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return AdvancedPerks.getInstance().getDescription().getVersion();
    }
}
