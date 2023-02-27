package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

public class FarmerPerk extends AbstractListenerPerk {
    public FarmerPerk() {
        super("Farmer");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Ageable) ||
                ((Ageable) event.getBlock().getBlockData()).getAge() != ((Ageable) event.getBlock().getBlockData()).getMaximumAge()) {
            return;
        }
        PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(event.getPlayer());
        if (perkData.isPerkActivated(this)) {
            Material crop = event.getBlock().getType();
            Bukkit.getScheduler().runTaskLater(AdvancedPerks.getInstance(), () -> {
                event.getBlock().setType(crop);
            }, 1L);
        }
    }


    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.WHEAT)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
