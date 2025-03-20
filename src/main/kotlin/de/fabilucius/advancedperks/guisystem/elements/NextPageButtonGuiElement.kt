package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.guisystem.PerkGui
import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class NextPageButtonGuiElement(
    private val perkGui: PerkGui,
    private val icon: ItemStack,
) : PerkGuiElement {

    override fun getGuiIcon(): ItemStack = icon

    override fun onInventoryClick(event: InventoryClickEvent) = perkGui.nextPage()

}