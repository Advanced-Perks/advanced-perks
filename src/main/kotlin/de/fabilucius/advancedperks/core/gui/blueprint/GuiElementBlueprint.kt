package de.fabilucius.advancedperks.core.gui.blueprint

import de.fabilucius.advancedperks.core.gui.GuiElement
import de.fabilucius.advancedperks.core.gui.GuiElementState
import de.fabilucius.advancedperks.core.gui.blueprint.representation.GuiElementRepresentationBlueprint

data class GuiElementBlueprint(
    val function: String,
    val state: GuiElementState,
    val representations: Map<GuiElementState, GuiElementRepresentationBlueprint>,
    val slot: Int,
) {

    fun GuiElementBlueprint.toGuiElement(): GuiElement {
        return GuiElement(this.state, this.function, this.slot)
    }

}
