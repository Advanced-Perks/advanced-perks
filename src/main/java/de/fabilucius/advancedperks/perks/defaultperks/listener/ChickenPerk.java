package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.commons.item.impl.ItemStackBuilder;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class ChickenPerk extends AbstractListenerPerk {
    public ChickenPerk() {
        super("Chicken");
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.EGG)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(player);
            boolean shouldCancel = event.getCause().equals(EntityDamageEvent.DamageCause.FALL) && perkData.isPerkActivated(this);
            /* To prevent overwriting existing conditions from other plugins */
            if (shouldCancel) {
                event.setCancelled(true);
            }
        }
    }

}
