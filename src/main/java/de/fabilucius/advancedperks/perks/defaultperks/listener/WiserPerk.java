package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import de.fabilucius.advancedperks.utilities.ItemStackBuilder;
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
        return new ItemStackBuilder(Material.ENCHANTED_BOOK)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PerkData perkData = AdvancedPerks.getInstance().getPerkDataRepository().getPerkData(event.getEntity());
        if (perkData.isPerkActivated(this)) {
            event.setKeepLevel(true);
        }
    }

}
