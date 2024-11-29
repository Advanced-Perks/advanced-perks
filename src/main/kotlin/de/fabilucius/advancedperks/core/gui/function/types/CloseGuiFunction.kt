package de.fabilucius.advancedperks.core.gui.function.types

import de.fabilucius.advancedperks.core.gui.function.GuiFunction
import org.bukkit.event.inventory.InventoryClickEvent

class CloseGuiFunction : GuiFunction {

    override fun onInventoryClick(event: InventoryClickEvent) {
        if (event.isLeftClick) {
            event.whoClicked.closeInventory()
        }
    }

}