package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder
import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import de.fabilucius.advancedperks.perk.Perk
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class PerkButtonGuiElement(
    private val perk: Perk
) : PerkGuiElement {

    //TODO redo that (rewrite and properly use perk icon)
    override fun getGuiIcon(): ItemStack = ItemStackBuilder.fromItemStack(perk.perkGuiIcon.icon)
        .setDisplayName(perk.displayName)
        .setDescription(perk.description.lines)
        .build();

    override fun onInventoryClick(event: InventoryClickEvent) {
    }

}