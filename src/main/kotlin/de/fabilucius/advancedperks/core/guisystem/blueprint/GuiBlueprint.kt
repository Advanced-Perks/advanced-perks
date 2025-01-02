package de.fabilucius.advancedperks.core.guisystem.blueprint

import org.bukkit.Bukkit

data class GuiBlueprint(
    val title: String,
    val size: Int,
    val elements: List<GuiElementBlueprint>
) {

    fun createInventory() = Bukkit.createInventory(null, this.size, this.title)

}