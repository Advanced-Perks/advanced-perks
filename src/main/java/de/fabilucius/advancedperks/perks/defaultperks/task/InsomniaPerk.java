package de.fabilucius.advancedperks.perks.defaultperks.task;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.Perk;
import de.fabilucius.advancedperks.perks.types.AbstractTaskPerk;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InsomniaPerk extends AbstractTaskPerk {
    public InsomniaPerk() {
        super("Insomnia", new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
                    Perk perk = AdvancedPerks.getInstance().getPerkRegistry().getPerkByIdentifier("insomnia");
                    if (perkData.isPerkActivated(perk)) {
                        player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                    }
                });
            }
        }.runTaskTimerAsynchronously(AdvancedPerks.getInstance(), 0L, 100L));
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.CLOCK)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
