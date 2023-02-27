package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class WiserPerk extends AbstractListenerPerk {
    public WiserPerk() {
        super("Wiser");
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.ENCHANTED_BOOK)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(event.getEntity());
        if (perkData.isPerkActivated(this)) {
            event.setKeepLevel(true);
            event.setDroppedExp(0);
        }
    }

}
