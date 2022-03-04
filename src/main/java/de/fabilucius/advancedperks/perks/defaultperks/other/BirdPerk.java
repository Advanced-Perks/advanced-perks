package de.fabilucius.advancedperks.perks.defaultperks.other;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.AbstractPerk;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class BirdPerk extends AbstractPerk implements Listener {

    public BirdPerk() {
        super("Bird");
        if (AdvancedPerks.getInstance().getPerksConfiguration().returnFrom("Bird.Disable-In-Lava").getAsWithDefault(false, Boolean.class)) {
            Bukkit.getPluginManager().registerEvents(this, AdvancedPerks.getInstance());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() != null && event.getTo().getBlock().isLiquid() && event.getTo().getBlock().getType().name().contains("LAVA")) {
            PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(event.getPlayer());
            if (perkData.isPerkActivated(this)) {
                event.getPlayer().setFlying(false);
            }
        } else if (event.getFrom().getBlock().isLiquid() && event.getFrom().getBlock().getType().name().contains("LAVA") &&
                event.getTo() != null && !event.getTo().getBlock().getType().name().contains("LAVA")) {
            PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(event.getPlayer());
            if (perkData.isPerkActivated(this)) {
                event.getPlayer().setFlying(true);
            }
        }
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.FEATHER)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }

    @Override
    public void perkEnable(Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @Override
    public void perkDisable(Player player) {
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}
