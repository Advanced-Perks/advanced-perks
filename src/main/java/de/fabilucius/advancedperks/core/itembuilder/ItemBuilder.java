package de.fabilucius.advancedperks.core.itembuilder;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface ItemBuilder {

    ItemMeta getItemMeta();
    ItemStack getItemStack();
    ItemStack build();

}
