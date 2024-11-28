package de.fabilucius.advancedperks.core.gui.blueprint.representation

import org.bukkit.inventory.ItemStack

interface GuiElementRepresentationBlueprint {

    fun toItemStack(): ItemStack
}