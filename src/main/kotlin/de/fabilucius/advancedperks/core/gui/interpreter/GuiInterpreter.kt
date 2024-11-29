package de.fabilucius.advancedperks.core.gui.interpreter

import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.core.gui.PerkGuiFactory
import de.fabilucius.advancedperks.core.gui.blueprint.PerkGuiBlueprint
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory

@Singleton
class GuiInterpreter @Inject constructor(
    private val perkGuiFactory: PerkGuiFactory
) {

    fun interpretBlueprint(perkGuiBlueprint: PerkGuiBlueprint): Inventory{
        val inventory = Bukkit.createInventory(null, perkGuiBlueprint.size, perkGuiBlueprint.title)

        val perkGui = perkGuiFactory.create(inventory)

        return perkGui.inventory
    }

}