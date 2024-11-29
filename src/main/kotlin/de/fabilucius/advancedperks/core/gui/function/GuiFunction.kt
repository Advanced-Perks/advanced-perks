package de.fabilucius.advancedperks.core.gui.function

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

interface GuiFunction {
     fun onInventoryClose(event: InventoryCloseEvent) {}

    fun onInventoryClick(event: InventoryClickEvent) {}

    fun onInventoryOpen(event: InventoryOpenEvent) {}
}