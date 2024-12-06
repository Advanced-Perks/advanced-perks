package de.fabilucius.advancedperks.core.gui

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import de.fabilucius.advancedperks.core.gui.manager.GuiSystemManager
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class PerkGui @Inject constructor(
    @Assisted val inventory: Inventory
) {

    val elements = mutableListOf<GuiElement>()

    fun getElementByItemStack(itemStack: ItemStack): GuiElement = TODO("Get with the uuid key from persistentcontainer")

}

interface PerkGuiFactory {
    fun create(inventory: Inventory): PerkGui
}