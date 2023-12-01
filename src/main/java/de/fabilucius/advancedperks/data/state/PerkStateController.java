package de.fabilucius.advancedperks.data.state;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import de.fabilucius.advancedperks.configuration.ConfigurationLoader;
import de.fabilucius.advancedperks.configuration.exception.ConfigurationInitializationException;
import de.fabilucius.advancedperks.core.SettingsConfiguration;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.event.perk.PerkDisableEvent;
import de.fabilucius.advancedperks.event.perk.PerkEnableEvent;
import de.fabilucius.advancedperks.perk.Perk;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PerkStateController {

    private final int globalMaxActivePerks;

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    public PerkStateController(ConfigurationLoader configurationLoader) throws ConfigurationInitializationException {
        SettingsConfiguration settingsConfiguration = configurationLoader.getConfigurationAndLoad(SettingsConfiguration.class);
        this.globalMaxActivePerks = settingsConfiguration.getGlobalMaxActivePerks();
    }

    public PerkUseStatus canUsePerk(Player player, Perk perk) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        int maxActivePerksAllowed = Math.max(this.globalMaxActivePerks, perkData.getMaxPerks());
        //TODO currently unneeded implement sensible way to periodically refresh the permission based max active perks
        /* Max perk active check */
        if (perkData.getEnabledPerks().size() >= maxActivePerksAllowed && maxActivePerksAllowed >= 0) {
            return PerkUseStatus.TOO_MANY_ACTIVE;
        }
        /* Permission check */
        if (perk.getPermission().isPresent() && !player.hasPermission(perk.getPermission().get()) &&
                perkData.getBoughtPerks().stream().noneMatch(boughtPerk -> boughtPerk.equalsIgnoreCase(perk.getIdentifier()))) {
            return PerkUseStatus.NO_PERMISSION;
        }
        /* Disallowed world check */
        if (perk.getDisallowedWorlds().isPresent() && perk.getDisallowedWorlds().get().stream().anyMatch(world -> world.equalsIgnoreCase(player.getWorld().getName()))) {
            return PerkUseStatus.DISALLOWED_WORLD;
        }
        return PerkUseStatus.CAN_BE_USED;
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
        PerkEnableEvent perkEnableEvent = new PerkEnableEvent(player, perk, true);
        Bukkit.getPluginManager().callEvent(perkEnableEvent);
        if (perkData.getEnabledPerks().add(perk)) {
            perk.onPrePerkEnable(player);
        }
        return PerkToggleResult.ENABLED;
    }

    public PerkToggleResult enablePerk(Player player, Perk perk) {
        PerkUseStatus perkUseStatus = this.canUsePerk(player, perk);
        switch (perkUseStatus) {
            case DISALLOWED_WORLD -> {
                return PerkToggleResult.DISALLOWED_WORLD;
            }
            case NO_PERMISSION -> {
                return PerkToggleResult.NO_PERMISSION;
            }
            case TOO_MANY_ACTIVE -> {
                return PerkToggleResult.TOO_MANY_ACTIVE;
            }
            default -> {
                PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
                PerkEnableEvent perkEnableEvent = new PerkEnableEvent(player, perk, false);
                Bukkit.getPluginManager().callEvent(perkEnableEvent);
                if (perkEnableEvent.isCancelled()) {
                    return PerkToggleResult.EVENT_CANCELLED;
                }
                if (perkData.getEnabledPerks().add(perk)) {
                    perk.onPrePerkEnable(player);
                }
                return PerkToggleResult.ENABLED;
            }
        }
    }

    public PerkToggleResult forceDisablePerk(Player player, Perk perk) {
        return this.disable(player, perk, true);
    }

    public PerkToggleResult disablePerk(Player player, Perk perk) {
        return this.disable(player, perk, false);
    }

    public PerkToggleResult disable(Player player, Perk perk, boolean force) {
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
        PerkDisableEvent perkDisableEvent = new PerkDisableEvent(player, perk, force);
        Bukkit.getPluginManager().callEvent(perkDisableEvent);
        if (perkDisableEvent.isCancelled() && !force) {
            return PerkToggleResult.EVENT_CANCELLED;
        }
        if (perkData.getEnabledPerks().remove(perk)) {
            perk.onPrePerkDisable(player);
        }
        return PerkToggleResult.DISABLED;
    }

    public void disablePerks(Player player, List<Perk> perks) {
        perks.forEach(perk -> {
            this.disablePerk(player, perk);
        });
    }

}
