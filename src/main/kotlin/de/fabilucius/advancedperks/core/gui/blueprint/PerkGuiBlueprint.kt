package de.fabilucius.advancedperks.core.gui.blueprint

import de.fabilucius.advancedperks.core.gui.blueprint.representation.GuiElementRepresentationBlueprint

data class PerkGuiBlueprint (
    val title: String,
    val size: Int,
    val background: GuiElementRepresentationBlueprint?,
    val elements: List<GuiElementBlueprint>
)