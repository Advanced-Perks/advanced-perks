package de.fabilucius.advancedperks.data.state;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationProvider;
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
    public PerkStateController(ConfigurationProvider configurationProvider) throws ConfigurationInitializationException {
        SettingsConfiguration settingsConfiguration = configurationProvider.getConfigurationAndLoad(SettingsConfiguration.class);
        this.globalMaxActivePerks = settingsConfiguration.getGlobalMaxActivePerks();
    }

    public PerkToggleResult forceEnablePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.getEnabledPerks().add(perk)) {
            perk.onPrePerkEnable(player);
        }
        return PerkToggleResult.SUCCESS;
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
        return PerkToggleResult.SUCCESS;
    }

    public PerkToggleResult disablePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        if (perkData.getEnabledPerks().remove(perk)) {
            perk.onPrePerkDisable(player);
        }
        //TODO find out if this makes problems when i gets out of sync and if a PerkToggleResult.NOT_ACTIVATED/ALREADY_ACTIVATED are needed
        return PerkToggleResult.SUCCESS;
    }

}
