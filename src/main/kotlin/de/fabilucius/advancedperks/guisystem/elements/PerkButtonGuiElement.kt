package de.fabilucius.advancedperks.guisystem.elements

import de.fabilucius.advancedperks.core.itembuilder.types.ItemStackBuilder
import de.fabilucius.advancedperks.data.state.PerkStateController
import de.fabilucius.advancedperks.guisystem.PerkGui
import de.fabilucius.advancedperks.guisystem.PerkGuiElement
import de.fabilucius.advancedperks.perk.Perk
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class PerkButtonGuiElement(
    private val perk: Perk,
    private val perkStateController: PerkStateController,
) : PerkGuiElement {

    //TODO redo that (rewrite and properly use perk icon)
    override fun getGuiIcon(): ItemStack = ItemStackBuilder.fromItemStack(perk.perkGuiIcon.icon)
        .setDisplayName(perk.displayName)
        .setDescription(perk.description.lines)
        .build();

    override fun onInventoryClick(event: InventoryClickEvent) = perkStateController.togglePerk(event.whoClicked as Player, perk).let {  }

}