package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class TelekinesisPerk extends AbstractListenerPerk {
    public TelekinesisPerk() {
        super("Telekinesis");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(event.getPlayer());
        if (perkData.isPerkActivated(this)) {
            Iterator<Item> iterator = event.getItems().iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (!event.getPlayer().getInventory().addItem(item.getItemStack()).isEmpty()) {
                    break;
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.ENDER_PEARL)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
