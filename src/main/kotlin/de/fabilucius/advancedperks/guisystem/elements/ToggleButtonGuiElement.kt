package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.data.PerkData
import de.fabilucius.advancedperks.data.state.PerkStateController
import de.fabilucius.advancedperks.guisystem.PerkGui
import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import de.fabilucius.advancedperks.perk.Perk
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ToggleButtonGuiElement(
    private val activeIcon: ItemStack,
    private val inactiveIcon: ItemStack,
    private val perk: Perk,
    private val perkData: PerkData,
    private val perkStateController: PerkStateController,
) : PerkGuiElement {

    override fun getGuiIcon(): ItemStack = if (perkData.isPerkEnabled(perk)) activeIcon else inactiveIcon

    override fun onInventoryClick(event: InventoryClickEvent) =
        perkStateController.togglePerk(event.whoClicked as Player, perk).let {}

}