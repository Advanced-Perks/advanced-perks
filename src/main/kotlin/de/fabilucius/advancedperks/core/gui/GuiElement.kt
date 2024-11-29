package de.fabilucius.advancedperks.core.gui

import de.fabilucius.advancedperks.core.gui.function.GuiFunction
import org.bukkit.inventory.ItemStack

class GuiElement(
    val state: GuiElementState = GuiElementState.DEFAULT,
    val function: GuiFunction,
    val slot: Int,
) {

    fun getRepresentation(): ItemStack = TODO()

}