package de.fabilucius.advancedperks.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.data.state.PerkStateController;
import de.fabilucius.advancedperks.data.state.PerkToggleResult;
import de.fabilucius.advancedperks.data.state.PerkUseStatus;
import de.fabilucius.advancedperks.perk.Perk;
import de.fabilucius.advancedperks.registry.PerkRegistryImpl;
import org.bukkit.entity.Player;

@Singleton
public class AdvancedPerksApiImpl implements AdvancedPerksApi {

    private static AdvancedPerksApiImpl instance;

    @Inject
    private PerkStateController perkStateController;

    @Inject
    private PerkRegistryImpl perkRegistry;

    public AdvancedPerksApiImpl() {
        instance = this;
    }

    @Override
    public PerkStateController getPerkStateController() {
        return this.perkStateController;
    }

    @Override
    public PerkRegistryImpl getPerkRegistry() {
        return this.perkRegistry;
    }

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
    public static AdvancedPerksApiImpl getInstance() {
        return instance;
    }

}
