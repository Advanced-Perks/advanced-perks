package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import de.fabilucius.advancedperks.perk.Perk
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class PerkButtonGuiElement(
    private val perk: Perk
) : PerkGuiElement {

    override fun getGuiIcon(): ItemStack = perk.perkGuiIcon.icon

    override fun onInventoryClick(event: InventoryClickEvent) {
    }

}