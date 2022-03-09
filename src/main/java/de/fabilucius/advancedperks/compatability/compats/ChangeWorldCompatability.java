package de.fabilucius.advancedperks.compatability.compats;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.BukkitListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class ChangeWorldCompatability extends BukkitListener {

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        AdvancedPerks.getPerkDataRepository().consumePerkData(player, perkData -> {
            perkData.getActivatedPerks().forEach(perk -> {
                if (perk.getDisabledWorlds().contains(player.getWorld().getName())) {
                    AdvancedPerks.getPerkStateController().disablePerk(player, perk);
                }
            });
        });
    }
}
