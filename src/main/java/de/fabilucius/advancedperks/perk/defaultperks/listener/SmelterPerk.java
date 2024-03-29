package de.fabilucius.advancedperks.perk.defaultperks.listener;

import com.google.inject.Inject;
import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.data.PerkDataRepository;
import de.fabilucius.advancedperks.perk.AbstractDefaultPerk;
import de.fabilucius.advancedperks.perk.annotation.PerkIdentifier;
import de.fabilucius.advancedperks.perk.properties.PerkDescription;
import de.fabilucius.advancedperks.perk.properties.PerkGuiIcon;
import de.fabilucius.advancedperks.perk.types.ListenerPerk;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;

import java.util.Map;

@PerkIdentifier("smelter")
public class SmelterPerk extends AbstractDefaultPerk implements ListenerPerk {

    @Inject
    private PerkDataRepository perkDataRepository;

    @Inject
    private AdvancedPerks advancedPerks;

    public SmelterPerk(String identifier, String displayName, PerkDescription perkDescription, PerkGuiIcon perkGuiIcon, boolean enabled, Map<String, Object> flags) {
        super(identifier, displayName, perkDescription, perkGuiIcon, enabled, flags);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        PerkData perkData = this.perkDataRepository.getPerkDataByPlayer(event.getPlayer());
        if (perkData.getEnabledPerks().contains(this)) {
            for (Item item : event.getItems()) {
                Material material = item.getItemStack().getType();
                switch (material.name()) {
                    case "COAL_ORE", "DEEPSLATE_COAL_ORE":
                        item.getItemStack().setType(Material.COAL);
                        break;
                    case "IRON_ORE", "DEEPSLATE_IRON_ORE", "RAW_IRON":
                        item.getItemStack().setType(Material.IRON_INGOT);
                        break;
                    case "GOLD_ORE", "DEEPSLATE_GOLD_ORE", "RAW_GOLD":
                        item.getItemStack().setType(Material.GOLD_INGOT);
                        break;
                    case "COPPER_ORE", "DEEPSLATE_COPPER_ORE", "RAW_COPPER":
                        item.getItemStack().setType(Material.valueOf("COPPER_INGOT"));
                        break;
                    case "DIAMOND_ORE", "DEEPSLATE_DIAMOND_ORE":
                        item.getItemStack().setType(Material.DIAMOND);
                        break;
                    case "EMERALD_ORE", "DEEPSLATE_EMERALD_ORE":
                        item.getItemStack().setType(Material.EMERALD);
                        break;
                    case "LAPIS_ORE", "DEEPSLATE_LAPIS_ORE":
                        item.getItemStack().setType(Material.LAPIS_LAZULI);
                        break;
                    case "NETHER_QUARTZ_ORE":
                        item.getItemStack().setType(Material.QUARTZ);
                        break;
                    case "REDSTONE_ORE", "DEEPSLATE_REDSTONE_ORE":
                        item.getItemStack().setType(Material.REDSTONE);
                        break;
                    case "NETHER_GOLD_ORE":
                        item.getItemStack().setType(Material.GOLD_NUGGET);
                        break;
                    case "ANCIENT_DEBRIS":
                        item.getItemStack().setType(Material.NETHERITE_SCRAP);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
