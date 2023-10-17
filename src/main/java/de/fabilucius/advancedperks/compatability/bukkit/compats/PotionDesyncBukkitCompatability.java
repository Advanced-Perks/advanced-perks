package de.fabilucius.advancedperks.compatability.bukkit.compats;

import com.google.common.collect.Queues;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.BukkitListener;
import de.fabilucius.advancedperks.compatability.bukkit.BukkitCompatabilityEntity;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractEffectPerk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayDeque;

public class PotionDesyncBukkitCompatability extends BukkitListener implements BukkitCompatabilityEntity {

    private final ArrayDeque<Player> desyncedPlayer = Queues.newArrayDeque();

    public PotionDesyncBukkitCompatability() {
        Bukkit.getScheduler().runTaskTimer(AdvancedPerks.getInstance(), this.getPotionResyncTask(), 10L, 10L);
        Bukkit.getScheduler().runTaskTimer(AdvancedPerks.getInstance(), this.getWatchdogTask(), 10L, 10L);
    }

    private final Runnable potionResyncTask = () -> {
        while (!this.getDesyncedPlayer().isEmpty()) {
            Player player = this.getDesyncedPlayer().poll();
            PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
            perkData.getActivatedPerks().forEach(perk -> {
                if (perk instanceof AbstractEffectPerk) {
                    AbstractEffectPerk effectPerk = (AbstractEffectPerk) perk;
                    if (player != null) {
                        player.addPotionEffect(effectPerk.getPotionEffect());
                    }
                }
            });
        }
    };

    private final Runnable watchdogTask = () -> Bukkit.getOnlinePlayers().forEach(player -> {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        perkData.getActivatedPerks().forEach(perk -> {
            if (perk instanceof AbstractEffectPerk) {
                AbstractEffectPerk effectPerk = (AbstractEffectPerk) perk;
                if (!player.hasPotionEffect(effectPerk.getPotionEffect().getType()) && !this.getDesyncedPlayer().contains(player)) {
                    this.getDesyncedPlayer().add(player);
                }
            }
        });
    });

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
            Player player = event.getPlayer();
            if (!this.getDesyncedPlayer().contains(player)) {
                this.getDesyncedPlayer().add(player);
            }
        }
    }

    /* the getter and setter of this class */

    public ArrayDeque<Player> getDesyncedPlayer() {
        return desyncedPlayer;
    }

    public Runnable getWatchdogTask() {
        return watchdogTask;
    }

    public Runnable getPotionResyncTask() {
        return potionResyncTask;
    }
}
