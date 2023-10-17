package de.fabilucius.advancedperks.compatability.bukkit.compats;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.BukkitListener;
import de.fabilucius.advancedperks.compatability.bukkit.BukkitCompatabilityEntity;
import de.fabilucius.advancedperks.perks.defaultperks.other.BirdPerk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangeWorldBukkitCompatability extends BukkitListener implements BukkitCompatabilityEntity {

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        AdvancedPerks.getInstance().getPerkDataRepository().consumePerkData(player, perkData -> {
            perkData.getActivatedPerks().stream().filter(BirdPerk.class::isInstance).findFirst().ifPresent(perk -> {
                perk.perkEnable(event.getPlayer());
            });
            perkData.getActivatedPerks().forEach(perk -> {
                if (perk.getDisabledWorlds().contains(player.getWorld().getName())) {
                    AdvancedPerks.getInstance().getPerkStateController().disablePerk(player, perk);
                }
            });
        });
    }
}
