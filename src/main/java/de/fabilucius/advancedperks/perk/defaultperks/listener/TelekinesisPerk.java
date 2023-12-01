package de.fabilucius.advancedperks.perk.defaultperks.listener;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.ListenerPerk;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;

import java.util.Iterator;
import java.util.Map;

@PerkIdentifier("telekinesis")
public class TelekinesisPerk extends AbstractDefaultPerk implements ListenerPerk {

    @Inject
    private PerkDataRepository perkDataRepository;

    public TelekinesisPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(event.getPlayer());
        if (perkData.getEnabledPerks().contains(this)) {
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

}
