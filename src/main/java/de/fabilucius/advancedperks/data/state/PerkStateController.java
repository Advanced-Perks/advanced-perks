package de.fabilucius.advancedperks.data.state;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.SettingsConfiguration;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.entity.Player;

public class PerkStateController {

    private final int globalMaxActivePerks;

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    public PerkStateController(ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        SettingsConfiguration settingsConfiguration = configurationLoader.getConfigurationAndLoad(SettingsConfiguration.class);
        this.globalMaxActivePerks = settingsConfiguration.getGlobalMaxActivePerks();
    }

    public void disableAllPerks(Player player) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        Lists.newArrayList(perkData.getEnabledPerks()).forEach(perk -> this.disablePerk(player, perk));
    }

    public PerkToggleResult togglePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.getEnabledPerks().contains(perk)) {
            return this.disablePerk(player, perk);
        } else {
            return this.enablePerk(player, perk);
        }
    }

    public PerkToggleResult forceEnablePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.getEnabledPerks().add(perk)) {
            perk.onPrePerkEnable(player);
        }
        return PerkToggleResult.ENABLED;
    }

    public PerkToggleResult enablePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        int maxActivePerksAllowed = Math.max(this.globalMaxActivePerks, perkData.getMaxPerks());
        //TODO implement sensible way to periodically refresh the permission based max active perks
        /* Max perk active check */
        if (perkData.getEnabledPerks().size() >= maxActivePerksAllowed && maxActivePerksAllowed >= 0) {
            return PerkToggleResult.TOO_MANY_ACTIVE;
        }
        /* Permission check */
        if (perk.getPermission().isPresent() && !player.hasPermission(perk.getPermission().get()) &&
                perkData.getBoughtPerks().stream().noneMatch(boughtPerk -> boughtPerk.equalsIgnoreCase(perk.getIdentifier()))) {
            return PerkToggleResult.NO_PERMISSION;
        }
        /* Disallowed world check */
        if (perk.getDisallowedWorlds().isPresent() && perk.getDisallowedWorlds().get().stream().anyMatch(world -> world.equalsIgnoreCase(player.getWorld().getName()))) {
            return PerkToggleResult.DISALLOWED_WORLD;
        }
        if (perkData.getEnabledPerks().add(perk)) {
            perk.onPrePerkEnable(player);
        }
        return PerkToggleResult.ENABLED;
    }

    public PerkToggleResult disablePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.getEnabledPerks().remove(perk)) {
            perk.onPrePerkDisable(player);
        }
        //TODO find out if this makes problems when i gets out of sync and if a PerkToggleResult.NOT_ACTIVATED/ALREADY_ACTIVATED are needed
        return PerkToggleResult.DISABLED;
    }

}
