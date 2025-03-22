package de.fabilucius.advancedperks.guisystem

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

interface PerkGuiElement {

    fun getGuiIcon(): ItemStack

    fun onInventoryClick(event: InventoryClickEvent)

}