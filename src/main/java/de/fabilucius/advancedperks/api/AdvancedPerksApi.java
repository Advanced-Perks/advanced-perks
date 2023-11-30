package de.fabilucius.advancedperks.api;

import de.fabilucius.advancedperks.data.state.PerkToggleResult;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

public interface AdvancedPerksApi {

    static AdvancedPerksApi getInstance() {
        return AdvancedPerksApiImpl.getInstance();
    }

    PerkToggleResult enablePerk(Player player, Perk perk);

    PerkToggleResult forceEnablePerk(Player player, Perk perk);

    PerkToggleResult disablePerk(Player player, Perk perk);

    boolean hasPermissionForPerk(Player player, Perk perk);

}
