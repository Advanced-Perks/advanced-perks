package de.fabilucius.advancedperks.core.gui

import org.bukkit.inventory.ItemStack

interface GuiWindow {
    val elements: MutableList<GuiElement>

    fun getElementByItemStack(itemStack: ItemStack): GuiElement = TODO("Get with the uuid key from persistentcontainer")
}