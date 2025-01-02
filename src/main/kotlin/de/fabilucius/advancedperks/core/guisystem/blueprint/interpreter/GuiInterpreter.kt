package de.fabilucius.advancedperks.core.guisystem.blueprint.interpreter

import com.google.inject.Inject
import com.google.inject.Singleton
import de.fabilucius.advancedperks.core.guisystem.blueprint.GuiBlueprint

@Singleton
class GuiInterpreter @Inject constructor(

) {

    fun interpretBlueprint(guiBlueprint: GuiBlueprint) {
        val inventory = guiBlueprint.createInventory()
    }

}