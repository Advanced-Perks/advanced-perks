package de.fabilucius.advancedperks.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.data.state.PerkToggleResult;
import de.fabilucius.advancedperks.data.state.PerkUseStatus;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

@Singleton
public class AdvancedPerksApiImpl implements AdvancedPerksApi {

    @Inject
    private PerkStateController perkStateController;

    @Override
    public PerkToggleResult enablePerk(Player player, Perk perk) {
        return this.perkStateController.enablePerk(player, perk);
    }

    @Override
    public PerkToggleResult forceEnablePerk(Player player, Perk perk) {
        return this.perkStateController.togglePerk(player, perk);
    }

    @Override
    public PerkToggleResult disablePerk(Player player, Perk perk) {
        return this.perkStateController.disablePerk(player, perk);
    }

    @Override
    public boolean hasPermissionForPerk(Player player, Perk perk) {
        PerkUseStatus useStatus = this.perkStateController.canUsePerk(player, perk);
        return !useStatus.equals(PerkUseStatus.NO_PERMISSION);
    }

    /* Singleton stuff */

    private static AdvancedPerksApiImpl instance;

    public AdvancedPerksApiImpl() {
        instance = this;
    }

    public static AdvancedPerksApiImpl getInstance() {
        return instance;
    }

}
