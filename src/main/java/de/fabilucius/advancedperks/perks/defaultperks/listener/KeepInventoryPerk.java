package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class KeepInventoryPerk extends AbstractListenerPerk {
    public KeepInventoryPerk() {
        super("KeepInventory");
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.CHEST)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(event.getEntity());
        if (perkData.isPerkActivated(this)) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }

}
