package de.fabilucius.advancedperks.core.gui.interpreter

import com.google.inject.Singleton
import de.fabilucius.advancedperks.core.gui.blueprint.GuiWindowBlueprint
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory

@Singleton
class GuiInterpreter {

    fun interpretBlueprint(guiWindowBlueprint: GuiWindowBlueprint): Inventory{
        val inventory = Bukkit.createInventory(null, guiWindowBlueprint.size, guiWindowBlueprint.title)

        guiWindowBlueprint.pages.forEach { guiPageBlueprint ->
            guiPageBlueprint.elements.forEach { guiElementBlueprint ->
                val itemStack = guiElementBlueprint.representations.values.first().toItemStack()
                inventory.setItem(guiElementBlueprint.slot, itemStack)
            }
        }

        return inventory
    }

}