package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class CloseGuiButtonGuiElement(
    private val icon: ItemStack
) : PerkGuiElement {

    override fun getGuiIcon(): ItemStack = icon

    override fun onInventoryClick(event: InventoryClickEvent) = event.whoClicked.closeInventory()

}