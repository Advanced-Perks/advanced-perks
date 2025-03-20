package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ToggleButtonGuiElement(
    //TODO REWORK WITH PROPER USE OF PERKSTATECONTROLLER OR SIMILAR FOR STATE STUFF
    private val active: Boolean,
    private val activeIcon: ItemStack,
    private val inactiveIcon: ItemStack,
) : PerkGuiElement {

    override fun getGuiIcon(): ItemStack = if(active) activeIcon else inactiveIcon

    override fun onInventoryClick(event: InventoryClickEvent) {
        TODO("Not yet implemented")
    }

}