package de.fabilucius.advancedperks.core.gui

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import de.fabilucius.advancedperks.core.gui.manager.GuiSystemManager
import org.bukkit.inventory.Inventory

class PerkGui @Inject constructor(
    @Assisted val inventory: Inventory
) : GuiWindow {

    override val elements = mutableListOf<GuiElement>()

}

interface PerkGuiFactory {
    fun create(inventory: Inventory): PerkGui
}