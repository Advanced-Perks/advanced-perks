package de.fabilucius.advancedperks.core.gui.blueprint

import de.fabilucius.advancedperks.core.gui.GuiElementState
import de.fabilucius.advancedperks.core.gui.blueprint.representation.GuiElementRepresentationBlueprint

data class GuiElementBlueprint(
    val function: String,
    val state: GuiElementState,
    val representations: Map<GuiElementState, GuiElementRepresentationBlueprint>,
    val slot: Int,
)
