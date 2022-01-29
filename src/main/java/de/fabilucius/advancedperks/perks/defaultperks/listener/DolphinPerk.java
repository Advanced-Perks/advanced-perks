package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import de.fabilucius.sympel.item.builder.types.SkullStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DolphinPerk extends AbstractListenerPerk {
    public DolphinPerk() {
        super("Dolphin");
    }

    @Override
    public ItemStack getDefaultIcon() {
        return SkullStackBuilder.fromApproximateMaterial("PLAYER_HEAD")
                .setBase64Value("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ==")
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent playerToggleSneakEvent) {
        Player player = playerToggleSneakEvent.getPlayer();
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(player);
        if (perkData.isPerkActivated(this) && playerToggleSneakEvent.isSneaking()) {
            Material playersBlock = player.getLocation().getBlock().getType();
            if (playersBlock == Material.WATER) {
                Vector direction = player.getLocation().getDirection().clone();
                player.setVelocity(direction.multiply(0.5D).setY(direction.getY() * 1.5D));
            }
        }
    }

}
