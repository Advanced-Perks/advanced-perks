package de.fabilucius.advancedperks.compatabilities.bukkit;

import com.google.common.collect.Queues;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.compatabilities.AbstractPerkCompatability;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.types.EffectPerk;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.ArrayDeque;


@Singleton
public class PotionDesyncCompatability extends AbstractPerkCompatability {

    @Inject
    private PerkDataRepository perkDataRepository;

    private final ArrayDeque<Player> desyncedPlayer = Queues.newArrayDeque();

    @Inject
    public PotionDesyncCompatability(AdvancedPerks advancedPerks) {
        super(advancedPerks);
        Bukkit.getScheduler().runTaskTimer(advancedPerks, resyncPotionEffectTask, 10L, 10L);
        Bukkit.getScheduler().runTaskTimer(advancedPerks, watchdogTask, 10L, 10L);
    }

    private final Runnable resyncPotionEffectTask = () -> {
        while (!this.desyncedPlayer.isEmpty()) {
            Player player = this.desyncedPlayer.poll();
            PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
            perkData.getEnabledPerks().stream().filter(perk -> perk instanceof EffectPerk).map(perk -> (EffectPerk) perk).forEach(effectPerk -> player.addPotionEffects(effectPerk.getPotionEffects()));
        }
    };

    private final Runnable watchdogTask = () -> {
        Bukkit.getOnlinePlayers().forEach(player -> {
            PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(player);
            perkData.getEnabledPerks().stream().filter(perk -> perk instanceof EffectPerk).map(perk -> (EffectPerk) perk).forEach(effectPerk -> {
                if (effectPerk.getPotionEffects().stream().anyMatch(potionEffect -> !player.hasPotionEffect(potionEffect.getType()))) {
                    if (!this.desyncedPlayer.contains(player)) {
                        this.desyncedPlayer.add(player);
                    }
                }
            });
        });
    };

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
            if (!this.desyncedPlayer.contains(player)) {
                this.desyncedPlayer.add(player);
            }
        }
    }

}