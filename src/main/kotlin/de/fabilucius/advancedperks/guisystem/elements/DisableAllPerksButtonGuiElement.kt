package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.data.state.PerkStateController
import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class DisableAllPerksButtonGuiElement(
    private val perkStateController: PerkStateController,
    private val icon: ItemStack,
) : PerkGuiElement {

    override fun getGuiIcon(): ItemStack = icon

    override fun onInventoryClick(event: InventoryClickEvent) {
        if(event.isLeftClick && event.whoClicked is Player){
            perkStateController.disableAllPerks(event.whoClicked as Player)
        }
    }

}