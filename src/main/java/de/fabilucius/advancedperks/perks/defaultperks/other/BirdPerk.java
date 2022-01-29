package de.fabilucius.advancedperks.perks.defaultperks.other;

import de.fabilucius.advancedperks.perks.AbstractPerk;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BirdPerk extends AbstractPerk {
    public BirdPerk() {
        super("Bird");
    }

    @Override
    public ItemStack getDefaultIcon() {
        return new ItemStackBuilder(Material.FEATHER)
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
