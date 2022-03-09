package de.fabilucius.advancedperks.perks.defaultperks.listener;

import de.fabilucius.advancedperks.AdvancedPerks;
import de.fabilucius.advancedperks.data.PerkData;
import de.fabilucius.advancedperks.perks.types.AbstractListenerPerk;
import de.fabilucius.sympel.item.builder.types.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class SmelterPerk extends AbstractListenerPerk {
    public SmelterPerk() {
        super("Smelter");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDropItem(BlockDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        PerkData perkData = AdvancedPerks.getPerkDataRepository().getPerkData(event.getPlayer());
        if (perkData.isPerkActivated(this)) {
            for (Item item : event.getItems()) {
                Material material = item.getItemStack().getType();
                switch (material) {
                    case COAL_ORE:
                    case DEEPSLATE_COAL_ORE:
                        item.getItemStack().setType(Material.COAL);
                        break;
                    case IRON_ORE:
                    case DEEPSLATE_IRON_ORE:
                    case RAW_IRON:
                        item.getItemStack().setType(Material.IRON_INGOT);
                        break;
                    case GOLD_ORE:
                    case DEEPSLATE_GOLD_ORE:
                    case RAW_GOLD:
                        item.getItemStack().setType(Material.GOLD_INGOT);
                        break;
                    case COPPER_ORE:
                    case DEEPSLATE_COPPER_ORE:
                    case RAW_COPPER:
                        item.getItemStack().setType(Material.COPPER_INGOT);
                        break;
                    case DIAMOND_ORE:
                    case DEEPSLATE_DIAMOND_ORE:
                        item.getItemStack().setType(Material.DIAMOND);
                        break;
                    case EMERALD_ORE:
                    case DEEPSLATE_EMERALD_ORE:
                        item.getItemStack().setType(Material.EMERALD);
                        break;
                    case LAPIS_ORE:
                    case DEEPSLATE_LAPIS_ORE:
                        item.getItemStack().setType(Material.LAPIS_LAZULI);
                        break;
                    case NETHER_QUARTZ_ORE:
                        item.getItemStack().setType(Material.QUARTZ);
                        break;
                    case REDSTONE_ORE:
                    case DEEPSLATE_REDSTONE_ORE:
                        item.getItemStack().setType(Material.REDSTONE);
                        break;
                    case NETHER_GOLD_ORE:
                        item.getItemStack().setType(Material.GOLD_NUGGET);
                        break;
                    case ANCIENT_DEBRIS:
                        item.getItemStack().setType(Material.NETHERITE_SCRAP);
                        break;
                }
            }
        }
    }

    @Override
    public ItemStack getDefaultIcon() {
        return ItemStackBuilder.fromMaterial(Material.MAGMA_CREAM)
                .setDisplayName(this.getDisplayName())
                .setDescription(this.getDescription())
                .build();
    }
}
