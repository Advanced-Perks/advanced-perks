package de.fabilucius.advancedperks.api;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancedPerksExpansion extends PlaceholderExpansion {

    private static final Logger LOGGER = AdvancedPerks.getInstance().getLogger();

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
