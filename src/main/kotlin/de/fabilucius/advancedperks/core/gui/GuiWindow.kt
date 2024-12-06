package de.fabilucius.advancedperks.core.gui

import org.bukkit.inventory.ItemStack

interface GuiWindow {
    val elements: MutableList<GuiElement>

}